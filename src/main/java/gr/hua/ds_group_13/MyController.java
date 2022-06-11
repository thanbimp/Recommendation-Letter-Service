package gr.hua.ds_group_13;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MyController {

    @Autowired
    KeycloakUserService keycloakUserService;
    @Autowired
    EmailSender emailSender;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private LetterRepository letterRepository;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletResponse response) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User authUser = KeycloakUserService.getUser(keycloakAuthenticationToken.getAccount().getPrincipal().toString());
        if (authUser.getAccType().equals("STUDENT")) {
            return "redirect:student/dashboard";
        } else if (authUser.getAccType().equals("PROFESSOR")) {
            return "redirect:professor/dashboard";
        } else if (authUser.getAccType().equals("ADMIN")) {
            return "redirect:admin/dashboard";
        }
        return "redirect:/error";
    }



    @GetMapping("/admin/dashboard")
    private String adminPage(HttpServletResponse response) {
        return "admin_page";
    }

    @GetMapping("/professor/dashboard")
    private String profPage(HttpServletResponse response) {
        return "professor_dashboard";
    }

    @GetMapping("/student/dashboard")
    private String studPage(HttpServletResponse response) {
        return "dashboard";
    }


    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }


    @PatchMapping("/admin/delete")
    @ResponseBody
    private String deleteUser(@RequestParam Map<String, String> body, HttpServletResponse response) {
        if (KeycloakUserService.getUser(KeycloakUserService.getUIDfromEmail(body.get("userEmail"))).getEmail() != null) {
            KeycloakUserService.deleteUser(KeycloakUserService.getUIDfromEmail(body.get("userEmail")));
            return "true";
        } else  {
            return "false";
        }
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        return "register";
    }

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    private String addUser(@RequestParam Map<String, String> body, HttpSession session) {
        User user = new User(body.get("email"), body.get("password"), body.get("fname"), body.get("lname"), body.get("phoneNo"), body.get("accType"));
        keycloakUserService.addUser(user);
        return "redirect:dashboard";
    }


    @GetMapping(
            value = "/currUser",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private User getCurrUser() {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return KeycloakUserService.getUser(keycloakAuthenticationToken.getAccount().getPrincipal().toString());
    }

    @GetMapping(
            value = "/admin/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private User getUser(@RequestParam Map<String, String> body) {
        return KeycloakUserService.getUser(KeycloakUserService.getUIDfromEmail(body.get("email")));
    }

    @GetMapping(
            value = "/admin/allUsers",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private List<User> getAllUsers() {
       return KeycloakUserService.getAllUsers();
    }

    @GetMapping(
            value = {"/student/application", "/professor/application"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private Application getApplication(@RequestParam Map<String, String> body) {
        return applicationRepository.findApplicationByAppId(body.get("appID")).get();
    }

    @PatchMapping(value = "/user",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    private void updateUser(@RequestParam Map<String, String> body) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser.setFName(body.get("fname"));
        currentUser.setLName(body.get("lname"));
        currentUser.setPhoneNo(body.get("phoneNo"));
        //userRepository.save(currentUser);
    }

    @PostMapping(value = "/student/application")
    @ResponseBody
    private String addNewApplication(@RequestParam Map<String, String> body) {
        if (KeycloakUserService.getUser(KeycloakUserService.getUIDfromEmail(body.get("profEmail"))).getEmail() != null) {
           if (!KeycloakUserService.getInstance().get(KeycloakUserService.getUIDfromEmail(body.get("profEmail"))).roles().realmLevel().listAll().get(0).toString().equals("PROFESSOR")) {
                return "false";
            } else {
                Application application = new Application(body.get("profEmail"), body.get("appBody"), body.get("fname"), body.get("lname"), body.get("fromMail"), body.get("toMail"));
                applicationRepository.save(application);
               return "true";
            }
        } else {
            return "false";
        }
    }


    @GetMapping(
            value = "/student/new_application"
    )
    private String newApplication() {
        return "new_application";
    }


    @GetMapping(
            value = "/professor/letter",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private String getLetter(@RequestParam Map<String, String> body) {
        if (letterRepository.findLetterByAppID(applicationRepository.getById(body.get("appID"))).isPresent()) {
            return letterRepository.findLetterByAppID(applicationRepository.getById(body.get("appID"))).get().getBody();
        }
        return null;
    }


    @PostMapping(
            value = "/professor/letter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    private void addNewLetter(@RequestParam Map<String, String> body, HttpServletResponse response) {
        Letter letter;
        if (letterRepository.findLetterByAppID(applicationRepository.getById(body.get("appID"))).isPresent()) {
            letter = letterRepository.findLetterByAppID(applicationRepository.getById(body.get("appID"))).get();
            letter.setBody(body.get("body"));
        } else {
            Application tempApp = applicationRepository.getById(body.get("appID"));
            letter = new Letter(KeycloakUserService.getUser(KeycloakUserService.getUIDfromEmail(tempApp.getProfEmail())).getFName(),KeycloakUserService.getUser(KeycloakUserService.getUIDfromEmail(tempApp.getProfEmail())).getLName(), body.get("body"), tempApp, body.get("receiverEmail"));
        }
        letterRepository.save(letter);
        Application relatedApplication = applicationRepository.getById(body.get("appID"));
        relatedApplication.setLetterId(letter.getId());
        applicationRepository.save(relatedApplication);
    }

    @PatchMapping(
            value = "/professor/letter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    private void sendLetter(@RequestParam Map<String, String> body, HttpServletResponse response) throws MessagingException {
        Letter tempLtr = letterRepository.getById(applicationRepository.getById(body.get("appID")).getLetterId());
        emailSender.SendEmail(tempLtr);
        letterRepository.delete(letterRepository.getById(applicationRepository.getById(body.get("appID")).getLetterId()));
        applicationRepository.delete(applicationRepository.getById(body.get("appID")));
    }

    @GetMapping(value = "/professor/write_letter")
    private String showWriteLetter(@RequestParam String appID, HttpServletResponse response) {
        return "write_letter";
    }

    @GetMapping(
            value = "/student/myApplications",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private List<Application> getApplicationsForStudent() {
        List<Application> AllApplications = applicationRepository.findAll();
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User authUser = KeycloakUserService.getUser(keycloakAuthenticationToken.getAccount().getPrincipal().toString());
        return AllApplications.stream().filter(o -> o.getFromMail().equals(authUser.getEmail())).collect(Collectors.toList());
    }

    @GetMapping(
            value = "/professor/myApplications",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private List<Application> getApplicationsForProf(HttpServletResponse response) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User authUser = KeycloakUserService.getUser(keycloakAuthenticationToken.getAccount().getPrincipal().toString());
        List<Application> AllApplications = applicationRepository.findAll();
        //only return applications to the professor
        return AllApplications.stream().filter(o -> o.getProfEmail().equals(authUser.getEmail())).collect(Collectors.toList());
    }


    @PatchMapping
            (value = "/professor/application",
                    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    private void ApproveApplication(@RequestParam Map<String, String> body, HttpServletResponse response) {
        String appID = body.get("appID");
        boolean accept = Boolean.parseBoolean(body.get("accepted"));
        Application tempApplication = applicationRepository.getById(appID);
        if (accept) {
            tempApplication.setAccepted(true);
            applicationRepository.save(tempApplication);
        } else {
            if (letterRepository.findLetterByAppID(tempApplication).isPresent()) {
                letterRepository.delete(letterRepository.findLetterByAppID(tempApplication).get());
            }

            applicationRepository.delete(tempApplication);
            if (letterRepository.findLetterByAppID(tempApplication).isPresent()) {
                letterRepository.delete(letterRepository.getById(tempApplication.getLetterId()));
            }

        }
    }


    @GetMapping(value = "/profile")
    private String profilePage() {
        return "profile";
    }


}
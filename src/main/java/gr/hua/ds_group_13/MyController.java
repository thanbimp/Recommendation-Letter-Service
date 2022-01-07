package gr.hua.ds_group_13;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MyController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityUserDetailsService userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private LetterRepository letterRepository;


    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpSession session) {
        session.setAttribute(
                "error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION")
        );
        return "login";
    }


    @GetMapping("/dashboard")
    public String dashboard(){
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (checkIfProfessor()){
            return "professor_dashboard";
        }else{
            return "dashboard";
        }

    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public String addUser(@RequestParam Map<String, String> body,HttpSession session) {
        User user = new User(body.get("email"),passwordEncoder.encode(body.get("password")),body.get("fname"),body.get("lname"),(short) Integer.parseInt(body.get("accType")),body.get("phoneNo"));
        userDetailsManager.createUser(user);
        session.setAttribute("registered",true);
        return "redirect:/login";
    }


    @GetMapping(
            value = "/user",
            produces=MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private User getUser(@RequestParam Map<String, String> body){
        return userRepository.findUserByEmail(body.get("email")).get();
    }
    @GetMapping(
            value = "/application",
            produces=MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    private Application getApplication(@RequestParam Map<String, String> body){
        return applicationRepository.findApplicationByAppId(body.get("appId")).get();
    }

    @PostMapping(value="/application")
    @ResponseBody
    public String addNewApplication(@RequestParam Map<String, String> body){
        if(userRepository.findUserByEmail(body.get("profEmail")).isPresent()){
            if(userRepository.findUserByEmail(body.get("profEmail")).get().getAccType()==0){
                return "false";
            }
            else {
                Application application = new Application(body.get("profEmail"), body.get("appBody"), body.get("fname"), body.get("lname"), body.get("fromMail"));
                applicationRepository.save(application);
                return "true";
            }
        }
        else{
            return "false";
        }
    }


    @GetMapping(
            value = "/new_application"
    )
    public String newApplication(){
        return "/new_application";
    }


    @PostMapping(
            value="/letter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public void addNewLetter(@RequestParam Map<String, String> body,HttpServletResponse response) {
        if(checkIfProfessor()) {
            Letter letter = new Letter(body.get("fName"), body.get("lName"), body.get("body"), applicationRepository.getById(body.get("appID")));
            letterRepository.save(letter);
        }
        else {
            response.setStatus(403);
        }
    }

    @PatchMapping(
            value="/letter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    public void sendLetter(@RequestParam Map<String, String> body,HttpServletResponse response){
        if(checkIfProfessor()) {
            try {
                EmailSender.SendEmail(letterRepository.getById(body.get("letterID")), "NEEDS TO RETRIEVE EMAIL TO SEND TO");

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        else {
            response.setStatus(403);
        }
    }

    @GetMapping(value = "/write_letter")
    public String showWriteLetter(@RequestParam String appID,HttpServletResponse response){
        if(checkIfProfessor()) {
            return "write_letter";
        }
        else{
            response.setStatus(403);
        }
        return null;
    }

    @GetMapping(
            value = "/myApplications",
            produces=MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Application> getApplicationsForStudent(){
        List<Application> AllApplications = applicationRepository.findAll();
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return AllApplications.stream().filter(o -> o.getFromMail().equals(authUser.getEmail())).collect(Collectors.toList());
    }

    @GetMapping(
            value = "/myApplicationsProf",
            produces=MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Application> getApplicationsForProf(HttpServletResponse response){
        if(checkIfProfessor()) {
            User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Application> AllApplications = applicationRepository.findAll();
            //only return applications to the professor
            return AllApplications.stream().filter(o -> o.getProfEmail().equals(authUser.getEmail())).collect(Collectors.toList());
        }else{
            response.setStatus(403);
            return null;
        }
    }

    @GetMapping(value = "/applicationByID",
                produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Application getApplicationByID(@RequestParam String appID){
       Optional<Application> application = applicationRepository.findById(appID);
       //need to do this cause of optional class
       return application.get();
    }

    @PatchMapping
            (value = "/application",
            consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public void ApproveApplication(@RequestParam Map<String, String> body,HttpServletResponse response){
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(checkIfProfessor()) {
            String appID = body.get("appID");
            boolean accept = Boolean.parseBoolean(body.get("accepted"));
            Application tempApplication = applicationRepository.getById(appID);
            if (accept) {
                tempApplication.setAccepted(true);
                applicationRepository.save(tempApplication);
            } else {
                applicationRepository.delete(tempApplication);
            }
        }
        else {
            response.setStatus(403);
        }
        }



    private String getErrorMessage(HttpServletRequest request, String key) {
        Exception exception = (Exception) request.getSession().getAttribute(key);
        String error;
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Invalid username and password!";
        }
        return error;
    }

    private Boolean checkIfProfessor(){
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUser.getAccType() == 1;
    }
}
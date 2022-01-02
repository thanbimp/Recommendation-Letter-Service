package gr.hua.ds_group_13;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MyController {

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
        return "dashboard";
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
    public void addUser(@RequestParam Map<String, String> body) {
        User user = new User(body.get("email"),passwordEncoder.encode(body.get("password")),body.get("fname"),body.get("lname"),(short) Integer.parseInt(body.get("accType")));
        userDetailsManager.createUser(user);
    }


    @PostMapping(
            value="/application",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public void addNewApplication(@RequestParam Map<String, String> body){
        Application application=new Application(body.get("profEmail"),body.get("appBody"),body.get("fName"),body.get("lName"));
        applicationRepository.save(application);
    }


    @PostMapping(
            value="/add_letter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public void addNewLetter(@RequestParam Map<String, String> body) {
       Letter letter = new Letter(body.get("fName"),body.get("lName"),body.get("body"),applicationRepository.getById(body.get("appID")));
       letterRepository.save(letter);
    }

    @PostMapping(
            value="/send_letter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    public void sendLetter(@RequestParam Map<String, String> body){
        try {
            EmailSender.SendEmail(letterRepository.getById(body.get("letterID")),"NEEDS TO RETRIEVE EMAIL TO SEND TO");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @GetMapping(
            value = "/profApplications",
            produces=MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Application> getApplicationsByProfEmail(@RequestParam Map<String, String> body){
        List<Application> AllApplications = applicationRepository.findAll();
        List<Application> ProfApplications = AllApplications.stream().filter(o -> o.getProfEmail().equals(body.get("email"))).collect(Collectors.toList());
        return ProfApplications;
    }


    @PatchMapping
            (value = "/application",
            consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public void ApproveApplication(@RequestParam Map<String, String> body){
        String appID= body.get("appID");
        Boolean accept= Boolean.parseBoolean(body.get("accepted"));
        Application tempApplication= applicationRepository.getById(appID);
        if (accept){
            tempApplication.setAccepted(true);
            applicationRepository.save(tempApplication);
        }
        else applicationRepository.delete(tempApplication);
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
}
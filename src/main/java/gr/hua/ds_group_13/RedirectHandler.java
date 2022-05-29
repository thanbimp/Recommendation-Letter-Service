package gr.hua.ds_group_13;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RedirectHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        UserRepresentation userRepresentation = (UserRepresentation) authentication.getPrincipal();
        //User currUser = new User(userRepresentation.getEmail(),null,userRepresentation.getFirstName(),userRepresentation.getLastName(),userRepresentation.getAttributes().get("phoneNo"),"STUDENT");

        String redirectURL = request.getContextPath();

        //if (userDetails.hasRole("ROLE_STUDENT")) {
            redirectURL = "/student/dashboard";
        //} else if (userDetails.hasRole("ROLE_PROFESSOR")) {
            redirectURL = "/professor/dashboard";
      // } else if (userDetails.hasRole("ROLE_ADMIN")) {
            redirectURL = "/admin/dashboard";
    }

       // response.sendRedirect(redirectURL);

   // }

}
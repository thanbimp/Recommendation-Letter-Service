package gr.hua.ds_group_13;


import lombok.AllArgsConstructor;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gr.hua.ds_group_13.KeycloakConfig.keycloak;

@AllArgsConstructor
@Service
public class KeycloakUserService {

    @Value("${keycloak.realm}")
    private String realmName;

    public static String realmName_static;

    public KeycloakUserService() {
    }

    @Value("${keycloak.realm}")
    public void setRealmName_static(String realmName) {
        KeycloakUserService.realmName_static = realmName;
    }

    public void addUser(User user){
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(user.getPassword());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(user.getFName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setLastName(user.getLName());
        userRepresentation.setCredentials(Collections.singletonList(credential));
        userRepresentation.setEnabled(true);
        userRepresentation.setAttributes(Collections.singletonMap("phoneNo",Collections.singletonList(user.getPhoneNo())));

        UsersResource instance = getInstance();
        RoleRepresentation roleRepresentation = keycloak.realm(realmName).roles().get(user.getAccType()).toRepresentation();
        instance.create(userRepresentation);
        String uID = getInstance().search(user.getEmail()).get(0).getId();
        UserResource userResource = getInstance().get(uID);
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    public static String getUIDfromEmail(String email){
        UsersResource usersResource = getInstance();
        return usersResource.search(email).get(0).getId();
    }

    public static User getUser(String uID){
        UsersResource usersResource = getInstance();
        UserRepresentation userRepresentation = usersResource.get(uID).toRepresentation();
        return new User(userRepresentation.getEmail(),null,userRepresentation.getFirstName(),userRepresentation.getLastName(),userRepresentation.getAttributes().get("phoneNo").get(0),usersResource.get(uID).roles().realmLevel().listAll().get(0).toString());
    }

    public void updateUser(String userId, User user){
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(user.getPassword());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(user.getFName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setLastName(user.getLName());
        userRepresentation.setEmail(user.getPassword());
        userRepresentation.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = getInstance();
        usersResource.get(userId).update(userRepresentation);
    }
    public static void deleteUser(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .remove();
    }


  public static List<User> getAllUsers(){
      List<UserRepresentation>  userRepresentationList = getInstance().list();
      UsersResource usersResource = getInstance();
      List<User> usersList = new ArrayList<>();
      for (UserRepresentation userRepresentation : userRepresentationList) {
          usersList.add(new User(userRepresentation.getEmail(), null, userRepresentation.getFirstName(), userRepresentation.getLastName(), userRepresentation.getAttributes().get("phoneNo").get(0), usersResource.get(userRepresentation.getId()).roles().realmLevel().listAll().get(0).toString()));
      }
      return usersList;
  }
    public static UsersResource getInstance(){
        return KeycloakConfig.getInstance().realm(realmName_static).users();
    }


}

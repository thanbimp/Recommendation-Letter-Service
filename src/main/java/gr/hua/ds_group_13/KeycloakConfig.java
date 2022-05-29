package gr.hua.ds_group_13;

import com.itextpdf.xmp.XMPMeta;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class KeycloakConfig {


    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Resource
    Environment environment;

    public static Environment environment_static ;

    @Resource
    public void setEnvironment_static(Environment environment){
        KeycloakConfig.environment_static = environment;
    }



    static Keycloak keycloak = null;



    public KeycloakConfig() {
    }

    public static Keycloak getInstance(){
        if(keycloak == null){



            final String serverUrl = environment_static.getProperty("keycloak.auth-server-url");
            final String realm = environment_static.getProperty("keycloak.realm") ;
            final String clientId = environment_static.getProperty("keycloak.resource");
            final String clientSecret = environment_static.getProperty("keycloak.credentials.secret");

            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build())
                    .build();
        }
        return keycloak;
    }
}

package africa.learnspace.usermanagement.iam;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCloakConfig {
    @Value("${keycloak.server}")
    private String KEYCLOAK_SERVER_URL;


    @Value("${keycloak.username}")
    private String KEYCLOAK_USERNAME;
    @Value("${keycloak.password}")
    private String KEYCLOAK_PASSWORD;
    @Bean
    public Keycloak keycloakConfigResolver() {
        return Keycloak.getInstance(
                KEYCLOAK_SERVER_URL,
                "master",
                KEYCLOAK_USERNAME,
                KEYCLOAK_PASSWORD,
                "admin-cli");
    }
}

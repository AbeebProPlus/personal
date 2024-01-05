package africa.learnspace.usermanagement.iam;

import com.nimbusds.jwt.JWTClaimNames;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    @Value("${keycloak.principal_attribute}")
    private String principalAttribute;
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractRealmRoles(jwt).stream()).toList();

        String claimName = principalAttribute == null ? JWTClaimNames.SUBJECT : principalAttribute;
        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(claimName));
    }

    private Collection<SimpleGrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null) return Collections.emptySet();
        Collection<String> realmRoles = getRolesFromRealm(realmAccess);
        if (realmRoles == null) return Collections.emptySet();
        return realmRoles.stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private Collection<String> getRolesFromRealm(Map<String, Object> realmAccess) {
        return (Collection<String>) realmAccess.get("roles");
    }
}
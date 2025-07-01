package org.spring.anotherinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ACESS {


    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/access-token")
    public ResponseEntity<?> getAccessToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        if (client == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String accessToken = client.getAccessToken().getTokenValue();

        // Extract user information
        OAuth2User principal = authentication.getPrincipal();
        String userId = principal.getName(); // Or extract a specific attribute

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("userId", userId);

        return ResponseEntity.ok(response);
    }
}

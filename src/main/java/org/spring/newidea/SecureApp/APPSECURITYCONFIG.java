//package org.spring.projectangular.SecureApp;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class APPSECURITYCONFIG {
//
//    @Autowired
//    private Myuserdetails userdetails;
//
//   @Autowired
// private JwtFilter jwtFilters;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(csrf -> csrf.disable());
//       http
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/LOGIN","oauth2/authorization/google","/SIGNUP","/uploads/**", "/css/**", "/js/**", "/images/**").permitAll()
//                        // Authenticate any other requests
//                        .anyRequest().authenticated()
//                )
////
////
//               .oauth2Login( Customizer.withDefaults()
//               )
//                // Logout configuration
//                .logout((logout) -> logout
//                        .logoutUrl("/logout")
////                        .logoutSuccessUrl("/LOGINN?logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                );
//
//
////        http.formLogin(Customizer.withDefaults());
//        http.httpBasic(Customizer.withDefaults());
////        http.sessionManagement(session -> session
////                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use sessions if required for form login
////                )
////                .addFilterBefore(jwtFilters, UsernamePasswordAuthenticationFilter.class);
//http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).addFilterBefore(jwtFilters, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider =new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
//        provider.setUserDetailsService(userdetails);
//        return provider;
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
////    @Bean
////    public UserDetailsService userDetailsService() {
////        User user=userrepo.findByUsername(username);
////        if(user==null)
////            throw new UsernameNotFoundException(username);
////        return new UserPrincipal(user);
////    }
//}
package org.spring.newidea.SecureApp;

import org.spring.newidea.Model.User;
import org.spring.newidea.Repositary.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class APPSECURITYCONFIG {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private JWTSERVICE JTSERVICE;
    @Autowired
    private Userrepo userRepository;
    private String getGitHubEmail(OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        if (email != null) {
            return email;
        }

        // If email is null, fetch from GitHub's /emails API
        // You may need to make an API call to fetch emails
        // Here is a simplified example

        // Get access token
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authToken.getAuthorizedClientRegistrationId(),
                authToken.getName()
        );
        String accessToken = client.getAccessToken().getTokenValue();

        // Make API call to GitHub to get emails
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> emails = response.getBody();
        if (emails != null) {
            for (Map<String, Object> emailObj : emails) {
                Boolean primary = (Boolean) emailObj.get("primary");
                Boolean verified = (Boolean) emailObj.get("verified");
                if (primary != null && primary && verified != null && verified) {
                    return (String) emailObj.get("email");
                }
            }
        }

        return null;
    }


    private User getOrCreateUser(String email, String name) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // User does not exist, create new
            user = new User();
            String NAMES[]=name.split(" ");

            user.setEmail(email);
            user.setName(NAMES[0]);
            user.setLastname(NAMES[1]);
            userRepository.save(user);
        }
        return user;
    }


    @Autowired
    private Myuserdetails userdetails;

    @Autowired
    private JwtFilter jwtFilters;

    // Security configuration for API endpoints using JWT authentication
    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Apply this security chain to API endpoints
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/LOGIN", "/api/SIGNUP").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilters, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Security configuration for web endpoints using OAuth2 login
    @Bean
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // Apply this security chain to all other endpoints
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/LOGIN",
                                "/SIGNUP",
                                "/oauth2/**",
                                "/uploads/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/assets/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
                            OAuth2User oauthUser = authToken.getPrincipal();

                            // Determine the provider (registrationId)
                            String registrationId = authToken.getAuthorizedClientRegistrationId();

                            String email = null;
                            String name = null;

                            // Extract user attributes based on the provider
                            if ("google".equals(registrationId)) {
                                email = oauthUser.getAttribute("email");
                                name = oauthUser.getAttribute("name");
                            } else if ("github".equals(registrationId)) {
                                email = getGitHubEmail(oauthUser);
                                name = oauthUser.getAttribute("name");
                                if (name == null) {
                                    name = oauthUser.getAttribute("login"); // Fallback to username
                                }
                            }

                            // Handle case where email is null
                            if (email == null) {
                                // Redirect to an error page or handle accordingly
                                response.sendRedirect("/error?message=Email+not+found");
                                return;
                            }

                            // Retrieve or create user in your database
                            User user = getOrCreateUser(email, name);

                            // Generate JWT token
                            String jwtToken = JTSERVICE.GenerateToken(user.getEmail());

                            // Build redirect URL with token and user ID
                            String redirectUrl = "http://localhost:4200/token-handler?token=" + jwtToken + "&userId=" + user.getId()+"&name="+name;

                            // Redirect to Angular app
                            response.sendRedirect(redirectUrl);
                        })
                )

                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userdetails);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
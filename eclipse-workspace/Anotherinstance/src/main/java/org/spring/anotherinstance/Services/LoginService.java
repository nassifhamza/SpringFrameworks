package org.spring.anotherinstance.Services;

import org.spring.anotherinstance.Model.CREDENTIALS;
import org.spring.anotherinstance.Model.User;
import org.spring.anotherinstance.Model.userDto;
import org.spring.anotherinstance.Repositary.Userrepo;
import org.spring.anotherinstance.SecureApp.JWTSERVICE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
@Autowired
private JWTSERVICE JTSERVICE;
    @Autowired
    Userrepo userRepo;
    @Autowired
    AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
//    public User CHECK(String username)
//    {
//        return userRepo.findByUsername(username);
//    }

    public ResponseEntity<String> save(User user) {
        if (!userRepo.existsByUsername(user.getUsername()) && !userRepo.existsByEmail(user.getEmail())) {
            user.setPassword(encoder.encode(user.getPassword()));
            userRepo.save(user);

            return ResponseEntity.ok("ACCOUNT HAS BEEN CREATED !"); // Success
        } else if(userRepo.existsByUsername(user.getUsername())) {

           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("the Username Already Exists");
        }
        else if(userRepo.existsByEmail(user.getEmail())) {
           return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("the Email Already Exists");

        }
        return null;
    }
public User getUser(String UID){

        return userRepo.findById(UID).orElse(null);

}
    public userDto verify(CREDENTIALS creds) {

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getText(),creds.getPassword()));
        if(auth.isAuthenticated())
        {
            User user=userRepo.findByUsernameOrEmail(creds.getText(), creds.getText());
            return new userDto(user,JTSERVICE.GenerateToken(creds.getText()));
        }

        return new userDto();
    }
    public ResponseEntity<Map<String, String>> USERUPDATE(String UID, User user) {
        if (userRepo.existsById(UID)) {
            user.setId(UID);
            User user1=userRepo.findById(UID).orElse(null);
            user.setProfileImage(user1.getProfileImage());
            user.setPassword(user1.getPassword());
            userRepo.save(user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "ACCOUNT HAS BEEN UPDATED!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Something went wrong!");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    public String USERUPDATE(String UID, User user) {
//        try {
//            // Check if the user exists
//            if (!userRepo.existsById(UID)) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + UID + " not found.");
//            }
//
//            // Set the user ID and save the updated user data
//            user.setId(UID);
//            userRepo.save(user);
//
//            return "ACCOUNT HAS BEEN UPDATED!";
//
//        } catch (IllegalArgumentException ex) {
//            // Handle any argument-related exceptions
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input data: " + ex.getMessage(), ex);
//        } catch (Exception ex) {
//            // Catch any other exceptions and return a generic error message
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong: " + ex.getMessage(), ex);
//        }
//    }

//    public void LOGIN(String text, String password, HttpServletResponse res) throws IOException {
//        if(userRepo.existsByEmailOrUsername(text,text)) {
//            User user= userRepo.findByUsernameOrEmail(text,text);
//            if(user.getPassword().equals(password)) {
//                res.sendRedirect("/CALCUL");
//            }
//            else {
//                res.sendRedirect("/");
//            }
//        }
//        else {
//            res.sendRedirect("/");
//        }
//    }

}

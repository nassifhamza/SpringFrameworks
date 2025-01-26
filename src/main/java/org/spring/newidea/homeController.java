package org.spring.newidea;

import org.spring.newidea.Model.*;
import org.spring.newidea.Repositary.Userrepo;
import org.spring.newidea.Services.JOBSService;
import org.spring.newidea.Services.LoginService;
import org.spring.newidea.Services.STORAGE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class homeController {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    LoginService LOGR;
    @Autowired
    JOBSService J;
    @Autowired
    Userrepo userrepo;
    @Autowired
    STORAGE storage;

    @PostMapping("/LOGIN")
    @CrossOrigin
    public userDto LOGIN(@RequestBody CREDENTIALS creds) {
        return LOGR.verify(creds);
    }

    @PostMapping("/SIGNUP")
    @CrossOrigin
    public ResponseEntity<String> SIGNUP(@RequestBody User user) {


        return LOGR.save(user);


    }

    @PostMapping("/addjob/{UID}")
    @CrossOrigin
    public ResponseEntity<String> addjob(@PathVariable("UID") String UID, @RequestBody JOBS JOB) {

        return J.addjob(UID, JOB);
    }

    @GetMapping("/TEST")
    @CrossOrigin
    public ResponseEntity<String> TEST() {
        return ResponseEntity.ok("GOOOD");
    }

    @GetMapping("/SEARCH/{name}/{username}")
    @CrossOrigin
    public List<JOBS> SEARCH(@PathVariable("name") String name, @PathVariable("username") String username) {
        return J.getallJOBS(name, username);
    }

    @GetMapping("/ALLJOBS/{ID}")
    @CrossOrigin
    public List<JOBS> ALLJOBS(@PathVariable("ID") String name) {
        return J.getallJOBSByID(name);
    }

    @DeleteMapping("/clears/{name}/{ID}")
    @CrossOrigin
    public void CLEAR(@PathVariable("name") String name, @PathVariable("ID") String ID) {
        J.DeleteALLBYname(ID, name);
    }

    @DeleteMapping("/DELETEONE/{ID}/{UID}")
    @CrossOrigin
    public void DELETEONE(@PathVariable("ID") String ID, @PathVariable("UID") String UID) {
        J.DELETEBYID(ID, UID);
    }

    @GetMapping("/DETAILED/{ID}/{UID}")
    @CrossOrigin
    public JOBS GetByID(@PathVariable("ID") String ID, @PathVariable("UID") String UID) {
        return J.getOne(UID, ID);
    }

    @PutMapping("/UPDATEONE/{ID}")
    @CrossOrigin
    public void UPDATEONE(@PathVariable("ID") String ID, @RequestBody JOBS JOB) {

        J.UpdateOne(ID, JOB);

    }

    @DeleteMapping("/CLEARALL/{UID}")
    @CrossOrigin
    public void CLEARALL(@PathVariable("UID") String UID) {
        J.CLEARALL(UID);
    }

    @GetMapping("/GETUSER/{UID}")
    @CrossOrigin
    public User GETUSER(@PathVariable("UID") String UID) {
        return LOGR.getUser(UID);
    }


    @PutMapping("/PROFILESAVE/{UID}")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> PROFILESAVE(@PathVariable("UID") String UID, @RequestBody User user) {
        return LOGR.USERUPDATE(UID, user);
    }

    @PutMapping("/SETPHOTO/{UID}")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> uploadPhoto(@PathVariable("UID") String UID, @RequestParam("image") MultipartFile file) {
        User user = userrepo.findById(UID).orElse(null);

        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            // Save the image to file system or cloud storage and store the file path/URL in the database

            String filePath = storage.saveFile(file);  // Implement saveFile method to handle file storage

            user.setProfileImage(filePath); // Store file path/URL
            userrepo.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile photo updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error updating profile photo");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Resetpassword/{UID}")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> Resetpassword(@PathVariable("UID") String UID, @RequestBody CREDENTIALS creds) {


        User user =userrepo.findById(UID).orElse(null);
        creds.setText(user.getUsername());
        userDto RESULT = LOGR.verify(creds);
        if(RESULT.getToken()!=null)
        {
            user.setPassword(encoder.encode(creds.getNewpassword()));
            userrepo.save(user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "The Password Has Been Updated!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The Old Passowrd is Wrong!");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }




    }







}

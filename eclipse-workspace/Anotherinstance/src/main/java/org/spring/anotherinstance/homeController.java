package org.spring.anotherinstance;

import org.spring.anotherinstance.Model.*;
import org.spring.anotherinstance.Repositary.Userrepo;
import org.spring.anotherinstance.Repositary.VIDEOSREPO;
import org.spring.anotherinstance.Services.JOBSService;
import org.spring.anotherinstance.Services.LoginService;
import org.spring.anotherinstance.Services.STORAGE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@CrossOrigin(origins = "http://localhost:3000")
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
    @Autowired
    VIDEOSREPO VIDEOREPO;

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

    @GetMapping("/FETCHPROFILE/{user}")
    @CrossOrigin
    public User FETCHPROFILE(@PathVariable("user") String username) {
        return userrepo.findByUsernameOrEmail(username,username);
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
            response.put("profileImage", filePath);
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


    @PostMapping("/uploadvideo/{UID}")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> uploadVideo(@PathVariable("UID") String UID,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();
        User user = userrepo.findById(UID).orElse(null);
        if (user == null) {

            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            // Save the video file and get its storage path
            String filePath = storage.saveFile(file);

            // Create a new video instance and populate it with the provided data


           VIDEOS video = new VIDEOS();
           video.setUser(user);
            video.setTitle(title);
            video.setDescription(description);
            video.setDate(date);
            video.setLocation(filePath);

            // Save the video instance to MongoDB
            VIDEOREPO.save(video);

            response.put("message", "Video uploaded successfully");
            response.put("video", video);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            response.put("message", "Error uploading video");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

@DeleteMapping("/deletevideo/{id}/{UID}")
    @CrossOrigin
    public ResponseEntity<Map<String,String>> deleteVideo(@PathVariable("id") String ID, @PathVariable("UID") String UID) {
        User user = userrepo.findById(UID).orElse(null);
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        VIDEOREPO.deleteByUserIdAndId(UID,ID);
        response.put("message", "Video deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
}

@GetMapping("/allvideos/{UID}")
    @CrossOrigin
    public List<VIDEOS> FETCHALLVIDEOS(@PathVariable("UID") String UID)
{
    return VIDEOREPO.findByUserId(UID);
}

    @GetMapping("/SPECIFICVIDEO/{UID}/{ID}")
    @CrossOrigin
    public List<VIDEOS> FETCHALLVIDEOS(@PathVariable("UID") String UID,@PathVariable("ID") String ID)

    {
        return VIDEOREPO.findByIdAndUserId(ID,UID);
    }


}

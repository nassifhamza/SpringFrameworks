package org.spring.newidea.Services;

import org.spring.newidea.Model.JOBS;
import org.spring.newidea.Model.User;
import org.spring.newidea.Repositary.JOBSREPO;
import org.spring.newidea.Repositary.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JOBSService {
    @Autowired
    JOBSREPO jobrepo;
    @Autowired
    Userrepo Userrepo;
    public ResponseEntity<String> addjob(String UID,JOBS job){
        if(Userrepo.existsById(UID))
        {
            User user=Userrepo.findById(UID).orElse(null);
            job.setUser(user);
            jobrepo.save(job);
return ResponseEntity.ok("Job added successfully");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went wrong");

    }

    public List<JOBS> getallJOBS(String name, String username) {
        return jobrepo.findByUserIdAndNameContainingIgnoreCaseOrUserIdAndSkillsContainingIgnoreCaseOrUserIdAndSpecsContainingIgnoreCaseOrUserIdAndTitleContainingIgnoreCase(
                username, name,username, name,username, name,username, name
        );
    }
    public List<JOBS> getallJOBSByID(String ID) {
        return jobrepo.findByUserId(ID);
    }
    public void DeleteALLBYname(String ID,String name)
    {
         jobrepo.deleteByUserIdAndTitleContainingOrNameContainingOrSkillsContainingOrSpecsContaining(ID,name,name,name,name);
    }
    public  void DELETEBYID(String ID,String UID)
    {
        jobrepo.deleteByUserIdAndId(UID,ID);
    }
    public JOBS getOne(String UID,String ID)
    {
        return jobrepo.findByUserIdAndId(UID,ID);
    }

    public void UpdateOne(String ID,JOBS JOB)
    {

        if(jobrepo.existsById(ID))
        jobrepo.save(JOB);
    }
    public void CLEARALL(String ID)
    {


            jobrepo.deleteByUserId(ID);
    }

//    public ResponseEntity<String> addjob(
//            String UID,
//            JOBS job
//    ) {
//        try {
//            if (Userrepo.existsById(UID)) {
//                User user = Userrepo.findById(UID).orElse(null);
//                if (user == null) {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                            .body("User not found");
//                }
//                job.setUser(user);
//                jobrepo.save(job);
//                return ResponseEntity.ok("Job added successfully");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("User does not exist");
//            }
//        } catch (Exception e) {
//            // Log the exception
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred while adding the job");
//        }
//    }


}

package org.spring.newidea;


import org.spring.newidea.Model.*;
import org.spring.newidea.Repositary.Inviterepo;
import org.spring.newidea.Repositary.Tasksession;
import org.spring.newidea.Services.TRELLOSERVICE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class TrelloController {
    @Autowired
    private TRELLOSERVICE TLS;

    @Autowired
    private Inviterepo inviterepo;

    @Autowired
    private Tasksession tasksession;



    @GetMapping("/ALLTASKS/{SID}")
    @CrossOrigin
    public List<TRELLOM> getAllTasks(@PathVariable("SID") String SID) {
        return TLS.getALLTRELLOS(SID);
    }

    @PostMapping("/ADDTASKS/{UID}/{SID}")
    @CrossOrigin
    public ResponseEntity<String> addTask(@PathVariable("UID") String UID,@PathVariable("SID") String SID, @RequestBody TRELLOM TRELLOM) {
        return TLS.addTRELLO(SID,UID, TRELLOM);
    }


    @PutMapping("/UPDATETASKS/{UID}/{SID}/{ID}")
    @CrossOrigin
    public ResponseEntity<String> UpdateTask(@PathVariable("UID") String UID,@PathVariable("SID") String SID,@PathVariable("ID") String ID, @RequestBody TRELLOM Tre) {
    return TLS.UpdateTRELLO(SID,UID,ID, Tre);
    }


    @DeleteMapping("/DELETEONETASKS/{UID}/{ID}")
    @CrossOrigin
    public void DELETETASK(@PathVariable("UID") String UID,@PathVariable("ID") String ID) {
        TLS.DELETERELLO(UID,ID);
    }

    @PostMapping("/addtask/{UID}")
    @CrossOrigin
    public ResponseEntity<String> addsessiontask(@PathVariable("UID") String UID,@RequestBody Sessiontask addtask) {

return TLS.addsessiontask(UID,addtask);


    }



    @GetMapping("/getallsessions/{UID}")
    @CrossOrigin
    public List<Sessiontask>allsessions(@PathVariable("UID") String UID) {

       return TLS.findsessions(UID);


    }
    @DeleteMapping("/deletesessions/{UID}/{SID}")
    @CrossOrigin
    public void deletesessions(@PathVariable("UID") String UID,@PathVariable("SID") String SID) {

        TLS.DELETEALLSESSIONS(UID,SID);


    }



    @GetMapping("/ALLUSERS/{username}/{UID}")
    @CrossOrigin
    public List<User> getallusers(@PathVariable("username") String username, @PathVariable("UID") String UID) {

        return TLS.allusers(username,UID);


    }

    @PostMapping("/invitesome/{UID}/{SID}")
    @CrossOrigin
    public  ResponseEntity<String> invitesome(@PathVariable("UID") String UID, @PathVariable("SID") String SID,@RequestBody Invites invitedata) {
        return TLS.invite(UID,SID,invitedata);
    }


@GetMapping("/getone/{UID}/{SID}")
    @CrossOrigin
    public boolean checkinvites(@PathVariable("UID") String UID, @PathVariable("SID") String SID) {
        return  inviterepo.existsAllByUserIdAndSessioninviteId(UID,SID);

}

@GetMapping("/allinvites/{UID}")
    @CrossOrigin
    public List<Sessiontask> allinvites(@PathVariable("UID") String UID) {

        return TLS.getSessionsinvites(UID);
}

@DeleteMapping("/quitsession/{SID}/{UID}")
    @CrossOrigin
    public void quitsession(@PathVariable("SID") String SID,@PathVariable("UID") String UID)
{
    inviterepo.deleteByUserIdAndSessioninviteId(UID,SID);
}

@GetMapping("/owner/{SID}/{UID}")
    @CrossOrigin
    public Boolean checkproperty(@PathVariable("SID") String SID,@PathVariable("UID") String UID)
{
    return tasksession.existsByUserIdAndId(UID,SID);
}

    @GetMapping("/invitedusers/{SID}")
    @CrossOrigin
    public List<Invites> invitedusers(@PathVariable("SID") String SID) {

        return TLS.getInvites(SID);
    }
    @DeleteMapping("/deleteinvitationuser/{UID}/{SID}")
    @CrossOrigin
    public void deleteoneinvitation(@PathVariable("UID") String UID,@PathVariable("SID") String SID)
    {
        inviterepo.deleteByUserIdAndSessioninviteId(UID,SID);
    }

    @PutMapping("/updatingroles/{UID}/{SID}/{ID}")
    @CrossOrigin
    public ResponseEntity<String> updateone(@PathVariable("UID") String UID, @PathVariable("SID") String SID, @PathVariable("ID") String ID, @RequestBody Invites invit)
    {
       return TLS.Updateinvites(SID,UID,ID,invit);
    }

    @GetMapping("/checkpromotion/{UID}/{SID}")
    @CrossOrigin
    public Boolean checkpromotion(@PathVariable("UID") String UID,@PathVariable("SID") String SID)
    {
        Invites invite=inviterepo.findByUserIdAndSessioninviteId(UID,SID);
        return invite.getPromoted();
    }


}

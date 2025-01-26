package org.spring.newidea.Services;


import org.spring.newidea.Model.*;
import org.spring.newidea.Repositary.Inviterepo;
import org.spring.newidea.Repositary.TRELLO;
import org.spring.newidea.Repositary.Tasksession;
import org.spring.newidea.Repositary.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TRELLOSERVICE {


    @Autowired
    private Userrepo userrepo;

    @Autowired
    private TRELLO TrelloRepo;
    @Autowired
    private Tasksession taskrepo;
    @Autowired
    private Inviterepo inviterepo;


    public ResponseEntity<String> addTRELLO(String SID,String UID, TRELLOM tre){
        if(userrepo.existsById(UID))
        {

            Sessiontask session=taskrepo.findById(SID).orElse(null);
            User user=userrepo.findById(UID).orElse(null);
            tre.setUser(user);
            tre.setTask(session);
            TrelloRepo.save(tre);
            return ResponseEntity.ok("TASK added successfully");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went wrong");

    }

    public void DELETERELLO(String UID,String ID){
       TrelloRepo.deleteByUserIdAndId(UID,ID);

    }

    public ResponseEntity<String> UpdateTRELLO(String SID,String UID,String ID,TRELLOM tre){
        if(TrelloRepo.existsById(ID) && userrepo.existsById(UID))
        {
User user=userrepo.findById(UID).orElse(null);
Sessiontask session=taskrepo.findById(SID).orElse(null);
tre.setUser(user);
            tre.setTask(session);
            TrelloRepo.save(tre);
            return ResponseEntity.ok("TASK Updated successfully");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went wrong");

    }

    public List<TRELLOM> getALLTRELLOS(String SID)
    {
        return TrelloRepo.findByTaskId(SID);
    }

    public ResponseEntity<String> addsessiontask(String UID, Sessiontask addtask){
        if(userrepo.existsById(UID))
        {
            User user=userrepo.findById(UID).orElse(null);
            addtask.setUser(user);
            taskrepo.save(addtask);
            return ResponseEntity.ok("TASK added successfully");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went wrong");

    }

    public List<Sessiontask> findsessions(String UID)
    {
       return taskrepo.findByUserId(UID);
    }

    public void DELETEALLSESSIONS(String UID,String SID)
    {
        taskrepo.deleteByUserIdAndId(UID,SID);
        TrelloRepo.deleteByUserIdAndTaskId(UID,SID);
        inviterepo.deleteBySessioninviteId(SID);
    }

    public List<User> allusers(String username,String UID)
    {


        return userrepo.findByUsernameContainingIgnoreCaseAndIdNot(username,UID);
    }

    public ResponseEntity<String> invite(String UID, String SID, Invites invitedata){

        if(userrepo.existsById(UID))
        {

            Sessiontask session=taskrepo.findById(SID).orElse(null);
            User user=userrepo.findById(UID).orElse(null);
           invitedata.setUser(user);
            invitedata.setSessioninvite(session);
            inviterepo.save(invitedata);
            return ResponseEntity.ok("User invited successfully");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went wrong");

    }

    public List<Sessiontask> getSessionsinvites(String UID)
    {
        List<Invites> invitelist=  inviterepo.findByUserId(UID);
        List<Sessiontask> sessiontask=new ArrayList<>();
        for(Invites invite:invitelist)
        {
            sessiontask.add(invite.getSessioninvite());
        }
        return sessiontask;
    }


    public List<Invites> getInvites(String SID)
    {
        List<Invites> invitelist=  inviterepo.findBySessioninviteId(SID);


        return invitelist;
    }


    public ResponseEntity<String> Updateinvites(String SID, String UID, String ID, Invites invit){
        if(inviterepo.existsById(ID) && userrepo.existsById(UID))
        {
            User user=userrepo.findById(UID).orElse(null);
            Sessiontask session=taskrepo.findById(SID).orElse(null);
            invit.setUser(user);
            invit.setSessioninvite(session);
invit.setId(ID);
            inviterepo.save(invit);
            return ResponseEntity.ok("User's Role Has Changed successfully");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went wrong");

    }


}

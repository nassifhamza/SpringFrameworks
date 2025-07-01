package org.spring.anotherinstance.SecureApp;

import org.spring.anotherinstance.Model.User;
import org.spring.anotherinstance.Repositary.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class Myuserdetails implements UserDetailsService {


    @Autowired
private  Userrepo userrepo;


    @Override
    public UserDetails loadUserByUsername(String text) throws UsernameNotFoundException {
        User user=userrepo.findByUsernameOrEmail(text,text);
        if(user==null)
            throw new UsernameNotFoundException("User doesnt exist at all");
        return new UserPrincipal(user);
    }
}

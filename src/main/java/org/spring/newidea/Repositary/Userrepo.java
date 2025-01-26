package org.spring.newidea.Repositary;

import org.spring.newidea.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Userrepo extends MongoRepository<User, String> {


        Boolean existsByUsername(String username);
        Boolean existsByEmail(String email);
        User findByUsernameOrEmail(String username,String email);
        List<User> findByUsernameContainingIgnoreCaseAndIdNot(String username,String UID);
        User findByEmail(String email);



}

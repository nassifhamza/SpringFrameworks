package org.spring.newidea.Repositary;

import org.spring.newidea.Model.Sessiontask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Tasksession extends MongoRepository<Sessiontask,String> {
    @Transactional
    void deleteByUserIdAndId(String userId, String id);
   Boolean existsByUserIdAndId(String userId, String id);
    List<Sessiontask> findByUserId(String name);
}

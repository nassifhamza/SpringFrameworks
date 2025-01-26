package org.spring.newidea.Repositary;

import org.spring.newidea.Model.JOBS;
import org.spring.newidea.Model.TRELLOM;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TRELLO extends MongoRepository<TRELLOM,String> {

    @Transactional
    void deleteByUserIdAndId(String userId, String id);

    @Transactional
    void deleteByUserIdAndTaskId(String userId,String SID);

    List<TRELLOM> findByTaskId(String ID);

}

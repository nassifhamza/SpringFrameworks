package org.spring.anotherinstance.Repositary;

import org.spring.anotherinstance.Model.VIDEOS;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VIDEOSREPO extends MongoRepository<VIDEOS,String> {

    List<VIDEOS> findByUserId(String userId);
    List<VIDEOS> findByIdAndUserId(String ID, String userId);
    @Transactional
    void deleteByUserIdAndId(String userId, String ID);
}

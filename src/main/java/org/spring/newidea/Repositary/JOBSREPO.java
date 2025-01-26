package org.spring.newidea.Repositary;

import org.spring.newidea.Model.JOBS;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JOBSREPO extends MongoRepository<JOBS, String> {
    List<JOBS> findByUserIdAndNameContainingIgnoreCaseOrUserIdAndSkillsContainingIgnoreCaseOrUserIdAndSpecsContainingIgnoreCaseOrUserIdAndTitleContainingIgnoreCase(
            String userId1, String name, String userId2,String skills, String userId3,String specs, String userId4,String title);
    List<JOBS> findByTitleContainingOrNameContainingOrSkillsContainingOrSpecsContaining(String name, String name1, String name2,String name3);
    List<JOBS> findByUserId(String name);
    JOBS findByUserIdAndId(String userId,String ID);
    @Transactional
    void deleteByUserIdAndId(String userId,String ID);
    @Transactional
    void deleteByUserId(String userId);
    @Transactional
    void deleteByUserIdAndTitleContainingOrNameContainingOrSkillsContainingOrSpecsContaining(String userId,String title, String name, String skills, String spes);
}

package org.spring.anotherinstance.Repositary;

import org.spring.anotherinstance.Model.Invites;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Inviterepo extends MongoRepository<Invites, String> {
    boolean existsAllByUserIdAndSessioninviteId(String userId, String sessioninviteId);
    Invites findByUserIdAndSessioninviteId(String userId, String sessioninviteId);
    List<Invites> findByUserId(String UID);
    @Transactional
    void deleteBySessioninviteId(String sessioninviteId);
List<Invites> findBySessioninviteId(String sessioninviteId);
    @Transactional
    void deleteByUserIdAndSessioninviteId(String UID, String sessioninviteId);


}

package org.seydaliev.jsonwebtokenauth.repository;

import org.seydaliev.jsonwebtokenauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}

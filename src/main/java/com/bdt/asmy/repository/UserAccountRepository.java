package com.bdt.asmy.repository;

import com.bdt.asmy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}

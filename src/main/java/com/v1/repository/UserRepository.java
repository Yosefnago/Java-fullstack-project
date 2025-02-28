package com.v1.repository;


import com.v1.entity.AccountantUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AccountantUser,Long> {

    AccountantUser findByUsername(String username);
}

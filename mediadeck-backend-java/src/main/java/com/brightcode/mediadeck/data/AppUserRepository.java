package com.brightcode.mediadeck.data;

import com.brightcode.mediadeck.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByVerificationCode(String verificationCode);

    @Query(value ="SELECT * FROM app_user WHERE username LIKE %:username%",nativeQuery = true)
    List<AppUser> findByUsernameLike(String username);

}


package com.example.authserver.general.repository;

import com.example.authserver.general.entity.Oauth2ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Auther: 长安
 */
public interface Oauth2ApplicationUserRepository extends JpaRepository<Oauth2ApplicationUser, Integer> {

    @Query("from Oauth2ApplicationUser where userName = :un")
    Oauth2ApplicationUser findByUserName(@Param("un") String userName);

    Oauth2ApplicationUser findByMobile(String mobile);
}

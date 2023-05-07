package com.example.authserver.general.repository;

import com.example.authserver.general.entity.Oauth2RegisteredClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: 长安
 */
public interface Oauth2RegisteredClientEntityRepository extends JpaRepository<Oauth2RegisteredClientEntity, String> {


}

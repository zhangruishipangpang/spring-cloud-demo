package com.example.authserver.general.repository;

import com.example.authserver.general.entity.UserInformationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserInformationMessageRepository extends JpaRepository<UserInformationMessage, Integer>, JpaSpecificationExecutor<UserInformationMessage> {

}
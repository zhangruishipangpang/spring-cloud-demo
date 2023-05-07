package com.example.authserver.general.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @Auther: 长安
 */
@Data
@Entity
@Table(name = "OAUTH2_APPLICATION_USER")
public class Oauth2ApplicationUser {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private Integer id;

    @Basic
    @Column(name = "USER_NAME")
    private String userName;

    @Basic
    @Column(name = "PASSWORD")
    private String password;
}

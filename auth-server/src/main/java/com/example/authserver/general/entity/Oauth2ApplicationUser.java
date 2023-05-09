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
    @Column(name = "USER_NAME", unique = true, length = 128)
    private String userName;

    @Basic
    @Column(name = "PASSWORD", length = 128)
    private String password;

    @Basic
    @Column(name = "uid", unique = true, length = 11)
    private Integer uid;
}

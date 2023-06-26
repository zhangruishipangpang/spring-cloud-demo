package com.example.authserver.general.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Auther: 长安
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "OAUTH2_APPLICATION_USER")
public class Oauth2ApplicationUser implements Serializable {
    private static final long serialVersionUID = 1L;

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
    /**
     * 手机号 e.g.全局唯一
     */
    @Column(name = "mobile", nullable = false)
    private String mobile;
}

package com.example.authserver.general.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "user_information_message")
public class UserInformationMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "head_portrait")
    private String headPortrait;

    @Column(name = "id_card")
    private String idCard;

    @Column(name = "sex")
    private String sex;
    /**
     * 用户信息
     */
    @Column(name = "nick", nullable = false)
    private String nick;

}

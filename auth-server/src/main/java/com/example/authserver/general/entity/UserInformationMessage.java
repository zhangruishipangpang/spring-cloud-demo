package com.example.authserver.general.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @Auther: 长安
 */
@Data
@Entity
@Table(name = "USER_INFORMATION_MESSAGE")
public class UserInformationMessage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", length = 11)
    private Integer id;

    @Basic
    @Column(name = "email", length = 64)
    private String email;

    @Basic
    @Column(name = "id_card", unique = true, length = 32)
    private String idCard;

    @Basic
    @Column(name = "sex", length = 3)
    private String sex;

    @Column(name = "face_id", length = 128)
    private String faceId;
}

package com.fleet.status.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TUsers")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "intUserId")
    private Long userId;

    @Column(name = "strUsername")
    private String username;

    @Column(name = "strPassword")
    private String password;

    @Column(name = "strRoles")
    private String roles;
}

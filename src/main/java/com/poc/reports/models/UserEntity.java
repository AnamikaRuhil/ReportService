package com.poc.reports.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "users")

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String password;
    @ManyToOne
    private RoleEntity roleEntity;
}

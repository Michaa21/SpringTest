package com.example.springtest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "profiles", schema = "spring_test")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;

    @OneToOne
    @JoinColumn(name = "user_id",
            nullable = false,
            unique = true)
    private User user;
}

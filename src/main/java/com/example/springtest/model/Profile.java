package com.example.springtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profiles", schema = "spring_test")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String firstName;
    private String lastName;

    @OneToOne
    @JoinColumn(name = "user_id",
            nullable = false,
            unique = true)
    @JsonIgnore
    private User user;
}

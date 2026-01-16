    package com.example.springtest.model;

    import com.fasterxml.jackson.annotation.JsonBackReference;
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
        private Long id;

        private String firstName;
        private String lastName;

        @OneToOne
        @JoinColumn(name = "user_id", nullable = false, unique = true)
        @JsonBackReference
        private User user;

        public Profile(){}

        public Profile(String firstName, String lastName, User user) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.user = user;
        }
    }

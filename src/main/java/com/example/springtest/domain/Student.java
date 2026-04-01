package com.example.springtest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "students", schema = "spring_test")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_lesson",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> lessons = new HashSet<>();

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.getStudents().add(this);
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
        lesson.getStudents().remove(this);
    }

    public void setLessons(Set<Lesson> lessons) {
        for (Lesson lesson : new HashSet<>(this.lessons)) {
            removeLesson(lesson);
        }
        for (Lesson lesson : lessons) {
            addLesson(lesson);
        }
    }
}


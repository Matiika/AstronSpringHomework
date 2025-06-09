package com.AstronSpringHomework.App.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;


    @Entity
    @Table(name = "users")
    @EntityListeners(AuditingEntityListener.class)
    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public class User {
        @Id
        @GeneratedValue(strategy = IDENTITY)
        @EqualsAndHashCode.Include
        private Long id;

        private String name;

        @Column(unique = true)
        private String email;

        private Integer age;

        @CreatedDate
        private LocalDateTime createdAt;

    }

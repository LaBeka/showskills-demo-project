package edu.example.demoproject.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "role_entities")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name")
    private String name;
}

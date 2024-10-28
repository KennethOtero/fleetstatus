package com.fleet.status.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "TTypes")
public class Type {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "intTypeId")
    private Long typeId;

    @Column(name = "strType")
    private String typeName;

    @OneToMany(mappedBy = "type")
    private Set<Aircraft> aircraft;
}

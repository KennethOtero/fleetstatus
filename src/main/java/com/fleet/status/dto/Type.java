package com.fleet.status.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Set<Aircraft> aircraft;
}

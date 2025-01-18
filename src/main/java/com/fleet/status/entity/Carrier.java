package com.fleet.status.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "TCarriers")
public class Carrier {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "intCarrierId")
    private Long carrierId;

    @Column(name = "strCarrier")
    private String carrierName;

    @OneToMany(mappedBy = "carrier")
    @JsonIgnore
    private Set<Aircraft> aircraft;
}

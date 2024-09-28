package com.fleet.status.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "TCarriers")
public class Carrier {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "intCarrierId")
    private long carrierId;

    @Column(name = "strCarrier")
    private String carrierName;

    @OneToMany(mappedBy = "carrier")
    private Set<Aircraft> aircraft;
}

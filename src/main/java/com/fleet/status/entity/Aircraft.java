package com.fleet.status.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TAircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "intAircraftId")
    private Long aircraftId;

    @Column(name = "strTailNumber")
    private String tailNumber;

    @ManyToOne
    @JoinColumn(name = "intCarrierId", referencedColumnName = "intCarrierId")
    private Carrier carrier;

    @ManyToOne
    @JoinColumn(name = "intTypeId", referencedColumnName = "intTypeId")
    private Type type;
}

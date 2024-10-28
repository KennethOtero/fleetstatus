package com.fleet.status.dto;
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
    @JoinColumn(name = "intTypeId", referencedColumnName = "intTypeId")
    private Type type;
}

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

    @Column(name = "strStatus")
    private String status;
    @Column(name = "strTailNumber")
    private String tailNumber;
    @Column(name = "strReason")
    private String reason;
    @Column(name = "strNextUpdate")
    private String nextUpdate;
    @Column(name = "strRemark")
    private String remark;
    @Column(name = "blnBackInService")
    private Integer backInService;
    @Column(name = "intDownTime")
    private Integer downTime;

    @ManyToOne
    @JoinColumn(name = "intCarrierId", referencedColumnName = "intCarrierId")
    private Carrier carrier;
}

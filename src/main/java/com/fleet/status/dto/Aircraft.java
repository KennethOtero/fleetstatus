package com.fleet.status.dto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TAircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "intAircraftId")
    private long aircraftId;

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
    private int backInService;
    @Column(name = "intDownTime")
    private int downTime;

    @ManyToOne
    @JoinColumn(name = "carrierId", referencedColumnName = "intCarrierId")
    private Carrier carrier;
}

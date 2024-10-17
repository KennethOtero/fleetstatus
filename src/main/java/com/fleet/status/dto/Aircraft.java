package com.fleet.status.dto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

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
    @Column(name = "strReason")
    private String reason;
    @Column(name = "strNextUpdate")
    @Getter(AccessLevel.NONE)
    private String nextUpdate;
    @Column(name = "strRemark")
    private String remark;
    @Column(name = "blnBackInService")
    private Integer backInService;
    @Column(name = "dtmStartTime")
    private String startTime;
    @Column(name = "dtmEndTime")
    private String endTime;

    @ManyToOne
    @JoinColumn(name = "intCarrierId", referencedColumnName = "intCarrierId")
    private Carrier carrier;


    public String getNextUpdate() {
        // Preserve old formatting of nextUpdate
        return nextUpdate.substring(10, 16) + "z";
    }
}

package com.fleet.status.dto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "TEvents")
public class Event {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "intEventId")
    private Long eventId;

    @Column(name = "dtmNextUpdate")
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
    @JoinColumn(name = "intAircraftId", referencedColumnName = "intAircraftId")
    private Aircraft aircraft;

    @ManyToMany
    @ToString.Exclude //avoid stackoverflow error when use toString method generated by @Data, they print each other between many to many
    @JoinTable(
            name = "TEventReasons",
            joinColumns = @JoinColumn(name = "intEventId"),
            inverseJoinColumns = @JoinColumn(name = "intReasonId")
    )
    private List<Reason> reason;

    @Transient
    private String reasonString;

    public String getNextUpdate() {
        // Preserve old formatting of nextUpdate
        if (nextUpdate != null) {
            return nextUpdate.substring(10, 16) + "z";
        }
        return null;
    }
}

package com.fleet.status.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "TEvents")
public class Event {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "intEventId")
    private Long eventId;

    @Column(name = "dtmNextUpdate")
    private Instant nextUpdate;
    @Column(name = "strRemark")
    @CsvBindByName(column = "Remark")
    private String remark;
    @Column(name = "blnBackInService")
    private Integer backInService;
    @Column(name = "dtmStartTime")
    @CsvBindByName(column = "Event Date")
    @CsvDate("MM/dd/yyyy")
    private Instant startTime;
    @Column(name = "dtmEndTime")
    private Instant endTime;

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
    @CsvBindByName(column = "Reason")
    @Getter(AccessLevel.NONE)
    private String reasonString;

    @Transient
    @CsvBindByName(column = "Downtime")
    @Getter(AccessLevel.NONE)
    private String downTime;

    @Transient
    @CsvBindByName(column = "Tail #")
    private String csvTailNumber;

    public String getDownTime() {
        if (getEndTime() == null || getStartTime() == null) {
            return "Down time is not available";
        }
        Duration downtime = Duration.between(startTime, endTime);
        return downtime.toDaysPart() + "d " + downtime.toHoursPart() + "h " + downtime.toMinutesPart() + "m";
    }

    public String getReasonString() {
        if (reason != null && !reason.isEmpty()) {
            return reason.stream()
                    .map(Reason::getReason)
                    .collect(Collectors.joining(", "));
        }
        return "N/A";
    }
}

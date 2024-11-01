package com.fleet.status.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "TReasons")
public class Reason {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "intReasonId")
    private Long reasonId;

    @Column(name = "strReason")
    private String reason;

    @ManyToMany
    @ToString.Exclude
    @JsonIgnore
    @JoinTable(
            name = "TEventReasons", // 中间表的表名
            joinColumns = @JoinColumn(name = "intReasonId"),
            inverseJoinColumns = @JoinColumn(name = "intEventId")
    )
    private List<Event> events;

}

package com.fleet.status.dto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TReason")
public class Reason {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "intReasonId")
    private Long reasonId;

    @Column(name = "strReason")
    private String reason;

}

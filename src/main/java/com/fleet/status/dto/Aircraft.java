package com.fleet.status.dto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int aircraftId;
    private String status;
    private String tailNumber;
    private String reason;
    private String nextUpdate;
    private String remark;
    private boolean backInService;
    private int downTime;

}

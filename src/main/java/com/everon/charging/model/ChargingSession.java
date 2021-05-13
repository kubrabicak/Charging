package com.everon.charging.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Charging Session
 *
 * */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargingSession {
    private UUID id;
    private String stationId;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private StatusEnum status;

    /**
     * Creates a new Charging Station
     */
    public ChargingSession(String stationId){
        this.id = UUID.randomUUID();
        this.status = StatusEnum.IN_PROGRESS;
        this.stationId = stationId;
        this.startedAt = LocalDateTime.now();
    }

}

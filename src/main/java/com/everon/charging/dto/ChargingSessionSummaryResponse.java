package com.everon.charging.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Charging Session Summary
 *
 * */

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargingSessionSummaryResponse {
    private long totalCount;
    private long startedCount;
    private long stoppedCount;
}

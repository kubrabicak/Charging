package com.everon.charging.service;

import com.everon.charging.dto.ChargingSessionSummaryResponse;
import com.everon.charging.model.ChargingSession;
import com.everon.charging.model.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/**
 * Charging Session Service
 *
 * */

@Service
@RequiredArgsConstructor
public class ChargingSessionsService {

    Map<UUID, ChargingSession> chargingSessionConcurrentMaps = new ConcurrentHashMap();

    /**
     * Creates a new charging session.
     *
     * @param chargingStationId Charging station Id
     * @return {@code ChargingSession}
     */
    public ChargingSession createChargingSession(String chargingStationId) {
        ChargingSession chargingSession = new ChargingSession(chargingStationId);
        chargingSessionConcurrentMaps.put(chargingSession.getId(), chargingSession);
        return chargingSession;
    }

    /**
     * Stops charging session.
     *
     * @param id Charging session UUID
     * @return {@code ChargingSession}
     */
    public ChargingSession stopChargingSession(UUID id) {
        ChargingSession chargingSession = chargingSessionConcurrentMaps.get(id);
        if(chargingSession == null){
              throw new IllegalArgumentException("No Charging session with id: " + id);
        }
        chargingSession.setStoppedAt(LocalDateTime.now());
        chargingSession.setStatus(StatusEnum.FINISHED);
        return chargingSession;
    }

    /**
     * List all charging sessions.
     *
     * @return {@code List<ChargingSession> }
     */
    public List<ChargingSession> listChargingSessions() {
        return new ArrayList<>(chargingSessionConcurrentMaps.values());
    }

    /**
     * Get charging sessions summary.
     *
     * @return {@code ChargingSessionSummaryResponse }
     */
    public ChargingSessionSummaryResponse chargingSessionsSummary() {
        Map map = chargingSessionConcurrentMaps.values().stream().filter(chargingSession -> chargingSession.getStartedAt().isAfter(LocalDateTime.now().minusHours(1L))).
                collect(Collectors.groupingBy(chargingSession -> chargingSession.getStatus(), Collectors.counting()));
        long inProgressCount = 0;
        if(map.get(StatusEnum.IN_PROGRESS) != null){
            inProgressCount = (long) map.get(StatusEnum.IN_PROGRESS);
        }

        long finishedCount = 0;
        if(map.get(StatusEnum.FINISHED) != null){
            finishedCount = (long) map.get(StatusEnum.FINISHED);
        }

        ChargingSessionSummaryResponse chargingSessionSummaryResponse = ChargingSessionSummaryResponse.builder()
                .startedCount(inProgressCount)
                .stoppedCount(finishedCount)
                .totalCount(inProgressCount + finishedCount)
                .build();
        return chargingSessionSummaryResponse;
    }

/**
 * Reset concurrent Hash Map
 *
 */

 public void resetMap(){
        chargingSessionConcurrentMaps = new ConcurrentHashMap<>();
    }
}

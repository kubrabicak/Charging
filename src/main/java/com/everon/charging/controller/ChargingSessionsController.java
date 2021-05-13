package com.everon.charging.controller;

import com.everon.charging.dto.ChargingSessionSummaryResponse;
import com.everon.charging.model.ChargingSession;
import com.everon.charging.service.ChargingSessionsService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Charging Session Rest
 *
 * */

@RestController
public class ChargingSessionsController {
    @Autowired
    ChargingSessionsService chargingSessionsService;

    /**
     * Create charging session
     *
     * @param requestBody Station Id that the charging session ocuurs
     * @return charging session response entity
     */

    @PostMapping("/chargingSessions")
    public ResponseEntity<ChargingSession> createChargingSession(@RequestBody String requestBody) {
        JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        ChargingSession chargingSession = chargingSessionsService.createChargingSession(jsonObject.get("stationId").getAsString());
        return ok().body(chargingSession);
    }

    /**
     * Stop charging session
     *
     * @param id charging session id
     * @return charging session response entity
     */

    @PutMapping("/chargingSessions/{id}")
    public ResponseEntity<ChargingSession> stopChargingSession(@PathVariable String id) {
        ChargingSession chargingSession = chargingSessionsService.stopChargingSession(UUID. fromString(id));
        return ok().body(chargingSession);
    }

    /**
     * List charging session
     *
     * @return list of charging session response entity
     */

    @GetMapping("/chargingSessions")
    public ResponseEntity<List<ChargingSession>> listChargingSessions() {
        List<ChargingSession> chargingSessionList = chargingSessionsService.listChargingSessions();
        return ok().body(chargingSessionList);
    }

    /**
     * List charging session
     *
     * @return list of charging session response entity
     */

    @GetMapping("/chargingSessions/summary")
    public ResponseEntity<ChargingSessionSummaryResponse> chargingSessionsSummary() {
        ChargingSessionSummaryResponse chargingSessionSummaryResponse = chargingSessionsService.chargingSessionsSummary();
        return ok().body(chargingSessionSummaryResponse);
    }
}

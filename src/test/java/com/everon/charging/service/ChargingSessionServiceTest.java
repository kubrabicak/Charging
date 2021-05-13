package com.everon.charging.service;

import com.everon.charging.dto.ChargingSessionSummaryResponse;
import com.everon.charging.model.ChargingSession;
import com.everon.charging.model.StatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ChargingSessionServiceTest {

    @Autowired
    private ChargingSessionsService chargingSessionsService;

    @Test
    void createChargingStation() {
        ChargingSession actual = chargingSessionsService.createChargingSession("AAA-12345");
        assertEquals(actual.getStatus(), StatusEnum.IN_PROGRESS);
        assertEquals(actual.getStationId(), "AAA-12345");
        assertNotNull(actual.getStartedAt());
        assertNull(actual.getStoppedAt());
    }

    @Test
    void chargingSessionStopped() {
        ChargingSession actual = chargingSessionsService.createChargingSession("AAA-12345");
        ChargingSession updated = chargingSessionsService.stopChargingSession(actual.getId());
        assertEquals(updated.getStatus(), StatusEnum.FINISHED);
        assertNotNull(updated.getStoppedAt());
        assertEquals(updated.getStationId(), "AAA-12345");
        assertNotNull(updated.getStartedAt());
        assertThrows(IllegalArgumentException.class, () -> chargingSessionsService.stopChargingSession(UUID.randomUUID()));
    }

    @Test
    void listChargingSessions() {
        chargingSessionsService.resetMap();
        List <ChargingSession> chargingSessions = chargingSessionsService.listChargingSessions();
        assertEquals(chargingSessions.size(), 0);
        chargingSessionsService.createChargingSession("st 1");
        chargingSessions = chargingSessionsService.listChargingSessions();
        assertEquals(chargingSessions.size(), 1);
    }

    @Test
    void chargingSessionsSummary() {
        chargingSessionsService.resetMap();
        chargingSessionsService.createChargingSession("AAA-12345");
        ChargingSession actual = chargingSessionsService.createChargingSession("AAA-12345");
        ChargingSession updated = chargingSessionsService.stopChargingSession(actual.getId());
        ChargingSessionSummaryResponse summary = chargingSessionsService.chargingSessionsSummary();
        assertEquals(summary.getTotalCount(), summary.getStartedCount() + summary.getStoppedCount());
        assertEquals(summary.getStartedCount(), 1);
        assertEquals(summary.getStoppedCount(), 1);
    }

    }
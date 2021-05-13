package com.everon.charging.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class ChargingSessionTest {

    private ChargingSession chargingStation;

    @BeforeEach
    public void init() {
        chargingStation = new ChargingSession("ABC-12345");
    }

    @Test
    void chargingSessionCreated() {
        assertEquals(chargingStation.getStatus(), StatusEnum.IN_PROGRESS);
        assertEquals(chargingStation.getStationId(), "ABC-12345");
        assertNotNull(chargingStation.getStartedAt());
        assertNull(chargingStation.getStoppedAt());
    }

}

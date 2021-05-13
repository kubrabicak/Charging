package com.everon.charging.controller;

import com.everon.charging.model.ChargingSession;
import com.everon.charging.service.ChargingSessionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.annotation.PostConstruct;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class ChargingSessionControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ChargingSessionsService chargingSessionsService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void createChargingSession() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/chargingSessions").content("{\n" +
                "\"stationId\":\n" +
                "\"ABC-12345\"\n" +
                "}"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void stopChargingSession() throws Exception {
        ChargingSession chargingSession = chargingSessionsService.createChargingSession("AAA-22");
        mockMvc.perform(MockMvcRequestBuilders.put("/chargingSessions/{id}", chargingSession.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("FINISHED"));
    }

    @Test
    void listChargingSession() throws Exception {
        chargingSessionsService.resetMap();
        ChargingSession chargingSession = chargingSessionsService.createChargingSession("AAA-22");
        mockMvc.perform(MockMvcRequestBuilders.get("/chargingSessions"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].stationId").value("AAA-22"));
    }


    @Test
    void createSummary() throws Exception {
        chargingSessionsService.resetMap();
        chargingSessionsService.createChargingSession("00");
        chargingSessionsService.createChargingSession("AA");
        ChargingSession chargingSession = chargingSessionsService.createChargingSession("BB");
        chargingSessionsService.stopChargingSession(chargingSession.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/chargingSessions/summary"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalCount").value(3))
                .andExpect(jsonPath("$.startedCount").value(2))
                .andExpect(jsonPath("$.stoppedCount").value(1));
    }
}

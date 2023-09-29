package com.bol.kalaha.controller;

import com.bol.kalaha.KalahaApplication;
import com.bol.kalaha.model.dto.NewGameRequest;
import com.bol.kalaha.model.dto.PlayGameRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = KalahaApplication.class)
@AutoConfigureMockMvc
public class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenNoGameStarted_Then_throwException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/game/{game_id}", 1))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()) // You can change the status code as needed
            .andExpect(result -> {
                // Assert that the response contains the expected error message
                String content = result.getResponse().getContentAsString();
                assertNotNull(content);
                Assertions.assertTrue(content.contains("No such game"));
            });
    }

    //
    @Test
    public void whenGameStarted_Then_ReturnTheStatus() throws Exception {

        NewGameRequest gameRequest = new NewGameRequest();
        gameRequest.setPlayer1(1);
        gameRequest.setPlayer2(2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(gameRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/game/{game_id}", 1))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPlay() throws Exception {

        NewGameRequest gameRequest = new NewGameRequest();
        gameRequest.setPlayer1(1);
        gameRequest.setPlayer2(2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(gameRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());

        // Perform a PATCH request to "/api/game/play/{game_id}/{player_id}/{pit}"
        PlayGameRequest playGameRequest = new PlayGameRequest();
        playGameRequest.setPlayerId(1);
        playGameRequest.setPitId(1);
        playGameRequest.setGameId(1);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/game/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(playGameRequest))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(print());
    }

    protected static String toJsonString(final Object obj) {

        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

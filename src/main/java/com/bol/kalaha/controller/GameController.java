package com.bol.kalaha.controller;

import com.bol.kalaha.model.dto.GameBoardInfo;
import com.bol.kalaha.model.dto.NewGameRequest;
import com.bol.kalaha.model.dto.PlayGameRequest;
import com.bol.kalaha.service.GameServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameServiceImpl gameService;

    @GetMapping("/{gameId}")
    public GameBoardInfo getBoard(@PathVariable Integer gameId) {

        return gameService.getGame(gameId);
    }

    @PostMapping("/new")
    public GameBoardInfo startNewGame(@RequestBody NewGameRequest request) {

        Integer player1 = request.getPlayer1();
        Integer player2 = request.getPlayer2();
        return gameService.initGame(player1, player2);
    }

    @PatchMapping("/play")
    public GameBoardInfo play(@RequestBody PlayGameRequest request) {

        return gameService.play(request.getGameId(), request.getPlayerId(), request.getPitId());

    }

}
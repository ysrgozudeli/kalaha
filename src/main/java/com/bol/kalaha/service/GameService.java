package com.bol.kalaha.service;

import com.bol.kalaha.model.dto.GameBoardInfo;

public interface GameService {

    GameBoardInfo getGame(int gameId);

    GameBoardInfo initGame(Integer player1Id, Integer player2Id);

    GameBoardInfo play(Integer gameId, Integer playerId, int pitNumber);
}

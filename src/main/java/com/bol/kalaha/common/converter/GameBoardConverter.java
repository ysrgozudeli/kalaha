package com.bol.kalaha.common.converter;

import com.bol.kalaha.model.dto.GameBoardInfo;
import com.bol.kalaha.model.gameboard.GameBoard;
import java.util.stream.Collectors;

public class GameBoardConverter {

    public static GameBoardInfo toGameBoardInfo(GameBoard gameBoard) {

        GameBoardInfo gameBoardInfo = new GameBoardInfo();

        gameBoardInfo.setId(gameBoard.getId());

        gameBoardInfo.setPlayer1(PlayerConverter.toPlayerInfo(gameBoard.getPlayer1()));
        gameBoardInfo.setPlayer2(PlayerConverter.toPlayerInfo(gameBoard.getPlayer2()));

        gameBoardInfo.setHoles(gameBoard.getHoles());
        gameBoardInfo.setGameBoardStatus(gameBoard.getGameBoardStatus());

        final var winners = gameBoard.gameWinners.stream()
            .map(PlayerConverter::toPlayerInfo)
            .collect(Collectors.toList());

        gameBoardInfo.setWinners(winners);

        return gameBoardInfo;
    }
}
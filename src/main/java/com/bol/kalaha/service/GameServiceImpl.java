package com.bol.kalaha.service;

import com.bol.kalaha.common.converter.GameBoardConverter;
import com.bol.kalaha.common.dao.GameBoardDao;
import com.bol.kalaha.common.dao.PlayerDao;
import com.bol.kalaha.model.Player;
import com.bol.kalaha.model.dto.GameBoardInfo;
import com.bol.kalaha.model.gameboard.GameBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final PlayerDao    playerDao;
    private final GameBoardDao gameBoardDao;

    @Override
    public GameBoardInfo getGame(int gameId) {

        final var gameBoard = gameBoardDao.getGameBoardById(gameId);
        if (gameBoard.isEmpty()) {
            throw new IllegalArgumentException("No such game");
        }
        return GameBoardConverter.toGameBoardInfo(gameBoard.get());

    }

    @Override
    public GameBoardInfo initGame(Integer player1Id, Integer player2Id) {

        Player player1 = playerDao.getPlayerById(player1Id)
            .orElseThrow(() -> new IllegalArgumentException("no such player"));
        Player player2 = playerDao.getPlayerById(player2Id)
            .orElseThrow(() -> new IllegalArgumentException("no such player"));

        // check existing games:
        final var gameOpt = gameBoardDao.getGameBoardByPlayerIds(player1Id, player2Id);
        if (gameOpt.isPresent()) {
            final GameBoard gameBoard = gameOpt.get();
            gameBoard.initGame();
            gameBoardDao.updateGameBoard(gameBoard);
            return GameBoardConverter.toGameBoardInfo(gameBoard);
        }

        GameBoard gameBoard = new GameBoard();
        gameBoard.setPlayer1(player1);
        gameBoard.setPlayer2(player2);
        gameBoard.initGame();
        gameBoardDao.addGameBoard(gameBoard);
        return GameBoardConverter.toGameBoardInfo(gameBoard);
    }

    @Override
    public GameBoardInfo play(Integer gameId, Integer playerId, int pitNumber) {

        final var game = gameBoardDao.getGameBoardById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("no such game"));

        final var player = playerDao.getPlayerById(playerId)
            .orElseThrow(() -> new IllegalArgumentException("no such player"));

        if (!game.validateUserTurn(player)) {
            throw new IllegalArgumentException("Game is not eligible for playing or not your turn");
        }

        game.playTurn(pitNumber);
        return GameBoardConverter.toGameBoardInfo(game);
    }

}
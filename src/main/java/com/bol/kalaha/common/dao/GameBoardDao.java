package com.bol.kalaha.common.dao;

import com.bol.kalaha.model.gameboard.GameBoard;
import java.util.Optional;

public interface GameBoardDao {

    // Add a new game board
    GameBoard addGameBoard(GameBoard gameBoard);

    // Get a game board by ID
    Optional<GameBoard> getGameBoardById(int id);

    Optional<GameBoard> getGameBoardByPlayerIds(Integer player1, Integer player2);

    // Update a game board by ID
    boolean updateGameBoard(GameBoard updatedGameBoard);
}

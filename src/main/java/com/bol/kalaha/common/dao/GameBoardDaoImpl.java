package com.bol.kalaha.common.dao;

import com.bol.kalaha.model.gameboard.GameBoard;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class GameBoardDaoImpl implements GameBoardDao {

    private final Map<Integer, GameBoard> gameBoards = new HashMap<>();
    private       int                     nextId     = 1;

    // Add a new game board
    @Override
    public GameBoard addGameBoard(GameBoard gameBoard) {

        gameBoard.setId(nextId++);
        gameBoards.put(gameBoard.getId(), gameBoard);
        return gameBoard;
    }

    // Get a game board by ID
    @Override
    public Optional<GameBoard> getGameBoardById(int id) {

        return Optional.ofNullable(gameBoards.get(id));
    }

    @Override
    public Optional<GameBoard> getGameBoardByPlayerIds(Integer player1, Integer player2) {

        return gameBoards.values().stream()
            .filter(gameBoard ->
                ((gameBoard.getPlayer1().getId() == player1) &&
                    (gameBoard.getPlayer2().getId() == player2))
                    ||
                    ((gameBoard.getPlayer2().getId() == player1) &&
                        (gameBoard.getPlayer1().getId() == player2))
            )
            .findFirst();
    }

    // Update a game board by ID
    @Override
    public boolean updateGameBoard(GameBoard updatedGameBoard) {

        Integer id = updatedGameBoard.getId();
        if (gameBoards.containsKey(id)) {
            gameBoards.put(id, updatedGameBoard);
            return true;
        }
        return false;
    }


    // other functionalities if needed
}

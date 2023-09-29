package com.bol.kalaha.model.gameboard;

import com.bol.kalaha.model.Player;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameBoardTest {

    private GameBoard gameBoard;
    private Player    player1;
    private Player    player2;

    @BeforeEach
    public void setUp() {

        gameBoard = new GameBoard();
        player1 = new Player(1, "Player 1");
        player2 = new Player(2, "Player 2");
        gameBoard.setPlayer1(player1);
        gameBoard.setPlayer2(player2);
    }

    @Test
    public void testInitGame() {

        gameBoard.initGame();
        assertEquals(GameBoardStatus.PLAYER_1_TURN, gameBoard.getGameBoardStatus());
        int[] expectedHoles = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
        assertArrayEquals(expectedHoles, gameBoard.getHoles());
        assertEquals(List.of(), gameBoard.getGameWinners());
    }

    @Test
    public void testPlayGameValidMove() {

        gameBoard.initGame();
        gameBoard.playTurn(1); // Player 1's turn, valid move
        assertEquals(GameBoardStatus.PLAYER_2_TURN, gameBoard.getGameBoardStatus());
    }

    @Test
    public void testPlayGameInvalidMove() {

        gameBoard.initGame();
        assertThrows(IllegalArgumentException.class, () -> gameBoard.playTurn(7)); // Player 1's turn, invalid move
    }

    @Test
    public void testIsGameOverNotOver() {

        gameBoard.initGame();
        assertFalse(gameBoard.isGameOver());
    }

    @Test
    public void testIsGameOverPlayer1Wins() {

        gameBoard.initGame();
        int[] holes = new int[]{0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 4, 32};
        gameBoard.setHoles(holes);
        gameBoard.handleEndOfTurn();
        assertTrue(gameBoard.isGameOver());
        assertEquals(List.of(player1), gameBoard.getGameWinners());
    }

    @Test
    public void testIsGameOverPlayer2Wins() {

        gameBoard.initGame();
        int[] holes = new int[]{0, 0, 0, 0, 0, 0, 4, 32, 0, 0, 0, 0, 0, 36};
        gameBoard.setHoles(holes);
        gameBoard.handleEndOfTurn();
        assertTrue(gameBoard.isGameOver());
        assertEquals(List.of(player2), gameBoard.getGameWinners());
    }

    @Test
    public void testIsGameOverDraw() {

        gameBoard.initGame();
        int[] holes = new int[]{0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 36};
        gameBoard.setHoles(holes);
        gameBoard.handleEndOfTurn();
        assertTrue(gameBoard.isGameOver());
        assertEquals(List.of(player1, player2), gameBoard.getGameWinners());
    }

    @Test
    public void testPlayTurnValidMove() {

        gameBoard.initGame();
        assertDoesNotThrow(() -> gameBoard.playTurn(0));
    }

    @Test
    public void testPlayTurnInvalidMove() {

        gameBoard.initGame();
        assertThrows(IllegalArgumentException.class, () -> gameBoard.playTurn(7)); // Invalid move
    }

}

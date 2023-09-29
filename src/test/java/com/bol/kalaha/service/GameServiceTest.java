package com.bol.kalaha.service;

import com.bol.kalaha.common.converter.PlayerConverter;
import com.bol.kalaha.common.dao.GameBoardDao;
import com.bol.kalaha.common.dao.GameBoardDaoImpl;
import com.bol.kalaha.common.dao.PlayerDao;
import com.bol.kalaha.common.dao.PlayerDaoImpl;
import com.bol.kalaha.model.Player;
import com.bol.kalaha.model.dto.GameBoardInfo;
import com.bol.kalaha.model.gameboard.GameBoard;
import com.bol.kalaha.model.gameboard.GameBoardStatus;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameServiceTest {

    private GameServiceImpl gameService;
    private PlayerDao       playerDao;
    private GameBoardDao    gameBoardDao;
    int player1Id = 1;
    int player2Id = 2;

    @BeforeEach
    public void setUp() {

        playerDao = Mockito.mock(PlayerDaoImpl.class);
        gameBoardDao = Mockito.mock(GameBoardDaoImpl.class);
        gameService = new GameServiceImpl(playerDao, gameBoardDao);

        // Arrange the initial data
        Player player1 = getPlayer(player1Id, "Player-1");
        Player player2 = getPlayer(player2Id, "Player-2");

        Mockito.when(playerDao.getPlayerById(player1Id)).thenReturn(Optional.of(player1));
        Mockito.when(playerDao.getPlayerById(player2Id)).thenReturn(Optional.of(player2));
    }

    @Test
    public void testGetGame_InvalidGameId_ThrowsIllegalArgumentException() {
        // Arrange
        int gameId = 1;
        Mockito.when(gameBoardDao.getGameBoardById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> gameService.getGame(gameId));
    }

    @Test
    public void testInitGame_ValidPlayers_ReturnsGameBoardInfo() {
        // Arrange

        GameBoard gameBoard = getInitializedGameBoard(getPlayer(player1Id), getPlayer(player2Id));

        Mockito.when(gameBoardDao.getGameBoardByPlayerIds(player1Id, player2Id)).thenReturn(Optional.of(gameBoard));

        Mockito.when(gameBoardDao.addGameBoard(gameBoard))
            .thenReturn(gameBoard);

        Mockito.when(gameBoardDao.getGameBoardById(1))
            .thenReturn(Optional.of(gameBoard));

        // Act
        GameBoardInfo result = gameService.initGame(player1Id, player2Id);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), 1);
        // Add assertions for the expected result based on the mocked data
        assertEquals(result.getGameBoardStatus(), GameBoardStatus.PLAYER_1_TURN);
    }

    @Test
    public void testGamerNotSwitch_WhenTurnEndsInOwnStockPit() {
        // Arrange
        GameBoard gameBoard = getInitializedGameBoard(getPlayer(player1Id), getPlayer(player2Id));

        Mockito.when(gameBoardDao.getGameBoardByPlayerIds(player1Id, player2Id)).thenReturn(Optional.of(gameBoard));
        Mockito.when(gameBoardDao.addGameBoard(gameBoard))
            .thenReturn(gameBoard);

        Mockito.when(gameBoardDao.getGameBoardById(1))
            .thenReturn(Optional.of(gameBoard));

        // Act
        GameBoardInfo result = gameService.initGame(player1Id, player2Id);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), 1);
        // Add assertions for the expected result based on the mocked data
        assertEquals(result.getGameBoardStatus(), GameBoardStatus.PLAYER_1_TURN);

        // Act
        result = gameService.play(gameBoard.getId(), player1Id, 0);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), 1);
        // Add assertions for the expected result based on the mocked data
        assertEquals(result.getGameBoardStatus(), GameBoardStatus.PLAYER_1_TURN);
    }

    @Test
    public void testGamerSwitch_WhenTurnEndsInOpponentPit() {
        // Arrange

        GameBoard gameBoard = getInitializedGameBoard(getPlayer(player1Id), getPlayer(player2Id));

        Mockito.when(gameBoardDao.getGameBoardByPlayerIds(player1Id, player2Id)).thenReturn(Optional.of(gameBoard));

        Mockito.when(gameBoardDao.addGameBoard(gameBoard))
            .thenReturn(gameBoard);

        Mockito.when(gameBoardDao.getGameBoardById(1))
            .thenReturn(Optional.of(gameBoard));

        // Act
        GameBoardInfo result = gameService.initGame(player1Id, player2Id);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), 1);
        // Add assertions for the expected result based on the mocked data
        assertEquals(result.getGameBoardStatus(), GameBoardStatus.PLAYER_1_TURN);

        // play player1
        result = gameService.play(gameBoard.getId(), player1Id, 1);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), 1);
        // Add assertions for the expected result based on the mocked data
        assertEquals(result.getGameBoardStatus(), GameBoardStatus.PLAYER_2_TURN);
    }

    @Test
    public void testFinalizedGame_ReturnsCorrectGameBoardInfo() {
        // Arrange

        GameBoard gameBoard = getAlmostP1WinningBoard(getPlayer(player1Id), getPlayer(player2Id));

        Mockito.when(gameBoardDao.addGameBoard(gameBoard))
            .thenReturn(gameBoard);

        Mockito.when(gameBoardDao.getGameBoardById(1))
            .thenReturn(Optional.of(gameBoard));

        // Act
        GameBoardInfo result = gameService.play(gameBoard.getId(), player1Id, 5);

        // Assert that the winner is player1
        assertAll(
            () -> assertEquals(1, result.getWinners().size()),
            () -> assertTrue(result.getWinners().contains(PlayerConverter.toPlayerInfo(getPlayer(player1Id))))
        );

        // Add assertions for the expected result based on the mocked data
        assertEquals(result.getGameBoardStatus(), GameBoardStatus.GAME_FINALIZED);
    }

    @Test
    public void testP2Wins_Case() {

        GameBoard gameBoard = getAlmostP2WinningBoard(getPlayer(player1Id), getPlayer(player2Id));

        Mockito.when(gameBoardDao.getGameBoardByPlayerIds(player1Id, player2Id))
            .thenReturn(Optional.empty());

        Mockito.when(gameBoardDao.addGameBoard(gameBoard))
            .thenReturn(gameBoard);

        Mockito.when(gameBoardDao.getGameBoardById(1))
            .thenReturn(Optional.of(gameBoard));

        GameBoardInfo result = gameService.play(gameBoard.getId(), player2Id, 12);

        assertAll(
            () -> assertEquals(1, result.getWinners().size()),
            () -> assertTrue(result.getWinners().contains(PlayerConverter.toPlayerInfo(getPlayer(player2Id))))
        );

        assertEquals(result.getGameBoardStatus(), GameBoardStatus.GAME_FINALIZED);
    }

    @Test
    public void testCaptureWorks() {
        // Arrange

        GameBoard gameBoard = getWhenP2Played12_ThenP1Captures5Board(getPlayer(player1Id), getPlayer(player2Id));
        gameBoard.setId(2);

        Mockito.when(gameBoardDao.getGameBoardByPlayerIds(player1Id, player2Id))
            .thenReturn(Optional.empty());

        Mockito.when(gameBoardDao.addGameBoard(gameBoard))
            .thenReturn(gameBoard);

        Mockito.when(gameBoardDao.getGameBoardById(2))
            .thenReturn(Optional.of(gameBoard));

        // Act
        GameBoardInfo result = gameService.play(gameBoard.getId(), player2Id, 12);

        int[] expectedHoles = new int[]{1, 1, 1, 1, 2, 0, 34, 0, 5, 0, 0, 0, 0, 30};
        gameBoard.setHoles(expectedHoles);

        assertArrayEquals(expectedHoles, result.getHoles());
    }

    private GameBoard getInitializedGameBoard(Player player1, Player player2) {

        GameBoard gameBoard = new GameBoard();
        gameBoard.setPlayer1(player1);
        gameBoard.setPlayer2(player2);
        gameBoard.initGame();
        gameBoard.setId(1);
        return gameBoard;
    }

    private GameBoard getAlmostP1WinningBoard(Player player1, Player player2) {

        GameBoard gameBoard = getInitializedGameBoard(player1, player2);
        gameBoard.setHoles(new int[]{0, 0, 0, 0, 0, 1, 35, 0, 0, 0, 0, 1, 1, 34});

        return gameBoard;
    }

    private GameBoard getAlmostP2WinningBoard(Player player1, Player player2) {

        GameBoard gameBoard = getInitializedGameBoard(player1, player2);
        gameBoard.setHoles(new int[]{0, 1, 0, 0, 1, 0, 34, 0, 0, 0, 0, 0, 1, 35});
        gameBoard.setGameBoardStatus(GameBoardStatus.PLAYER_2_TURN);

        return gameBoard;
    }

    private GameBoard getWhenP2Played12_ThenP1Captures5Board(Player player1, Player player2) {

        GameBoard gameBoard = getInitializedGameBoard(player1, player2);
        gameBoard.setHoles(new int[]{0, 0, 0, 0, 1, 4, 34, 0, 5, 0, 0, 0, 8, 23});
        gameBoard.setGameBoardStatus(GameBoardStatus.PLAYER_2_TURN);

        return gameBoard;
    }

    private Player getPlayer(final int playerId) {

        if (playerId == 1)
            return getPlayer(player1Id, "Player-1");
        else if (playerId == 2) {
            return getPlayer(player2Id, "Player-2");
        }
        return null;
    }

    private Player getPlayer(final int playerId, final String name) {

        Player player = new Player();
        player.setId(playerId);
        player.setName(name);
        return player;
    }

}

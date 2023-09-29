
package com.bol.kalaha.model.gameboard;

import com.bol.kalaha.common.util.Preconditions;
import com.bol.kalaha.model.Player;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Data;

@Data
public class GameBoard {

    private final int PLAYER_1_STOCK_PIT_INDEX = 6;
    private final int PLAYER_2_STOCK_PIT_INDEX = 13;

    private Integer id;
    private Player  player1;
    private Player  player2;
    private int[]   holes;

    public  List<Player>    gameWinners = List.of();
    private GameBoardStatus gameBoardStatus;

    public Player getCurrentPlayer() {

        if (gameBoardStatus == GameBoardStatus.PLAYER_1_TURN) {
            return player1;
        } else {
            return player2;
        }
    }

    public void initGame() {

        Preconditions.checkNotNull(player1, "Impossible to init without player1");
        Preconditions.checkNotNull(player2, "Impossible to init without player2");

        holes = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
        gameBoardStatus = GameBoardStatus.PLAYER_1_TURN;
        gameWinners = List.of();
    }

    public boolean validateUserTurn(final Player player) {

        return getGameBoardStatus() == GameBoardStatus.PLAYER_1_TURN && player.equals(getPlayer1())
            || getGameBoardStatus() == GameBoardStatus.PLAYER_2_TURN && player.equals(getPlayer2());
    }
    // Include methods from GameTurnProcessService below

    private void validateMove(Player player, int pit) throws IllegalArgumentException {

        int[] board = this.getHoles();

        // Check if the pit is within valid bounds
        if (pit < 0 || pit >= board.length) {
            throw new IllegalArgumentException("Not a valid pit");
        }
        // Check if the pit is empty
        if (board[pit] == 0) {
            throw new IllegalArgumentException("There's no stone in pit " + pit + ".");
        }

        // Check if the player is attempting to play from their own pits
        if ((pit < PLAYER_1_STOCK_PIT_INDEX && !player.equals(player1))
            || (pit > PLAYER_1_STOCK_PIT_INDEX && !player.equals(player2))) {
            throw new IllegalArgumentException("You can only play from your pits");
        }
    }

    private int sow(int opposingPlayerStockPit, int pit) {

        int[] board      = this.getHoles();
        int   stoneCount = board[pit];
        board[pit] = 0;

        for (int i = 1; i <= stoneCount; i++) {
            int currentIndex = (pit + i) % board.length;
            if (currentIndex == opposingPlayerStockPit) {
                stoneCount++;
            } else {
                board[currentIndex]++;
            }
        }
        this.setHoles(board);
        return (pit + stoneCount) % board.length;
    }

    boolean isGameOver() {

        int[] board       = this.getHoles();
        int[] player1Pits = Arrays.copyOfRange(board, 0, PLAYER_1_STOCK_PIT_INDEX);
        int[] player2Pits = Arrays.copyOfRange(board, PLAYER_1_STOCK_PIT_INDEX + 1, PLAYER_2_STOCK_PIT_INDEX);
        return IntStream.of(player1Pits).allMatch(x -> x == 0) || IntStream.of(player2Pits).allMatch(x -> x == 0);
    }

    private List<Player> determineWinner() {

        if (!isGameOver()) {
            throw new RuntimeException("The game is not ended yet");
        }

        int[]              board = this.getHoles();
        final List<Player> result;

        if (board[PLAYER_1_STOCK_PIT_INDEX] > board[PLAYER_2_STOCK_PIT_INDEX]) {
            result = List.of(player1);
        } else if (board[PLAYER_2_STOCK_PIT_INDEX] > board[PLAYER_1_STOCK_PIT_INDEX]) {
            result = List.of(player2);
        } else {
            result = List.of(player1, player2);
        }
        this.setGameWinners(result);
        this.setGameBoardStatus(GameBoardStatus.GAME_FINALIZED);
        return result;
    }

    public void playTurn(int pitIndex) {

        Player currentPlayer = getCurrentPlayer();

        validateMove(currentPlayer, pitIndex);
        int index = sow(getOpponentPlayerStockPitIndex(), pitIndex);

        if (index == getCurrentPlayerStockPitIndex()) {
            handleEndOfTurn();
        } else if (canBeCaptured(index)) {
            handleCapture(index);
        } else {
            switchPlayer();
        }
    }

    private boolean canBeCaptured(int index) {

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.equals(player1)) {
            return index < PLAYER_1_STOCK_PIT_INDEX;
        }
        return index > PLAYER_1_STOCK_PIT_INDEX && index < PLAYER_2_STOCK_PIT_INDEX;
    }

    private void switchGameToFinalizedState() {

        determineWinner();
        this.setGameBoardStatus(GameBoardStatus.GAME_FINALIZED);
    }

    private Integer getCurrentPlayerStockPitIndex() {

        if (this.getGameBoardStatus() == GameBoardStatus.PLAYER_1_TURN) {
            return PLAYER_1_STOCK_PIT_INDEX;
        }
        return PLAYER_2_STOCK_PIT_INDEX;
    }

    private Integer getOpponentPlayerStockPitIndex() {

        if (this.getGameBoardStatus() == GameBoardStatus.PLAYER_1_TURN) {
            return PLAYER_2_STOCK_PIT_INDEX;
        }
        return PLAYER_1_STOCK_PIT_INDEX;
    }

    private void handleCapture(int index) {

        int[] board = this.getHoles();
        if (board[index] == 1) {
            int capturedStones = board[12 - index];
            board[12 - index] = 0;
            board[index] = 0;
            board[getCurrentPlayerStockPitIndex()] += capturedStones + 1;
            this.setHoles(board);
        }
        handleEndOfTurn();
    }

    void handleEndOfTurn() {

        if (isGameOver()) {
            switchGameToFinalizedState();
        }
    }

    public void switchPlayer() {

        if (this.getGameBoardStatus() == GameBoardStatus.PLAYER_1_TURN) {
            this.setGameBoardStatus(GameBoardStatus.PLAYER_2_TURN);
        } else if (this.getGameBoardStatus() == GameBoardStatus.PLAYER_2_TURN) {
            this.setGameBoardStatus(GameBoardStatus.PLAYER_1_TURN);
        }
    }
}

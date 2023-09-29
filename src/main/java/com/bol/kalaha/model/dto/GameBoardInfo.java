package com.bol.kalaha.model.dto;

import com.bol.kalaha.model.gameboard.GameBoardStatus;
import java.util.List;
import lombok.Data;

@Data
public class GameBoardInfo {

    private Integer          id;
    private PlayerInfo       player1;
    private PlayerInfo       player2;
    private int[]            holes;
    private GameBoardStatus  gameBoardStatus;
    private List<PlayerInfo> winners;

}

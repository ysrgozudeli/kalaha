package com.bol.kalaha.common.converter;

import com.bol.kalaha.model.dto.PlayerInfo;
import com.bol.kalaha.model.Player;

public class PlayerConverter {

    public static PlayerInfo toPlayerInfo(Player player) {

        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setId(player.getId());
        playerInfo.setName(player.getName());
        return playerInfo;
    }
}

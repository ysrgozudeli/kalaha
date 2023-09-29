package com.bol.kalaha.common.dao;

import com.bol.kalaha.model.Player;
import java.util.Optional;

public interface PlayerDao {

    // Get a player by ID
    Optional<Player> getPlayerById(int id);
}

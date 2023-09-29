package com.bol.kalaha.common.dao;

import com.bol.kalaha.model.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PlayerDaoImpl implements PlayerDao {

    private final Map<Integer, Player> players = new HashMap<>();

    public PlayerDaoImpl() {

        Player player1 = new Player();
        player1.setId(1);
        player1.setName("Player1");

        Player player2 = new Player();
        player2.setId(2);
        player2.setName("Player2");

        players.put(player1.getId(), player1);
        players.put(player2.getId(), player2);
    }

    // Get a player by ID
    @Override
    public Optional<Player> getPlayerById(int id) {

        return Optional.ofNullable(players.get(id));

    }

    // other functionalities if needed

}

package com.bol.kalaha.model.dto;

import lombok.Data;

@Data
public class PlayGameRequest {

    private Integer gameId;
    private Integer playerId;
    private Integer pitId;
}

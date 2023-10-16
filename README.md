# Kalaha Game Java Spring Boot Application

### Assumptions

In this section, we outline the key assumptions made in the development of the online Kalaha game system:

1. **User Limitation:**  The system accommodates multiple users; however, for the purpose of simplicity, it is
   restricted to only two users at the data layer, referred to as User-1 and User-2. In addition, GameService assumes
   that the users are authenticated and authorized by some resources, if needed.

2. **Initial Game State:** Initially, there are no existing games in the data layer. Consequently, the game should be
   initialized when any two users are registered in the system.

3. **Model Classes:** The GameBoard and Player classes are considered as model classes. Typically, these would be stored
   in a database, but in this sample application, they are managed as objects within the DAO layer.

### Manually testing the application

* Initiating a New Game

To initiate a new game, this request can be used:

``` 
HTTP Method = POST
Request URI = /api/game/new
Body = {"player1":1,"player2":2}
```

* and the result would be:

 ```
{
  "id": 1,
  "player1": {"id": 1, "name": "Player1"},
  "player2": {"id": 2, "name": "Player2"},
  "holes":[6,6,6,6,6,6,0,6,6,6,6,6,6,0],
  "gameBoardStatus": "PLAYER_1_TURN",
  "winners": []
}
```

### Playing a Game

To play a game, ensure that the correct user ID is used. The player's turn is indicated in the JSON result.
For example, if the player turn is "PLAYER_1_TURN" and "player1" of GameBoard points to "Player1" with an ID of 1,
the next player should be "Player1" with Game ID 1. Otherwise, the play will not be allowed.

```
HTTP Method = PATCH
Request URI = /api/game/play
Body = {"gameId":1,"playerId":1,"pitId":1}
```

* The result of this action will be:

``` 
{
  "id": 1,
  "player1": {"id": 1, "name": "Player1"},
  "player2": {"id": 2, "name": "Player2"},
   "holes":[6,0,7,7,7,7,1,7,6,6,6,6,6,0],
  "gameBoardStatus": "PLAYER_2_TURN",
  "winners"        : []
}
```

As seen on the JSON result, the game is not finalized yet and there is no winners yet. After a game is completed, the
winner or winners in withdraw case will be returned in "winners" array.

* Postman or any equivalent tool might be used for testing purpose.

### About Tests

* GameControllerIntegrationTest is an Integration Test which uses Mock MVC for generating REST API requests.
* GameBoardTest is a Unit test checks the GameBoard methods.
* GameServiceTest is a Unit test checks the GameService layer functionalities.
   

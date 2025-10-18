package service;

import dto.Game;

public interface GameService {
    void startGameSetup();
    void playGame(Game game);
}

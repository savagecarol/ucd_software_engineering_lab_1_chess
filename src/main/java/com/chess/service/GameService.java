package com.chess.service;

import com.chess.dto.Game;

public interface GameService {
    void startGameSetup();
    void playGame(Game game);
}

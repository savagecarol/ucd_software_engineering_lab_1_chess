package service;


import dto.Player;

import java.util.List;

public  interface PlayerService {
     void createPlayer(String name,String password);
     void displayLeaderboard();
     List<Player> getPlayers();
     Player findPlayerByName(String name);
     Player selectPlayer(String user1);
}

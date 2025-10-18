package service.impl;

import dto.Player;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import service.PlayerService;
import storage.PlayerStorage;
import validator.PlayerValidator;

public class PlayerServiceImpl implements PlayerService {

    private List<Player> players;
    private final Scanner scanner = new Scanner(System.in);
    public PlayerServiceImpl() {
        this.players = PlayerStorage.loadPlayers();
    }

    @Override
    public void createPlayer(String name, String password) {
        if (!PlayerValidator.isValid(name, password)) {
            return;
        }

        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                System.err.println(" ▶ Player with this name already exists.");
                return;
            }
        }

        Player player = new Player(name, password);
        players.add(player);
        PlayerStorage.savePlayers(players);
        System.out.println(" ▶ Player created: " + player.getName());
    }
    @Override
    public void displayLeaderboard() {
        if (players.isEmpty()) {
            System.err.println("No players available.");
            return;
        }

        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort(Comparator.comparingInt(Player::getRating).reversed());

        System.out.println("================================================================");
        System.out.println("                        LEADERBOARD                            ");
        System.out.println("================================================================");
        System.out.printf("| %-4s | %-15s | %-6s | %-3s | %-4s | %-4s | %-6s | %-5s | %-8s | %-7s |%n",
                "Rank", "Player", "Rating", "Win", "Draw", "Loss", "Points", "Games", "Captures", "Special");
        System.out.println("================================================================================================================");

        int rank = 0;
        int prevRating = Integer.MIN_VALUE;

        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player p = sortedPlayers.get(i);
            if (p.getRating() != prevRating) {
                rank = i + 1;
            }
            prevRating = p.getRating();

            System.out.printf("| %-4d | %-15s | %-6d | %-3d | %-4d | %-4d | %-6d | %-5d | %-8d | %-7d |%n",
                    rank,
                    p.getName(),
                    p.getRating(),
                    p.getWin(),
                    p.getDraw(),
                    p.getLoss(),
                    p.getTotalPoints(),
                    p.getGamesPlayed(),
                    p.getPiecesCaptured(),
                    p.getSpecialMoves()
            );
        }
        System.out.println("================================================================================================================");
        
        // Display piece values
        System.out.println("\n📊 Piece Values:");
        System.out.println("   Pawn: 1 point    Knight: 3 points    Bishop: 3 points");
        System.out.println("   Rook: 5 points   Queen: 9 points     King: 10 points");
        System.out.println("\n🎯 Bonus Points:");
        System.out.println("   Check: +2        Checkmate: +10       Castling: +1");
        System.out.println("   En Passant: +1   Promotion: +2        Win: +3, Draw: +1");
    }


    public List<Player> getPlayers() {
        return players;
    }

    public Player findPlayerByName(String name) {
        for (Player p : getPlayers()) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

     public Player selectPlayer(String userLabel) {
        while (true) {
            System.out.print("Enter " + userLabel + " name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            Player p = this.findPlayerByName(name);
            if (p == null) {
                System.out.println(" ▶ Player not found. Please try again.");
                continue;
            }

            if (!p.validatePassword(password)) {
                System.out.println(" ▶ Invalid password. Please try again.");
                continue;
            }
            return p;
        }
    }


}

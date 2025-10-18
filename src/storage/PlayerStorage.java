package storage;

import dto.Player;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerStorage {

    private static final String FILE_NAME = "storage/players.csv";

    public static List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return players;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 8) {
                    Player p;
                    if (parts.length >= 12) {
                        // New format with all fields
                        p = new Player(
                                parts[0], parts[1], parts[2],
                                Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                                Integer.parseInt(parts[5]), Integer.parseInt(parts[6]),
                                Integer.parseInt(parts[7]), Integer.parseInt(parts[8]),
                                Integer.parseInt(parts[9]), Integer.parseInt(parts[10]),
                                Integer.parseInt(parts[11])
                        );
                    } else {
                        // Old format - use defaults for new fields
                        p = new Player(
                                parts[0], parts[1], parts[2],
                                Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                                Integer.parseInt(parts[5]), Integer.parseInt(parts[6]),
                                Integer.parseInt(parts[7])
                        );
                    }
                    players.add(p);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return players;
    }

    public static void savePlayers(List<Player> players) {
        try {
            File file = new File(FILE_NAME);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("id,name,passwordHash,win,draw,loss,pending,rating,totalPoints,gamesPlayed,piecesCaptured,specialMoves\n");
                for (Player p : players) {
                    writer.write(String.format("%s,%s,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                            p.getId(),
                            p.getName(),
                            p.getPasswordHash(),
                            p.getWin(),
                            p.getDraw(),
                            p.getLoss(),
                            p.getPending(),
                            p.getRating(),
                            p.getTotalPoints(),
                            p.getGamesPlayed(),
                            p.getPiecesCaptured(),
                            p.getSpecialMoves()
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

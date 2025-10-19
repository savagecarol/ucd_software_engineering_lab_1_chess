package com.chess.storage;

import com.chess.dto.Game;
import com.chess.dto.Move;
import com.chess.dto.Player;
import com.chess.enums.ColourEnum;
import com.chess.enums.PiecesEnum;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GameStorage {
    
    private static final String STORAGE_DIR = "storage";
    private static final String FILE_EXTENSION = ".json";
    
    public static void saveGame(Game game) {
        try {
            // Ensure com.chess.storage directory exists
            Path storagePath = Paths.get(STORAGE_DIR);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            
            String fileName = game.getGameId() + FILE_EXTENSION;
            Path filePath = storagePath.resolve(fileName);
            
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(gameToJson(game));
            }
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }
    
    public static Game loadGame(String gameId) {
        try {
            Path filePath = Paths.get(STORAGE_DIR, gameId + FILE_EXTENSION);
            if (!Files.exists(filePath)) {
                return null;
            }
            
            String json = new String(Files.readAllBytes(filePath));
            return jsonToGame(json);
        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }
    
    public static List<String> getAllGameFiles() {
        List<String> gameFiles = new ArrayList<>();
        try {
            Path storagePath = Paths.get(STORAGE_DIR);
            if (!Files.exists(storagePath)) {
                return gameFiles;
            }
            
            Files.list(storagePath)
                .filter(path -> path.toString().endsWith(FILE_EXTENSION))
                .forEach(path -> gameFiles.add(path.getFileName().toString().replace(FILE_EXTENSION, "")));
        } catch (IOException e) {
            System.err.println("Error listing game files: " + e.getMessage());
        }
        return gameFiles;
    }
    
    public static List<String> getFinishedGames() {
        List<String> finishedGames = new ArrayList<>();
        for (String gameId : getAllGameFiles()) {
            Game game = loadGame(gameId);
            if (game != null && game.isFinished()) {
                finishedGames.add(gameId);
            }
        }
        return finishedGames;
    }
    
    public static List<String> getPendingGames() {
        List<String> pendingGames = new ArrayList<>();
        for (String gameId : getAllGameFiles()) {
            Game game = loadGame(gameId);
            if (game != null && !game.isFinished()) {
                pendingGames.add(gameId);
            }
        }
        return pendingGames;
    }
    
    public static List<String> getGamesForPlayers(String player1Name, String player2Name) {
        List<String> playerGames = new ArrayList<>();
        Pattern pattern = Pattern.compile("^" + player1Name + ":" + player2Name + "-\\d+$|^" + player2Name + ":" + player1Name + ":\\d+$");
        System.out.println(pattern);
        for (String gameId : getAllGameFiles()) {
            if (pattern.matcher(gameId).matches()) {
                playerGames.add(gameId);
            }
        }
        return playerGames;
    }
    
    private static String gameToJson(Game game) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"gameId\": \"").append(escapeJson(game.getGameId())).append("\",\n");
        json.append("  \"whitePlayer\": ").append(playerToJson(game.getWhitePlayer())).append(",\n");
        json.append("  \"blackPlayer\": ").append(playerToJson(game.getBlackPlayer())).append(",\n");
        json.append("  \"currentTurn\": \"").append(game.getCurrentTurn()).append("\",\n");
        json.append("  \"isFinished\": ").append(game.isFinished()).append(",\n");
        json.append("  \"whiteInCheck\": ").append(game.isWhiteInCheck()).append(",\n");
        json.append("  \"blackInCheck\": ").append(game.isBlackInCheck()).append(",\n");
        json.append("  \"moveHistory\": ").append(movesToJson(game.getMoveHistory())).append("\n");
        json.append("}");
        return json.toString();
    }
    
    private static String playerToJson(Player player) {
        return String.format("{\"id\":\"%s\",\"name\":\"%s\",\"win\":%d,\"draw\":%d,\"loss\":%d,\"pending\":%d,\"rating\":%d}",
                escapeJson(player.getId()),
                escapeJson(player.getName()),
                player.getWin(),
                player.getDraw(),
                player.getLoss(),
                player.getPending(),
                player.getRating());
    }
    
    
    private static String movesToJson(List<Move> moves) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < moves.size(); i++) {
            if (i > 0) json.append(",");
            json.append(moveToJson(moves.get(i)));
        }
        json.append("]");
        return json.toString();
    }
    
    private static String moveToJson(Move move) {
        String promotionStr = move.getPromotionPiece() != null ? 
            String.format(",\"promotionPiece\":\"%s\"", move.getPromotionPiece()) : "";
        
        return String.format("{\"fromFile\":\"%c\",\"fromRank\":%d,\"toFile\":\"%c\",\"toRank\":%d,\"playerId\":\"%s\",\"playerName\":\"%s\"%s}",
                move.getFromFile(),
                move.getFromRank(),
                move.getToFile(),
                move.getToRank(),
                escapeJson(move.getPlayer().getId()),
                escapeJson(move.getPlayer().getName()),
                promotionStr);
    }
    
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
    
    private static Game jsonToGame(String json) {
        // Simple JSON parsing - in a real application, you'd use a proper JSON library
        // This is a basic implementation for the chess game
        
        try {
            // Extract basic game info
            String gameId = extractStringValue(json, "gameId");
            String currentTurnStr = extractStringValue(json, "currentTurn");
            boolean isFinished = extractBooleanValue(json, "isFinished");
            boolean whiteInCheck = extractBooleanValue(json, "whiteInCheck");
            boolean blackInCheck = extractBooleanValue(json, "blackInCheck");
            
            // Extract players
            Player whitePlayer = extractPlayer(json, "whitePlayer");
            Player blackPlayer = extractPlayer(json, "blackPlayer");
            
            // Create game
            Game game = new Game(whitePlayer, blackPlayer);
            game.setGameId(gameId);
            game.setCurrentTurn(ColourEnum.valueOf(currentTurnStr));
            game.setFinished(isFinished);
            game.setWhiteInCheck(whiteInCheck);
            game.setBlackInCheck(blackInCheck);
            
            // Load move history and reconstruct board
            loadMovesFromJson(game, json);
            
            return game;
        } catch (Exception e) {
            System.err.println("Error parsing game JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private static String extractStringValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1).replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return "";
    }
    
    private static boolean extractBooleanValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*(true|false)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return Boolean.parseBoolean(m.group(1));
        }
        return false;
    }
    
    private static Player extractPlayer(String json, String key) {
        // This is a simplified extraction - you'd need more robust JSON parsing
        // For now, we'll create a basic player object
        String playerJson = extractObjectValue(json, key);
        String id = extractStringValue(playerJson, "id");
        String name = extractStringValue(playerJson, "name");
        int win = extractIntValue(playerJson, "win");
        int draw = extractIntValue(playerJson, "draw");
        int loss = extractIntValue(playerJson, "loss");
        int pending = extractIntValue(playerJson, "pending");
        int rating = extractIntValue(playerJson, "rating");
        
        return new Player(id, name, "", win, draw, loss, pending, rating);
    }
    
    private static String extractObjectValue(String json, String key) {
        // Simplified object extraction
        int start = json.indexOf("\"" + key + "\"");
        if (start == -1) return "{}";
        
        start = json.indexOf("{", start);
        if (start == -1) return "{}";
        
        int braceCount = 0;
        int end = start;
        for (int i = start; i < json.length(); i++) {
            if (json.charAt(i) == '{') braceCount++;
            if (json.charAt(i) == '}') braceCount--;
            if (braceCount == 0) {
                end = i + 1;
                break;
            }
        }
        
        return json.substring(start, end);
    }
    
    private static int extractIntValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*(\\d+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }
    
    private static void loadMovesFromJson(Game game, String json) {
        try {
            // Extract move history array
            String moveHistoryJson = extractArrayValue(json, "moveHistory");
            if (moveHistoryJson.isEmpty() || moveHistoryJson.equals("[]")) {
                return; // No moves to load
            }
            
            // Parse each move and reconstruct the board
            List<Move> moves = parseMovesFromJson(moveHistoryJson, game);
            
            // Reconstruct board by replaying all moves
            for (Move move : moves) {
                game.getBoard().movePiece(move);
                game.getMoveHistory().add(move);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading moves from JSON: " + e.getMessage());
        }
    }
    
    private static List<Move> parseMovesFromJson(String moveHistoryJson, Game game) {
        List<Move> moves = new ArrayList<>();
        
        // Remove brackets and split by move objects
        String content = moveHistoryJson.substring(1, moveHistoryJson.length() - 1);
        if (content.trim().isEmpty()) {
            return moves;
        }
        
        // Split by move objects (simple approach - look for }{ pattern)
        String[] moveStrings = content.split("\\},\\s*\\{");
        
        for (int i = 0; i < moveStrings.length; i++) {
            String moveStr = moveStrings[i].trim();
            
            // Add back the braces that were removed by split
            if (i == 0 && !moveStr.startsWith("{")) {
                moveStr = "{" + moveStr;
            }
            if (i == moveStrings.length - 1 && !moveStr.endsWith("}")) {
                moveStr = moveStr + "}";
            }
            if (i > 0 && !moveStr.startsWith("{")) {
                moveStr = "{" + moveStr;
            }
            
            Move move = parseSingleMove(moveStr, game);
            if (move != null) {
                moves.add(move);
            }
        }
        
        return moves;
    }
    
    private static Move parseSingleMove(String moveJson, Game game) {
        try {
            char fromFile = extractStringValue(moveJson, "fromFile").charAt(0);
            int fromRank = extractIntValue(moveJson, "fromRank");
            char toFile = extractStringValue(moveJson, "toFile").charAt(0);
            int toRank = extractIntValue(moveJson, "toRank");
            String playerId = extractStringValue(moveJson, "playerId");
            String promotionPieceStr = extractStringValue(moveJson, "promotionPiece");
            
            // Find the player
            Player player = null;
            if (playerId.equals(game.getWhitePlayer().getId())) {
                player = game.getWhitePlayer();
            } else if (playerId.equals(game.getBlackPlayer().getId())) {
                player = game.getBlackPlayer();
            }
            
            if (player == null) {
                System.err.println("Could not find player with ID: " + playerId);
                return null;
            }
            
            // Create move with or without promotion
            if (!promotionPieceStr.isEmpty()) {
                try {
                    PiecesEnum promotionPiece = PiecesEnum.valueOf(promotionPieceStr);
                    return new Move(fromFile, fromRank, toFile, toRank, player, promotionPiece);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid promotion piece: " + promotionPieceStr);
                    return new Move(fromFile, fromRank, toFile, toRank, player);
                }
            } else {
                return new Move(fromFile, fromRank, toFile, toRank, player);
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing move: " + e.getMessage());
            return null;
        }
    }
    
    private static String extractArrayValue(String json, String key) {
        int start = json.indexOf("\"" + key + "\"");
        if (start == -1) return "[]";
        
        start = json.indexOf("[", start);
        if (start == -1) return "[]";
        
        int bracketCount = 0;
        int end = start;
        for (int i = start; i < json.length(); i++) {
            if (json.charAt(i) == '[') bracketCount++;
            if (json.charAt(i) == ']') bracketCount--;
            if (bracketCount == 0) {
                end = i + 1;
                break;
            }
        }
        
        return json.substring(start, end);
    }
}



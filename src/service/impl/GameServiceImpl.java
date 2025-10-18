package service.impl;

import dto.Block;
import dto.ChessBoard;
import dto.Game;
import dto.Move;
import dto.Player;
import enums.ColourEnum;
import enums.PiecesEnum;
import java.util.List;
import java.util.Scanner;
import service.GameService;
import service.PlayerService;
import service.ScoringService;
import storage.GameStorage;
import storage.PlayerStorage;

public class GameServiceImpl implements GameService {

    private final Scanner scanner = new Scanner(System.in);
    private static final PlayerService playerService = new PlayerServiceImpl();

    @Override
    public void startGameSetup() {
        System.out.println(" ▶ Setting up a new game...");
         Player user1;
         Player user2;

        user1 = playerService.selectPlayer("User1");

        while (true) {
            user2 = playerService.selectPlayer("User2");
            if (!user2.getId().equals(user1.getId())) break;
            System.out.println(" ▶ User2 cannot be the same as User1. Choose another player.");
        }

        boolean changing = true;
        while (changing) {
            System.out.println("\nCurrent players:");
            System.out.println("User1: " + user1.getName());
            System.out.println("User2: " + user2.getName());

            System.out.println("Do you want to change any player? (yes/no)");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("yes")) break;

            System.out.println("Which user do you want to change? (1 or 2)");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                user1 = playerService.selectPlayer("User1");
                if (user1.getId().equals(user2.getId())) {
                    System.out.println(" ▶ User1 cannot be the same as User2. Re-enter User1.");
                    user1 = playerService.selectPlayer("User1");
                }
            } else if (choice.equals("2")) {
                user2 = playerService.selectPlayer("User2");
                if (user2.getId().equals(user1.getId())) {
                    System.out.println(" ▶ User2 cannot be the same as User1. Re-enter User2.");
                    user2 = playerService.selectPlayer("User2");
                }
            } else {
                System.out.println(" ▶ Invalid choice.");
            }
        }
        System.out.println("\n ▶ Game starting between " + user1.getName() + " and " + user2.getName() + "!");
        Game game = new Game(user1, user2);
        GameStorage.saveGame(game);
        playGame(game);
    }

    @Override
    public void playGame(Game game) {
        System.out.println(" ▶ Starting game: " + game.getGameId());
        System.out.println(" ▶ White: " + game.getCurrentPlayer().getName() +
                ", Black: " + game.getOpponent().getName());
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        ChessBoard board = game.getBoard();

        while (!game.isFinished()) {
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println();
            board.printBoard();
            game.printMoveHistory();

            System.out.println(" ▶ " + currentPlayer.getName() + "'s turn (" + game.getCurrentTurn() + ")");
            if (game.isWhiteInCheck() && game.getCurrentTurn() == enums.ColourEnum.WHITE) {
                System.out.println(" ⚠ White is in check!");
            }
            if (game.isBlackInCheck() && game.getCurrentTurn() == enums.ColourEnum.BLACK) {
                System.out.println(" ⚠ Black is in check!");
            }

            System.out.println("\nChoose an option:");
            System.out.println(" 1. Make a move");
            System.out.println(" 2. Hint (show valid moves for a piece)");
            System.out.println(" 3. Quit");
            System.out.print(" ▶ Enter choice (1/2/3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleMove(scanner, game, currentPlayer);
                    break;

                case "2":
                    handleHint(scanner, game);
                    break;

                case "3":
                    game.endGame();
                    GameStorage.saveGame(game);
                    System.out.println(" ▶ Game ended by player.");
                    return;

                default:
                    System.out.println(" ▶ Invalid option. Please enter 1, 2, or 3.");
            }
        }

        System.out.println(" ▶ Game over!");
        board.printBoard();
        game.printMoveHistory();
        
        // Calculate final game scores
        Player whitePlayer = game.getWhitePlayer();
        Player blackPlayer = game.getBlackPlayer();
        
        int whitePoints = ScoringService.calculateTotalGamePoints(whitePlayer, game);
        int blackPoints = ScoringService.calculateTotalGamePoints(blackPlayer, game);
        
        whitePlayer.addPoints(whitePoints);
        blackPlayer.addPoints(blackPoints);
        
        // Update player statistics
        if (game.isFinished()) {
            if (game.isWhiteInCheck() && game.getCurrentTurn() == ColourEnum.BLACK) {
                // Black wins
                blackPlayer.addWin();
                whitePlayer.addLoss();
                System.out.println(" ▶ " + blackPlayer.getName() + " wins! (+" + blackPoints + " points)");
                System.out.println(" ▶ " + whitePlayer.getName() + " loses. (+" + whitePoints + " points)");
            } else if (game.isBlackInCheck() && game.getCurrentTurn() == ColourEnum.WHITE) {
                // White wins
                whitePlayer.addWin();
                blackPlayer.addLoss();
                System.out.println(" ▶ " + whitePlayer.getName() + " wins! (+" + whitePoints + " points)");
                System.out.println(" ▶ " + blackPlayer.getName() + " loses. (+" + blackPoints + " points)");
            } else {
                // Draw
                whitePlayer.addDraw();
                blackPlayer.addDraw();
                System.out.println(" ▶ Game is a draw! Both players get +" + whitePoints + " points");
            }
        }
        
        // Save updated player data
        PlayerStorage.savePlayers(playerService.getPlayers());
        GameStorage.saveGame(game);
    }

    private void handleHint(Scanner scanner, Game game) {
        System.out.print(" ▶ Enter piece position (e.g., e2): ");
        String input = scanner.nextLine().trim().toLowerCase();

        if (!input.matches("^[a-h][1-8]$")) {
            System.out.println(" ▶ Invalid input. Example: e2");
            return;
        }

        char fromFile = input.charAt(0);
        int fromRank = Character.getNumericValue(input.charAt(1));

        game.getBoard().printHintBoard(fromFile, fromRank , game.getMoveHistory());
    }

    private void handleMove(Scanner scanner, Game game, Player currentPlayer) {
        System.out.print(" ▶ Enter your move (e.g., e2 e4): ");
        String input = scanner.nextLine().trim();

        String[] parts = input.split("\\s+");
        if (parts.length != 2) {
            System.out.println(" ▶ Invalid format. Example: e2 e4");
            return;
        }

        String from = parts[0].toLowerCase();
        String to = parts[1].toLowerCase();

        if (!from.matches("^[a-h][1-8]$") || !to.matches("^[a-h][1-8]$")) {
            System.out.println(" ▶ Invalid coordinates. Example: e2 e4");
            return;
        }

        try {
            char fromFile = from.charAt(0);
            int fromRank = Character.getNumericValue(from.charAt(1));
            char toFile = to.charAt(0);
            int toRank = Character.getNumericValue(to.charAt(1));

            // Check if this is a pawn promotion move
            Move move = createMoveWithPromotion(game, fromFile, fromRank, toFile, toRank, currentPlayer, scanner);

            boolean success = game.makeMove(move);
            if (success) {
                // Calculate and display move points
                int movePoints = ScoringService.calculateMovePoints(move, game);
                currentPlayer.addPoints(movePoints);
                
                // Check for special moves
                if (ScoringService.isCastlingMove(move) || ScoringService.isEnPassantMove(move) || move.getPromotionPiece() != null) {
                    currentPlayer.addSpecialMove();
                }
                
                // Display scoring information
                System.out.println(" ▶ Move successful! Points earned: " + movePoints);
                ScoringService.displayScoringInfo(move, game);
                
                GameStorage.saveGame(game);
                PlayerStorage.savePlayers(playerService.getPlayers());
            } else {
                System.out.println(" ▶ Invalid move. Try again.");
            }
        } catch (Exception e) {
            System.out.println(" ▶ Error: " + e.getMessage());
        }
    }

    private Move createMoveWithPromotion(Game game, char fromFile, int fromRank, char toFile, int toRank, Player currentPlayer, Scanner scanner) {
        // Check if this is a pawn promotion move
        Block fromBlock = game.getBoard().getBlock(fromFile, fromRank);
        if (fromBlock != null && fromBlock.getPiece() == PiecesEnum.PAWN) {
            ColourEnum player = fromBlock.getPlayer();
            int promotionRank = (player == ColourEnum.WHITE) ? 8 : 1;
            
            if (toRank == promotionRank) {
                // This is a pawn promotion move
                PiecesEnum promotionPiece = askForPromotionPiece(scanner);
                return new Move(fromFile, fromRank, toFile, toRank, currentPlayer, promotionPiece);
            }
        }
        
        // Regular move
        return new Move(fromFile, fromRank, toFile, toRank, currentPlayer);
    }

    private PiecesEnum askForPromotionPiece(Scanner scanner) {
        System.out.println(" ▶ Pawn promotion! Choose a piece:");
        System.out.println(" 1. Queen (Q)");
        System.out.println(" 2. Rook (R)");
        System.out.println(" 3. Bishop (B)");
        System.out.println(" 4. Knight (N)");
        System.out.print(" ▶ Enter your choice (1-4): ");

        while (true) {
            try {
                String input = scanner.nextLine().trim();
                switch (input) {
                    case "1":
                        return PiecesEnum.QUEEN;
                    case "2":
                        return PiecesEnum.ROOK;
                    case "3":
                        return PiecesEnum.BISHOP;
                    case "4":
                        return PiecesEnum.KNIGHT;
                    default:
                        System.out.print(" ▶ Invalid choice. Please enter 1, 2, 3, or 4: ");
                        break;
                }
            } catch (Exception e) {
                System.out.print(" ▶ Invalid input. Please enter 1, 2, 3, or 4: ");
            }
        }
    }

    public void loadFinishedGames() {
        try {
            List<String> finishedGames = GameStorage.getFinishedGames();
            
            if (finishedGames.isEmpty()) {
                System.out.println(" ▶ No finished games found.");
                return;
            }
            
            System.out.println(" ▶ Finished Games:");
            for (int i = 0; i < finishedGames.size(); i++) {
                System.out.println((i + 1) + ". " + finishedGames.get(i));
            }
            
            System.out.print(" ▶ Enter game number to replay (0 to cancel): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice > 0 && choice <= finishedGames.size()) {
                    String gameId = finishedGames.get(choice - 1);
                    replayGame(gameId);
                }
            } catch (NumberFormatException e) {
                System.out.println(" ▶ Invalid input.");
            }
        } catch (Exception e) {
            System.out.println(" ▶ Error: " + e.getMessage());
        }
    }

    public void continuePendingGame() {
        System.out.println(" ▶ Continuing previous game...");
        
        try {
            Player user1 = playerService.selectPlayer("User1");
            Player user2 = playerService.selectPlayer("User2");
            
            List<String> pendingGames = GameStorage.getGamesForPlayers(user1.getName(), user2.getName());
            pendingGames.removeIf(gameId -> {
                Game game = GameStorage.loadGame(gameId);
                return game != null && game.isFinished();
            });
            
            if (pendingGames.isEmpty()) {
                System.out.println(" ▶ No pending games found for these players.");
                return;
            }
            
            System.out.println(" ▶ Pending Games:");
            for (int i = 0; i < pendingGames.size(); i++) {
                System.out.println((i + 1) + ". " + pendingGames.get(i));
            }
            
            System.out.print(" ▶ Enter game number to continue (0 to cancel): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice > 0 && choice <= pendingGames.size()) {
                    String gameId = pendingGames.get(choice - 1);
                    Game game = GameStorage.loadGame(gameId);
                    if (game != null) {
                        System.out.println(" ▶ Resuming game: " + gameId);
                        playGame(game);
                    } else {
                        System.out.println(" ▶ Error loading game. Please try again.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println(" ▶ Invalid input.");
            }
        } catch (Exception e) {
            System.out.println(" ▶ Error: " + e.getMessage());
        }
    }

    private void replayGame(String gameId) {
        Game game = GameStorage.loadGame(gameId);
        if (game == null) {
            System.out.println(" ▶ Error loading game.");
            return;
        }
        
        System.out.println(" ▶ Replaying game: " + gameId);
        System.out.println(" ▶ White: " + game.getWhitePlayer().getName() + 
                          ", Black: " + game.getBlackPlayer().getName());
        
        // Create a temporary board to replay moves
        ChessBoard tempBoard = new ChessBoard();
        
        System.out.println(" ▶ Initial position:");
        tempBoard.printBoard();
        
        List<Move> moves = game.getMoveHistory();
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            System.out.println("\n ▶ Move " + (i + 1) + ": " + move);
            tempBoard.movePiece(move);
            tempBoard.printBoard();
            
            System.out.print(" ▶ Press Enter to continue to next move...");
            scanner.nextLine();
        }
        
        System.out.println(" ▶ Game replay completed.");
    }
}

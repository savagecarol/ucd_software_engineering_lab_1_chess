package com.chess.service;

import com.chess.dto.Game;
import com.chess.dto.Move;
import com.chess.dto.Player;
import com.chess.enums.ColourEnum;
import com.chess.enums.PiecesEnum;

public class ScoringService {
    
    // Standard chess piece values
    private static final int PAWN_VALUE = 1;
    private static final int KNIGHT_VALUE = 3;
    private static final int BISHOP_VALUE = 3;
    private static final int ROOK_VALUE = 5;
    private static final int QUEEN_VALUE = 9;
    private static final int KING_VALUE = 10;
    
    // Game result points
    private static final int WIN_POINTS = 3;
    private static final int DRAW_POINTS = 1;
    private static final int LOSS_POINTS = 0;
    
    // Move bonus points
    private static final int CHECK_BONUS = 2;
    private static final int CHECKMATE_BONUS = 10;
    private static final int CASTLING_BONUS = 1;
    private static final int EN_PASSANT_BONUS = 1;
    private static final int PROMOTION_BONUS = 2;
    
    public static int getPieceValue(PiecesEnum piece) {
        switch (piece) {
            case PAWN: return PAWN_VALUE;
            case KNIGHT: return KNIGHT_VALUE;
            case BISHOP: return BISHOP_VALUE;
            case ROOK: return ROOK_VALUE;
            case QUEEN: return QUEEN_VALUE;
            case KING: return KING_VALUE;
            default: return 0;
        }
    }
    
    public static int calculateMovePoints(Move move, Game game) {
        int points = 0;
        
        // Base points for piece movement
        points += getPieceValue(move.getPlayer().getId().equals(game.getWhitePlayer().getId()) ? 
                               PiecesEnum.PAWN : PiecesEnum.PAWN); // This will be updated based on actual piece
        
        // Bonus for special moves
        if (isCastlingMove(move)) {
            points += CASTLING_BONUS;
        }
        
        if (isEnPassantMove(move)) {
            points += EN_PASSANT_BONUS;
        }
        
        if (move.getPromotionPiece() != null) {
            points += PROMOTION_BONUS;
            points += getPieceValue(move.getPromotionPiece());
        }
        
        return points;
    }
    
    public static int calculateCapturePoints(PiecesEnum capturedPiece) {
        return getPieceValue(capturedPiece);
    }
    
    public static int calculateGameResultPoints(Player player, Game game) {
        if (game.isFinished()) {
            if (isWinner(player, game)) {
                return WIN_POINTS;
            } else if (isDraw(game)) {
                return DRAW_POINTS;
            } else {
                return LOSS_POINTS;
            }
        }
        return 0;
    }
    
    public static int calculateTotalGamePoints(Player player, Game game) {
        int totalPoints = 0;
        
        // Game result points
        totalPoints += calculateGameResultPoints(player, game);
        
        // Move points
        for (Move move : game.getMoveHistory()) {
            if (move.getPlayer().getId().equals(player.getId())) {
                totalPoints += calculateMovePoints(move, game);
            }
        }
        
        // Check/Checkmate bonuses
        if (game.isWhiteInCheck() && player.getId().equals(game.getBlackPlayer().getId())) {
            totalPoints += CHECK_BONUS;
        }
        if (game.isBlackInCheck() && player.getId().equals(game.getWhitePlayer().getId())) {
            totalPoints += CHECK_BONUS;
        }
        
        return totalPoints;
    }
    
    public static void updatePlayerStats(Player winner, Player loser, Game game) {
        if (game.isFinished()) {
            if (isWinner(winner, game)) {
                winner.addWin();
                loser.addLoss();
                winner.setRating(winner.getRating() + calculateGameResultPoints(winner, game));
                loser.setRating(loser.getRating() + calculateGameResultPoints(loser, game));
            } else if (isDraw(game)) {
                winner.addDraw();
                loser.addDraw();
                winner.setRating(winner.getRating() + DRAW_POINTS);
                loser.setRating(loser.getRating() + DRAW_POINTS);
            }
        }
    }
    
    public static boolean isCastlingMove(Move move) {
        // Check if it's a castling move (king moves 2 squares horizontally)
        int fileDiff = Math.abs(move.getToFile() - move.getFromFile());
        int rankDiff = Math.abs(move.getToRank() - move.getFromRank());
        return fileDiff == 2 && rankDiff == 0;
    }
    
    public static boolean isEnPassantMove(Move move) {
        // Check if it's an en passant move (pawn moves diagonally to empty square)
        int fileDiff = Math.abs(move.getToFile() - move.getFromFile());
        int rankDiff = Math.abs(move.getToRank() - move.getFromRank());
        return fileDiff == 1 && rankDiff == 1;
    }
    
    private static boolean isWinner(Player player, Game game) {
        if (!game.isFinished()) return false;
        
        // Check if the player's opponent is in checkmate
        if (player.getId().equals(game.getWhitePlayer().getId())) {
            return game.isBlackInCheck() && isCheckmate(game, ColourEnum.BLACK);
        } else {
            return game.isWhiteInCheck() && isCheckmate(game, ColourEnum.WHITE);
        }
    }
    
    private static boolean isDraw(Game game) {
        // Check for stalemate or other draw conditions
        return game.isFinished() && !game.isWhiteInCheck() && !game.isBlackInCheck();
    }
    
    private static boolean isCheckmate(Game game, ColourEnum player) {
        // Simplified checkmate detection
        return game.isFinished() && 
               ((player == ColourEnum.WHITE && game.isWhiteInCheck()) ||
                (player == ColourEnum.BLACK && game.isBlackInCheck()));
    }
    
    public static String getPieceValueString(PiecesEnum piece) {
        return piece.getSymbol() + "(" + getPieceValue(piece) + ")";
    }
    
    public static void displayScoringInfo(Move move, Game game) {
        System.out.println(" ▶ Move Analysis:");
        System.out.println("   Piece moved: " + getPieceValueString(PiecesEnum.PAWN)); // This should be actual piece
        System.out.println("   Move points: " + calculateMovePoints(move, game));
        
        if (move.getPromotionPiece() != null) {
            System.out.println("   Promotion: " + getPieceValueString(move.getPromotionPiece()) + " (+" + PROMOTION_BONUS + " bonus)");
        }
        
        if (isCastlingMove(move)) {
            System.out.println("   Castling bonus: +" + CASTLING_BONUS);
        }
        
        if (isEnPassantMove(move)) {
            System.out.println("   En passant bonus: +" + EN_PASSANT_BONUS);
        }
    }
}

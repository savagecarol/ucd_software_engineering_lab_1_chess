package com.chess.dto;

import com.chess.enums.ColourEnum;
import com.chess.enums.PiecesEnum;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ChessBoard {

    private final Block[][] board = new Block[8][8];

    public ChessBoard() {
        initializeBoard();
    }

    public Block[][] getBoard() {
        return board;
    }

    public Block getBlock(char file, int rank) {
        int row = rank - 1;
        int col = file - 'a';
        if (row < 0 || row > 7 || col < 0 || col > 7) return null;
        return board[row][col];
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            int rank = i + 1;
            for (int j = 0; j < 8; j++) {
                char file = (char) ('a' + j);
                ColourEnum squareColour = (i + j) % 2 == 0 ? ColourEnum.WHITE : ColourEnum.BLACK;
                board[i][j] = new Block(PiecesEnum.EMPTY, squareColour, ColourEnum.BLANK, file, rank);
            }
        }

        board[0][0].setPiece(PiecesEnum.ROOK);   board[0][0].setPlayer(ColourEnum.WHITE);
        board[0][1].setPiece(PiecesEnum.KNIGHT); board[0][1].setPlayer(ColourEnum.WHITE);
        board[0][2].setPiece(PiecesEnum.BISHOP); board[0][2].setPlayer(ColourEnum.WHITE);
        board[0][3].setPiece(PiecesEnum.QUEEN);  board[0][3].setPlayer(ColourEnum.WHITE);
        board[0][4].setPiece(PiecesEnum.KING);   board[0][4].setPlayer(ColourEnum.WHITE);
        board[0][5].setPiece(PiecesEnum.BISHOP); board[0][5].setPlayer(ColourEnum.WHITE);
        board[0][6].setPiece(PiecesEnum.KNIGHT); board[0][6].setPlayer(ColourEnum.WHITE);
        board[0][7].setPiece(PiecesEnum.ROOK);   board[0][7].setPlayer(ColourEnum.WHITE);

        for (int j = 0; j < 8; j++) {
            board[1][j].setPiece(PiecesEnum.PAWN);
            board[1][j].setPlayer(ColourEnum.WHITE);
        }

        // Place black pieces
        board[7][0].setPiece(PiecesEnum.ROOK);   board[7][0].setPlayer(ColourEnum.BLACK);
        board[7][1].setPiece(PiecesEnum.KNIGHT); board[7][1].setPlayer(ColourEnum.BLACK);
        board[7][2].setPiece(PiecesEnum.BISHOP); board[7][2].setPlayer(ColourEnum.BLACK);
        board[7][3].setPiece(PiecesEnum.QUEEN);  board[7][3].setPlayer(ColourEnum.BLACK);
        board[7][4].setPiece(PiecesEnum.KING);   board[7][4].setPlayer(ColourEnum.BLACK);
        board[7][5].setPiece(PiecesEnum.BISHOP); board[7][5].setPlayer(ColourEnum.BLACK);
        board[7][6].setPiece(PiecesEnum.KNIGHT); board[7][6].setPlayer(ColourEnum.BLACK);
        board[7][7].setPiece(PiecesEnum.ROOK);   board[7][7].setPlayer(ColourEnum.BLACK);

        for (int j = 0; j < 8; j++) {
            board[6][j].setPiece(PiecesEnum.PAWN);
            board[6][j].setPlayer(ColourEnum.BLACK);
        }
    }

    public void printBoard() {
        for (int i = 7; i >= 0; i--) {
            System.out.print(" " + (i + 1) + " ");

            for (int j = 0; j < 8; j++) {
                Block b = board[i][j];

                String pieceSymbol = getPieceSymbol(b);

                String bgColor = (b.getColour() == ColourEnum.WHITE) ? "\u001B[47m" : "\u001B[100m";

                String textColor;
                if (b.getPlayer() == ColourEnum.WHITE) {
                    textColor = "\u001B[97m";
                } else if (b.getPlayer() == ColourEnum.BLACK) {
                    textColor = "\u001B[30m";
                } else {
                    textColor = (b.getColour() == ColourEnum.WHITE) ? "\u001B[30m" : "\u001B[37m";
                }
                System.out.print(bgColor + textColor + " " + pieceSymbol + " " + "\u001B[0m");
            }
            System.out.println();
        }
        System.out.print("    ");
        for (char c = 'a'; c <= 'h'; c++) {
            System.out.print(" " + c + "  ");
        }
        System.out.println();
    }


    public void printHintBoard(char fromFile, int fromRank, List<Move> history) {
        System.out.println(" ▶ Showing valid moves for " + fromFile + fromRank + ":");

        List<Move> possibleMoves = getPossibleMoves(fromFile, fromRank , history, null);
        if (possibleMoves == null || possibleMoves.isEmpty()) {
            System.out.println(" ▶ No valid moves available for that piece.");
            return;
        }


        Set<String> moveTargets = new HashSet<>();
        for (Move m : possibleMoves) {
            moveTargets.add("" + m.getToFile() + m.getToRank());
        }

        for (int rankIndex = 7; rankIndex >= 0; rankIndex--) {
            System.out.print(" " + (rankIndex + 1) + " ");
            for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
                Block b = board[rankIndex][fileIndex];
                char file = (char) ('a' + fileIndex);
                int rank = rankIndex + 1;

                String coord = "" + file + rank;
                String pieceSymbol = getPieceSymbol(b);

                String bgColor = (b.getColour() == ColourEnum.WHITE) ? "\u001B[47m" : "\u001B[100m";
                if (moveTargets.contains(coord)) {
                    bgColor = "\u001B[42m";
                }
                String textColor;
                if (b.getPlayer() == ColourEnum.WHITE) textColor = "\u001B[30m";
                else if (b.getPlayer() == ColourEnum.BLACK) textColor = "\u001B[97m";
                else textColor = (b.getColour() == ColourEnum.WHITE) ? "\u001B[30m" : "\u001B[37m";

                System.out.print(bgColor + textColor + " " + pieceSymbol + " " + "\u001B[0m");
            }
            System.out.println();
        }

        System.out.print("     ");
        for (char c = 'a'; c <= 'h'; c++) {
            System.out.print(c + "   ");
        }
        System.out.println();
    }

    private void addPawnMoves(List<Move> moves, int row, int col, ColourEnum player, char fromFile, int fromRank, List<Move> history, Player playerObj) {
        int dir = (player == ColourEnum.WHITE) ? 1 : -1;
        int startRow = (player == ColourEnum.WHITE) ? 1 : 6;
        int nextRow = row + dir;
        int promotionRow = (player == ColourEnum.WHITE) ? 7 : 0;

        if (inBounds(nextRow, col) && board[nextRow][col].getPlayer() == ColourEnum.BLANK) {
            // Check if this is a promotion move
            if (nextRow == promotionRow) {
                addPromotionMoves(moves, fromFile, fromRank, (char)('a' + col), nextRow + 1, playerObj);
            } else {
                moves.add(new Move(fromFile, fromRank, (char)('a' + col), nextRow + 1, playerObj));
            }

            int twoRow = row + 2 * dir;
            if (row == startRow && inBounds(twoRow, col) && board[twoRow][col].getPlayer() == ColourEnum.BLANK) {
                moves.add(new Move(fromFile, fromRank, (char)('a' + col), twoRow + 1, playerObj));
            }
        }

        int[][] caps = {{dir, -1}, {dir, 1}};
        for (int[] off : caps) {
            int r = row + off[0];
            int c = col + off[1];
            if (!inBounds(r, c)) continue;
            Block target = board[r][c];
            if (isOpponent(target, player)) {
                // Check if this is a promotion capture
                if (r == promotionRow) {
                    addPromotionMoves(moves, fromFile, fromRank, (char)('a' + c), r + 1, playerObj);
                } else {
                    moves.add(new Move(fromFile, fromRank, (char)('a' + c), r + 1, playerObj));
                }
            }
        }

        if (history != null && !history.isEmpty()) {
            Move last = history.get(history.size() - 1);
            Block lastBlock = getBlock(last.getToFile(), last.getToRank());

            if (lastBlock != null
                    && lastBlock.getPiece() == PiecesEnum.PAWN
                    && Math.abs(last.getFromRank() - last.getToRank()) == 2) {

                int lastCol = last.getToFile() - 'a';
                int lastRow = last.getToRank() - 1;

                if (lastRow == row && Math.abs(lastCol - col) == 1) {
                    int captureRow = row + dir;
                    // Check if this is a promotion en passant
                    if (captureRow == promotionRow) {
                        addPromotionMoves(moves, fromFile, fromRank, last.getToFile(), captureRow + 1, playerObj);
                    } else {
                        moves.add(new Move(fromFile, fromRank, last.getToFile(), captureRow + 1, playerObj));
                    }
                }
            }
        }
    }

    private void addPromotionMoves(List<Move> moves, char fromFile, int fromRank, char toFile, int toRank, Player player) {
        // Add moves for all possible promotion pieces (Queen, Rook, Bishop, Knight)
        moves.add(new Move(fromFile, fromRank, toFile, toRank, player, PiecesEnum.QUEEN));
        moves.add(new Move(fromFile, fromRank, toFile, toRank, player, PiecesEnum.ROOK));
        moves.add(new Move(fromFile, fromRank, toFile, toRank, player, PiecesEnum.BISHOP));
        moves.add(new Move(fromFile, fromRank, toFile, toRank, player, PiecesEnum.KNIGHT));
    }


    private void addCastlingMoves(List<Move> moves, int row, int col, ColourEnum player, List<Move> history, Player playerObj){
        boolean kingMoved = hasKingMoved(player, history);
        if(kingMoved) return;

        // King-side castling
        if(!hasRookMoved(player,'h',history)){
            if(col+2 < 8 && board[row][col+1].getPiece()==PiecesEnum.EMPTY && board[row][col+2].getPiece()==PiecesEnum.EMPTY){
                // Check if king would pass through or end up in check
                if(!isSquareUnderAttack((char)('f'), row+1, player, history) && 
                   !isSquareUnderAttack((char)('g'), row+1, player, history)) {
                    moves.add(new Move((char)('e'), row+1, (char)('g'), row+1, playerObj));
                }
            }
        }

        // Queen-side castling
        if(!hasRookMoved(player,'a',history)){
            if(col-3 >= 0 && board[row][col-1].getPiece()==PiecesEnum.EMPTY && board[row][col-2].getPiece()==PiecesEnum.EMPTY && board[row][col-3].getPiece()==PiecesEnum.EMPTY){
                // Check if king would pass through or end up in check
                if(!isSquareUnderAttack((char)('d'), row+1, player, history) && 
                   !isSquareUnderAttack((char)('c'), row+1, player, history)) {
                    moves.add(new Move((char)('e'), row+1, (char)('c'), row+1, playerObj));
                }
            }
        }
    }

    private boolean hasKingMoved(ColourEnum player, List<Move> history){
        char kingFile = 'e';
        int kingRank = player==ColourEnum.WHITE?1:8;
        for(Move m: history){
            if(m.getFromFile()==kingFile && m.getFromRank()==kingRank && getBlock(m.getFromFile(), m.getFromRank()).getPiece()==PiecesEnum.KING)
                return true;
        }
        return false;
    }

    private boolean hasRookMoved(ColourEnum player, char rookFile, List<Move> history){
        int rookRank = player==ColourEnum.WHITE?1:8;
        for(Move m: history){
            if(m.getFromFile()==rookFile && m.getFromRank()==rookRank && getBlock(m.getFromFile(), m.getFromRank()).getPiece()==PiecesEnum.ROOK)
                return true;
        }
        return false;
    }

    private boolean isSquareUnderAttack(char file, int rank, ColourEnum player, List<Move> history) {
        ColourEnum opponent = (player == ColourEnum.WHITE) ? ColourEnum.BLACK : ColourEnum.WHITE;
        
        // Check if any opponent piece can attack this square
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Block block = board[i][j];
                if (block.getPlayer() == opponent) {
                    List<Move> possibleMoves = getPossibleMoves((char)('a' + j), i + 1, history, null);
                    for (Move move : possibleMoves) {
                        if (move.getToFile() == file && move.getToRank() == rank) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }



        public List<Move> getPossibleMoves(char fromFile, int fromRank, List<Move> history, Player playerObj) {
            List<Move> moves = new ArrayList<>();

            Block from = getBlock(fromFile, fromRank);
            if (from == null || from.getPiece() == PiecesEnum.EMPTY || from.getPlayer() == ColourEnum.BLANK)
                return moves;

            ColourEnum player = from.getPlayer();
            int row = fromRank - 1;
            int col = fromFile - 'a';

            switch (from.getPiece()) {
                case PAWN:
                    addPawnMoves(moves, row, col, player, fromFile, fromRank, history, playerObj);
                    break;
                case KNIGHT:
                    int[][] knightOffsets = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
                    for (int[] off : knightOffsets) {
                        int r = row + off[0], c = col + off[1];
                        if (!inBounds(r, c)) continue;
                        Block target = board[r][c];
                        if (target.getPlayer() == ColourEnum.BLANK || isOpponent(target, player))
                            moves.add(new Move(fromFile, fromRank, (char)('a'+c), r+1, playerObj));
                    }
                    break;
                case BISHOP:
                    addSlidingMoves(moves, row, col, player, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}}, fromFile, fromRank, playerObj);
                    break;
                case ROOK:
                    addSlidingMoves(moves, row, col, player, new int[][]{{1,0},{-1,0},{0,1},{0,-1}}, fromFile, fromRank, playerObj);
                    break;
                case QUEEN:
                    addSlidingMoves(moves, row, col, player, new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}}, fromFile, fromRank, playerObj);
                    break;
                case KING:
                    // Normal king moves
                    for (int dr=-1; dr<=1; dr++) {
                        for (int dc=-1; dc<=1; dc++) {
                            if (dr==0 && dc==0) continue;
                            int r=row+dr, c=col+dc;
                            if (!inBounds(r,c)) continue;
                            Block target = board[r][c];
                            if (target.getPlayer() == ColourEnum.BLANK || isOpponent(target, player))
                                moves.add(new Move(fromFile, fromRank, (char)('a'+c), r+1, playerObj));
                        }
                    }
                    // Castling
                    addCastlingMoves(moves, row, col, player, history, playerObj);
                    break;
            }
            return moves;
        }


    private boolean inBounds(int r, int c) { return r>=0 && r<8 && c>=0 && c<8; }

    private boolean isOpponent(Block b, ColourEnum player) {
        return b!=null && b.getPlayer()!=ColourEnum.BLANK && b.getPlayer()!=player;
    }

    private void addSlidingMoves(List<Move> moves, int row, int col, ColourEnum player, int[][] directions, char fromFile, int fromRank, Player playerObj) {
        for(int[] dir: directions){
            int r=row+dir[0], c=col+dir[1];
            while(inBounds(r,c)){
                Block target = board[r][c];
                if(target.getPlayer()==ColourEnum.BLANK) moves.add(new Move(fromFile, fromRank, (char)('a'+c), r+1, playerObj));
                else {
                    if(isOpponent(target, player)) moves.add(new Move(fromFile, fromRank, (char)('a'+c), r+1, playerObj));
                    break;
                }
                r+=dir[0]; c+=dir[1];
            }
        }
    }



    private String getPieceSymbol(Block b) {
        if (b.getPiece() == PiecesEnum.EMPTY) return "  ";
        String player = b.getPlayer() == ColourEnum.WHITE ? "W" : "B";
        return player + b.getPiece().getSymbol();
    }

    public boolean movePiece(Move move) {
        Block from = getBlock(move.getFromFile(), move.getFromRank());
        Block to = getBlock(move.getToFile(), move.getToRank());

        if (from == null || to == null) {
            System.out.println(" ▶ Invalid move: out of bounds.");
            return false;
        }

        if (from.getPiece() == PiecesEnum.EMPTY) {
            System.out.println(" ▶ No piece at source.");
            return false;
        }

        // Handle special moves
        if (from.getPiece() == PiecesEnum.PAWN) {
            // Check for en passant
            if (isEnPassantMove(move)) {
                return executeEnPassant(move);
            }
        } else if (from.getPiece() == PiecesEnum.KING) {
            // Check for castling
            if (isCastlingMove(move)) {
                return executeCastling(move);
            }
        }

        // Regular move
        PiecesEnum pieceToPlace = from.getPiece();
        
        // Handle pawn promotion
        if (from.getPiece() == PiecesEnum.PAWN && move.getPromotionPiece() != null) {
            pieceToPlace = move.getPromotionPiece();
        }
        
        to.setPiece(pieceToPlace);
        to.setPlayer(from.getPlayer());
        from.setPiece(PiecesEnum.EMPTY);
        from.setPlayer(ColourEnum.BLANK);
        return true;
    }

    private boolean isEnPassantMove(Move move) {
        // En passant: pawn moves diagonally to an empty square
        Block from = getBlock(move.getFromFile(), move.getFromRank());
        Block to = getBlock(move.getToFile(), move.getToRank());
        
        if (from.getPiece() != PiecesEnum.PAWN || to.getPiece() != PiecesEnum.EMPTY) {
            return false;
        }
        
        // Check if it's a diagonal move
        int fileDiff = Math.abs(move.getToFile() - move.getFromFile());
        int rankDiff = Math.abs(move.getToRank() - move.getFromRank());
        
        return fileDiff == 1 && rankDiff == 1;
    }

    private boolean executeEnPassant(Move move) {
        Block from = getBlock(move.getFromFile(), move.getFromRank());
        Block to = getBlock(move.getToFile(), move.getToRank());
        
        // Move the pawn
        to.setPiece(from.getPiece());
        to.setPlayer(from.getPlayer());
        from.setPiece(PiecesEnum.EMPTY);
        from.setPlayer(ColourEnum.BLANK);
        
        // Remove the captured pawn (it's on the same file but different rank)
        int capturedRank = move.getFromRank(); // The rank where the captured pawn is
        Block capturedPawn = getBlock(move.getToFile(), capturedRank);
        if (capturedPawn != null && capturedPawn.getPiece() == PiecesEnum.PAWN) {
            capturedPawn.setPiece(PiecesEnum.EMPTY);
            capturedPawn.setPlayer(ColourEnum.BLANK);
        }
        
        return true;
    }

    private boolean isCastlingMove(Move move) {
        Block from = getBlock(move.getFromFile(), move.getFromRank());
        
        if (from.getPiece() != PiecesEnum.KING) {
            return false;
        }
        
        // Check if it's a castling move (king moves 2 squares horizontally)
        int fileDiff = Math.abs(move.getToFile() - move.getFromFile());
        int rankDiff = Math.abs(move.getToRank() - move.getFromRank());
        
        return fileDiff == 2 && rankDiff == 0;
    }

    private boolean executeCastling(Move move) {
        Block from = getBlock(move.getFromFile(), move.getFromRank());
        Block to = getBlock(move.getToFile(), move.getToRank());
        
        // Move the king
        to.setPiece(from.getPiece());
        to.setPlayer(from.getPlayer());
        from.setPiece(PiecesEnum.EMPTY);
        from.setPlayer(ColourEnum.BLANK);
        
        // Move the rook
        if (move.getToFile() == 'g') { // King-side castling
            Block rookFrom = getBlock('h', move.getFromRank());
            Block rookTo = getBlock('f', move.getFromRank());
            
            if (rookFrom != null && rookTo != null && rookFrom.getPiece() == PiecesEnum.ROOK) {
                rookTo.setPiece(rookFrom.getPiece());
                rookTo.setPlayer(rookFrom.getPlayer());
                rookFrom.setPiece(PiecesEnum.EMPTY);
                rookFrom.setPlayer(ColourEnum.BLANK);
            }
        } else if (move.getToFile() == 'c') { // Queen-side castling
            Block rookFrom = getBlock('a', move.getFromRank());
            Block rookTo = getBlock('d', move.getFromRank());
            
            if (rookFrom != null && rookTo != null && rookFrom.getPiece() == PiecesEnum.ROOK) {
                rookTo.setPiece(rookFrom.getPiece());
                rookTo.setPlayer(rookFrom.getPlayer());
                rookFrom.setPiece(PiecesEnum.EMPTY);
                rookFrom.setPlayer(ColourEnum.BLANK);
            }
        }
        
        return true;
    }
}

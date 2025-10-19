package com.chess.dto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Player {
    private String id;
    private String name;
    private String passwordHash;
    private int win;
    private int draw;
    private int loss;
    private int pending;
    private int rating;
    private int totalPoints;
    private int gamesPlayed;
    private int piecesCaptured;
    private int specialMoves;

    public Player(String name, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.passwordHash = hashPassword(password);
        this.win = 0;
        this.draw = 0;
        this.loss = 0;
        this.pending = 0;
        this.rating = 100;
        this.totalPoints = 0;
        this.gamesPlayed = 0;
        this.piecesCaptured = 0;
        this.specialMoves = 0;
    }

    public Player(String id, String name, String passwordHash ,int win, int draw, int loss, int pending, int rating) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.pending = pending;
        this.rating = rating;
        this.totalPoints = 0;
        this.gamesPlayed = 0;
        this.piecesCaptured = 0;
        this.specialMoves = 0;
    }

    public Player(String id, String name, String passwordHash, int win, int draw, int loss, int pending, int rating, int totalPoints, int gamesPlayed, int piecesCaptured, int specialMoves) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.pending = pending;
        this.rating = rating;
        this.totalPoints = totalPoints;
        this.gamesPlayed = gamesPlayed;
        this.piecesCaptured = piecesCaptured;
        this.specialMoves = specialMoves;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getWin() { return win; }
    public void setWin(int win) { this.win = win; }

    public int getDraw() { return draw; }
    public void setDraw(int draw) { this.draw = draw; }

    public int getLoss() { return loss; }
    public void setLoss(int loss) { this.loss = loss; }

    public int getPending() { return pending; }
    public void setPending(int pending) { this.pending = pending; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }

    public int getGamesPlayed() { return gamesPlayed; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }

    public int getPiecesCaptured() { return piecesCaptured; }
    public void setPiecesCaptured(int piecesCaptured) { this.piecesCaptured = piecesCaptured; }

    public int getSpecialMoves() { return specialMoves; }
    public void setSpecialMoves(int specialMoves) { this.specialMoves = specialMoves; }

    public void addWin() { this.win++; this.gamesPlayed++; }
    public void addDraw() { this.draw++; this.gamesPlayed++; }
    public void addLoss() { this.loss++; this.gamesPlayed++; }
    public void addPending() { this.pending++; }
    public void completePending() { if (this.pending > 0) this.pending--; }
    public void addPoints(int points) { this.totalPoints += points; }
    public void addCapturedPiece() { this.piecesCaptured++; }
    public void addSpecialMove() { this.specialMoves++; }

    public String getPasswordHash() { return passwordHash; }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found");
        }
    }

    public boolean validatePassword(String password) {
        String hash = hashPassword(password);
        return hash.equals(this.passwordHash);
    }

    @Override
    public String toString() {
        return String.format("Player{id='%s', name='%s', W=%d, D=%d, L=%d, Pending=%d, Rating=%d, Points=%d, Games=%d, Captures=%d, Special=%d}",
                id, name, win, draw, loss, pending, rating, totalPoints, gamesPlayed, piecesCaptured, specialMoves);
    }
}

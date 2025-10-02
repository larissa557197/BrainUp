package br.com.fiap.brainup.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Player {
    private UUID id = UUID.randomUUID();
    private String name;
    private int score;
    private boolean active;

    // construtor

    public Player(UUID id, String name, int score, boolean active) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

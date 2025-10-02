package br.com.fiap.brainup.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Quiz {

    private UUID id = UUID.randomUUID();
    private List<Question> questions;
    private List<Player> players;

    // construtor


    public Quiz(UUID id, List<Question> questions, List<Player> players) {
        this.id = id;
        this.questions = questions;
        this.players = players;
    }

    // métodos
    public void addPlayer(Player player) {
        if(player != null) {
            this.players.add(player);
        }
    }

    // getters e setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}

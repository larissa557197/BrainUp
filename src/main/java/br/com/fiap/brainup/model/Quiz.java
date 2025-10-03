package br.com.fiap.brainup.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Quiz {

    private UUID id = UUID.randomUUID();
    private List<Question> questions = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    // métodos
    public void addPlayer(Player player) {
        players.add(player);
    }

}

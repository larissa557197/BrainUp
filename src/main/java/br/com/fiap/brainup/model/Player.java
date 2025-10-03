package br.com.fiap.brainup.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Player {
    private UUID id = UUID.randomUUID();
    private String name;
    private int score = 0;
    private boolean active = true;

    // construtor
    public Player( String name) {
        this.name = name;
    }


}

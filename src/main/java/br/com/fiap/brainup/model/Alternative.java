package br.com.fiap.brainup.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Alternative {
    private UUID id = UUID.randomUUID();
    private String text;
    private boolean correct;

    // getters e setters

    public UUID getId() {
        return id;
    }


    public String getText() {
        return text;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

package br.com.fiap.brainup.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Question {

    private UUID id = UUID.randomUUID();
    private String text;
    private List<Alternative> alternatives;

    // getters e setters

    public UUID getId() {
        return id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Alternative> alternatives) {
        this.alternatives = alternatives;
    }
}

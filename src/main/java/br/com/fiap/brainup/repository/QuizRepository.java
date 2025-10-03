package br.com.fiap.brainup.repository;

import br.com.fiap.brainup.model.Alternative;
import br.com.fiap.brainup.model.Question;
import br.com.fiap.brainup.model.Quiz;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.List;

// indica que essa classe é um componente de persistência (Bean gerenciado pelo Spring)
@Repository

// lombok gera automaticamente o getter para o atributo "quiz"
@Getter
public class QuizRepository{

    // mantém a instância única de Quiz carregada na aplicação
    private final Quiz quiz;

    // construtor que inicializa o quiz com perguntas fixas
    public QuizRepository() {
        // lista de perguntas criadas manualmente com alternativas
        List<Question> questions = List.of(
                createQuestion("Qual palavra-chave é usada para definir uma classe em Java?", List.of("class", "struct", "define", "object"), 0),
                createQuestion("Qual tipo de dado armazena números inteiros?", List.of("int", "double", "String", "boolean"), 1),
                createQuestion("Como se declara um método estático?", List.of("static void metodo()", "void static metodo()", "method static void()", "void metodo static()"), 0),
                createQuestion("Qual é o operador de atribuição em Java?", List.of("=", ":=", "==", "<-"), 0),
                createQuestion("Qual estrutura de controle repete um bloco de código enquanto uma condição é verdadeira?", List.of("while", "if", "switch", "break"), 0)
        );
        // cria o objeto Quiz e injeta a lista de perguntas
        this.quiz = new Quiz();
        this.quiz.setQuestions(questions);
    }

    // método auxiliar para criar perguntas com suas alternativas
    private Question createQuestion(String questionText, List<String> alternativesText, int correctIndex) {

        // define o enunciado da pergunta
        Question question = new Question();
        question.setText(questionText);

        // lista para armazenar alternativas da questão
        List<Alternative> alternatives = new java.util.ArrayList<>();
        for (int i = 0; i < alternativesText.size(); i++) {
            Alternative alt = new Alternative();
            alt.setText(alternativesText.get(i));
            alt.setCorrect(i == correctIndex);

            // marca a alternativa correta comparando o índice
            alternatives.add(alt);
        }

        // associa a lista de alternativas à pergunta
        question.setAlternatives(alternatives);

        // retorna a pergunta criada
        return question;
    }
}
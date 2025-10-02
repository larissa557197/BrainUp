package br.com.fiap.brainup.controller;

import br.com.fiap.brainup.model.Player;

import br.com.fiap.brainup.repository.QuizRepository;
import br.com.fiap.brainup.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;

    public record StartRequest(String playerName) {}
    // public record StartResponse(String playerName) {}
    public record ExitRequest(String playerId) {}

    public ApiController(QuizService quizService, QuizRepository quizRepository) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, UUID>> startQuiz(@RequestBody StartRequest request) {
        if (request.playerName() == null || request.playerName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do jogador não pode ser vazio.");
        }

        Player player = new Player(request.playerName());
        quizRepository.addPlayer(player);

        quizService.sendEventToAdmin("player.joined", player);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("playerId", player.getId()));
    }

    @GetMapping("/stream/admin")
    public SseEmitter streamAdmin() {
        return quizService.createAdminEmitter();
    }

    @PostMapping("/exit")
    public ResponseEntity<Void> exitQuiz(@RequestBody ExitRequest request) {
        UUID playerId;
        try {
            playerId = UUID.fromString(request.playerId());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de jogador inválido.");
        }

        Player player = quizRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));

        player.setActive(false);

        quizService.sendEventToAdmin("player.exited", player);

        return ResponseEntity.ok().build();
    }

}

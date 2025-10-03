package br.com.fiap.brainup.service;

import br.com.fiap.brainup.controller.ApiController;
import br.com.fiap.brainup.model.Player;
import br.com.fiap.brainup.repository.QuizRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// indica que essa classe é um serviço Spring (gerenciado como Bean)
@Service
public class ApiService {

    // dependencia para acessar os dados do quiz (players, etc)
    private final QuizRepository quizRepository;

    // lista thread-safe de conexões abertas via SSE (cada admin conectando ao sistema)
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // construtor com injeção de dependencia do QuizRepository
    public ApiService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }


    //cria uma sala adicionando um jogador
    public ApiController.RoomResponse createRoom(String playerName){
        // cria um novo jogador com o nome recebido
        var player = new Player(playerName);

        //adiciona o jogador ao quiz
        quizRepository.getQuiz().addPlayer(player);

        // notifica todos os admins conectados que um novo player entrou
        sendEventToAdmins("player.joined", player);

        // retorna uma resposta contendo os dados do jogador criado
        return new ApiController.RoomResponse(
                player.getName(),
                player.getId()
        );
    }

    // envia eventos SSE para todos administradores conectados
    public void sendEventToAdmins(String eventName, Object data){

        // lista temporária para guardar conexões quebradas
        List<SseEmitter> deadEmitters = new ArrayList<>();

        // se a conexão estiver morta, guarda para remover depois
        for (SseEmitter emitter : emitters) {
            try{
                // envia o evento com o nome e dados
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (Exception e){
                deadEmitters.add(emitter);
            }
        }
        // remove conexões inválidas da lista
        emitters.removeAll(deadEmitters);
    }

    // cria um novo SSE Emitter (conexão aberta para streaming de eventos)
    public SseEmitter sseEmitter(){

        // 0L = sem timeout (conexão ilimitada)
        var emitter = new SseEmitter(0L);

        // adiciona o emitter à lista global
        emitters.add(emitter);

        // garante que, se a conexão fechar ou der erro, será removida da lista
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        // retorna o emitter para controller
        return emitter;
    }

    // remove um jogador da sala (quando ele sai)
    public void leaveRoom(UUID playerId){

        // busca o player pelo ID, lança erro 404 se não existe
        Player player = quizRepository.getQuiz()
                .getPlayers()
                .stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "jogador não encontrado"
                ));

        // marca o jogador como inativo (não remove de fato da lista)
        player.setActive(false);

        // notifica os admins que o jogador saiu
        sendEventToAdmins("player.exited", player);
    }

}

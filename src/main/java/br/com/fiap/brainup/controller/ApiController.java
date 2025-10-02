package br.com.fiap.brainup.controller;

import br.com.fiap.brainup.service.ApiService;

// anotação do lombok: cria automaticamente um construtor com todos os atribuitos "final"
import lombok.RequiredArgsConstructor;

// imports do Spring: HttpsStatus para status HTTP
// anotações de REST e SseEmitter para streaming de eventos
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


// importa UUID, usando para identificar jogadores de forma única
import java.util.UUID;


// define que essa classe é um controller REST (retorna JSON por padrão)
@RestController
// gera o construtor com dependências injetadas (ApiService)
@RequiredArgsConstructor
public class ApiController {

    // injeção de dependência: ApiService será instanciado pelo Spring
    private final ApiService apiService;

    // records usados para mapear JSON de request/response:

    // - representa o corpo da requisição ao criar uma sala (contém o nome do jogador)
    public record RoomRequest(String playerName){}

    // - representa a requisição para sair da sala (contém o ID único do jogador)
    public record LeaveRoomRequest(UUID playerId){}

    // - representa a resposta ao criar a sala: lista de jogadores e ID do jogador criado
    public record RoomResponse(String players, UUID playerId){}

    //endpoint POST em "/start
    @PostMapping("/start")
    public RoomResponse startRoom(@RequestBody RoomRequest request) {
        // recebe um JSON com playerName, cria a sala chamando o service
       return apiService.createRoom(request.playerName());
    }

    //endpoint GET em "/stram/admin" que cria uma conexão SSE (Server-Sent Events)
    @GetMapping("/stream/admin")
    public SseEmitter streamAdmin() {
        // retorna um emissor de eventos que o frontend pode escutar em tempo real
       return apiService.sseEmitter();
    }

    // endpoint POST em "/exit"
    @PostMapping("/exit")
    // define o status de resposta como 204 (sem conteúdo)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void exitRoom(@RequestBody LeaveRoomRequest request) {
        // remove o jogador da sala usando o serviço
        apiService.leaveRoom(request.playerId());
    }



}

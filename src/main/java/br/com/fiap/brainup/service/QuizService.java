package br.com.fiap.brainup.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuizService {

    private final ConcurrentHashMap<String, SseEmitter> adminEmitters = new ConcurrentHashMap<>();

    public SseEmitter createAdminEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String clientId = "admin-" + System.currentTimeMillis();
        adminEmitters.put(clientId, emitter);

        emitter.onCompletion(() -> adminEmitters.remove(clientId));
        emitter.onTimeout(() -> adminEmitters.remove(clientId));
        emitter.onError((e) -> adminEmitters.remove(clientId));

        return emitter;
    }

    public void sendEventToAdmin(String eventName, Object data) {
        adminEmitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (Exception e) {
                adminEmitters.remove(id);
            }
        });
    }
}

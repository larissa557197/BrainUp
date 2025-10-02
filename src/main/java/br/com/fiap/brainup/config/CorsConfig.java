package br.com.fiap.brainup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // indica que essa classe é uma configuração do Spring Boot
public class CorsConfig implements WebMvcConfigurer {
    // a classe implementa WebMvcConfigurer para costumizar configurações do spring MVC

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // subescreve o método para definir regras de CORS na aplicação

        // define todas as rotas (/**) da aplicação terão as regras abaixo aplicadas
        registry.addMapping("/**")
                // permite apenas requisições vindas desse dominio (frontend local em desenvolvimento
                .allowedOrigins("http://localhost:3000")
                // lista de métodos HTTP que são aceitos nas requisições
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // permite qualquer cabeçalho HTTP nas requisições
                .allowedHeaders("*")
                // permite o envio de credenciais (cookies, headers de autenticação) juntos com a requisição
                .allowCredentials(true);
    }
}

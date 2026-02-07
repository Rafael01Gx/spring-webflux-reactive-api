# Server-Sent Events (SSE)

Server-Sent Events (SSE) é uma tecnologia que permite que um servidor envie atualizações automáticas para um cliente via uma única conexão HTTP. É útil para aplicações que precisam de atualizações contínuas do servidor, como notificações em tempo real, feeds de dados ao vivo ou atualizações de estado de aplicativos.

## Funcionamento

### Conexão unidirecional
Diferente do WebSocket, que é bidirecional, o SSE é unidirecional. O servidor pode enviar dados para o cliente, mas o cliente não pode enviar dados de volta pela mesma conexão.

### Conexão persistente
O cliente abre uma conexão HTTP com o servidor, que permanece aberta, permitindo que o servidor envie eventos sempre que novos dados estiverem disponíveis.

### Formato de dados
Os dados são enviados como texto simples, com eventos separados por duas novas linhas. Cada evento pode conter um ou mais campos de dados.

## Implementação com Spring WebFlux

Confira um exemplo de implementação:

```java
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.stream.Stream;

@RestController
public class SseController {
    
    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamEvents() {
        return Flux.fromStream(Stream.generate(() -> "Evento: " + System.currentTimeMillis()))
                .delayElements(Duration.ofSeconds(1));
    }
}
```

Nesse código, criamos um controlador para lidar com as requisições SSE. Estamos criando um Flux que gera eventos a cada segundo. Cada evento é uma string contendo um timestamp atual.

## Consumindo SSE

Existem diversas maneiras de consumir SSE. Vamos exemplificar de duas formas:

### 1. Diretamente pelo HTML

Usando a API EventSource para receber os eventos:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SSE Example</title>
</head>
<body>
    <div id="events"></div>
    
    <script>
        const eventSource = new EventSource('/sse');
        
        eventSource.onmessage = function(event) {
            const newElement = document.createElement("div");
            newElement.textContent = event.data;
            document.getElementById("events").appendChild(newElement);
        };
    </script>
</body>
</html>
```

### 2. Através de outra aplicação

Utilizando o WebClient:

```java
public void consumirSSE() {
    WebClient client = WebClient.create("http://localhost:8080/sse");
    
    ParameterizedTypeReference<ServerSentEvent<String>> type
            = new ParameterizedTypeReference<ServerSentEvent<String>>() {};
    
    Flux<ServerSentEvent<String>> eventStream = client.get()
            .retrieve()
            .bodyToFlux(type);
    
    eventStream.subscribe(
        content -> logger.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                LocalTime.now(), content.event(), content.id(), content.data()),
        error -> logger.error("Error receiving SSE: {}", error),
        () -> logger.info("Completed!!!"));
}
```

## Benefícios e Limitações

### Benefícios

- **Simplicidade de implementação**: Fácil de configurar e usar
- **Suporte nativo**: Funciona nativamente nos navegadores modernos
- **Escalável**: Para muitos clientes (comparado com WebSockets em alguns casos)

### Limitações

- **Unidirecional**: Apenas o servidor pode enviar dados
- **Suporte limitado**: Navegadores antigos podem não ter suporte
- **Eficiência**: Pode ser menos eficiente que WebSockets para casos de uso bidirecional

## Conclusão

Server-Sent Event é uma ótima escolha para aplicações que necessitam de um fluxo contínuo de dados do servidor para o cliente, com uma implementação simples e eficiente usando Spring WebFlux.
# Testes Assíncronos em Spring

## Introdução

Testes de aplicações Spring assíncronas envolvem técnicas e ferramentas específicas para garantir que as funcionalidades assíncronas, como chamadas de métodos assíncronos, interações reativas e processos em background, funcionem corretamente.

A programação assíncrona é fundamental para construir aplicações modernas, escaláveis e responsivas. No entanto, testar código assíncrono apresenta desafios únicos, pois precisamos lidar com execução não-bloqueante, eventos que ocorrem ao longo do tempo e fluxos de dados reativos.

Este guia apresenta os principais tipos de aplicações assíncronas disponíveis no Spring, bem como ferramentas e bibliotecas úteis para realizar esses testes de forma eficaz.

## Tipos de Aplicações Assíncronas em Spring

### 1. Spring WebFlux

Utilizado para criar aplicações **reativas**, que são altamente escaláveis e eficientes em termos de recursos. O WebFlux trabalha com programação reativa baseada no Project Reactor, permitindo processamento não-bloqueante de requisições.

**Características principais:**
- Modelo de programação reativa
- Suporte para backpressure
- Alta escalabilidade com poucos recursos
- Baseado em Event Loop (não utiliza thread por requisição)

**Exemplo de uso:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.findAll();
    }
    
    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return userService.findById(id);
    }
}
```

### 2. @Async

Permite a execução de métodos assíncronos, utilizando as anotações `@EnableAsync` e `@Async` para facilitar a implementação. Esta abordagem é ideal para operações que podem ser executadas em background sem bloquear a thread principal.

**Características principais:**
- Execução em threads separadas
- Retorno através de `Future`, `CompletableFuture` ou `ListenableFuture`
- Configuração simplificada através de anotações
- Adequado para tarefas independentes

**Exemplo de uso:**
```java
@Service
@EnableAsync
public class EmailService {
    
    @Async
    public CompletableFuture<String> sendEmail(String to, String subject, String body) {
        // Simulando envio de email
        try {
            Thread.sleep(3000);
            return CompletableFuture.completedFuture("Email enviado com sucesso");
        } catch (InterruptedException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
```

### 3. Spring Integration e Spring Batch

Adequados para **integrações complexas** e **processamento em lote**, permitindo a automação de tarefas e fluxos de trabalho assíncronos.

**Spring Integration:**
- Integração entre sistemas
- Padrões Enterprise Integration Patterns (EIP)
- Processamento de mensagens

**Spring Batch:**
- Processamento de grandes volumes de dados
- Jobs e Steps configuráveis
- Suporte a transações e recuperação de falhas

### 4. Spring Messaging

Facilita a interação com sistemas de mensagens como **RabbitMQ** ou **Kafka**, permitindo a comunicação assíncrona entre diferentes componentes do sistema.

**Características principais:**
- Comunicação desacoplada entre serviços
- Garantia de entrega de mensagens
- Suporte a padrões pub/sub
- Escalabilidade horizontal

**Exemplo com RabbitMQ:**
```java
@Component
public class MessageListener {
    
    @RabbitListener(queues = "orders-queue")
    public void handleOrder(Order order) {
        // Processar pedido de forma assíncrona
        orderService.process(order);
    }
}
```

## Bibliotecas e Frameworks para Testes

Para realizar testes assíncronos de forma eficaz, utilizamos as seguintes bibliotecas:

### JUnit 5
A biblioteca de testes padrão para Java, com suporte aprimorado para testes parametrizados, extensões e testes assíncronos.

### Mockito
Para criar mocks e verificar interações entre componentes, essencial para isolar unidades de código durante os testes.

### Reactor Test
Biblioteca específica para testar fluxos reativos do Project Reactor (Mono e Flux).

### Awaitility
Para esperar por condições assíncronas de forma elegante, evitando `Thread.sleep()` e polling manual.

### Spring Boot Test
Suporte de teste integrado para aplicações Spring Boot, incluindo `@SpringBootTest`, `@WebFluxTest`, entre outros.

## Tipos de Teste

### Conceitos Fundamentais do Spring WebFlux

No Spring WebFlux, as operações são **reativas** e **não bloqueantes**, baseadas no padrão publish-subscribe. Os dados são emitidos ao longo do tempo e processados conforme ficam disponíveis.

**Principais conceitos:**
- As operações não esperam por resultados imediatos
- Usam **Mono** (para zero ou um resultado) e **Flux** (para múltiplos resultados) como containers reativos
- Baseado em Event Loop, não em threads por requisição
- Suporte nativo a backpressure

### 1. Testes Unitários

Testam componentes individuais, como serviços ou repositórios, focando na lógica interna isolada de dependências externas.

**Exemplo:**
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository repository;
    
    @InjectMocks
    private UserService service;
    
    @Test
    void shouldReturnUserById() {
        User expectedUser = new User("1", "John Doe");
        when(repository.findById("1")).thenReturn(Mono.just(expectedUser));
        
        Mono<User> result = service.findById("1");
        
        StepVerifier.create(result)
                    .expectNext(expectedUser)
                    .verifyComplete();
    }
}
```

### 2. Testes de Integração

Testam a integração entre componentes ou sistemas, como a comunicação entre o serviço WebFlux e um banco de dados reativo.

**Exemplo:**
```java
@SpringBootTest
@AutoConfigureWebTestClient
class UserIntegrationTest {
    
    @Autowired
    private WebTestClient webTestClient;
    
    @Autowired
    private UserRepository repository;
    
    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }
    
    @Test
    void shouldCreateAndRetrieveUser() {
        User newUser = new User(null, "Jane Doe");
        
        webTestClient.post()
                     .uri("/api/users")
                     .bodyValue(newUser)
                     .exchange()
                     .expectStatus().isCreated()
                     .expectBody(User.class)
                     .value(user -> assertNotNull(user.getId()));
    }
}
```

### 3. Testes de Contrato

Validam que a interface entre serviços de microsserviços permanece consistente ao longo do tempo, evitando quebras de compatibilidade.

**Ferramentas recomendadas:**
- Spring Cloud Contract
- Pact

### 4. Testes de Carga e Performance

Avaliam o desempenho sob carga, especialmente importante para APIs reativas que prometem alta escalabilidade.

**Ferramentas recomendadas:**
- JMeter
- Gatling
- K6

## Ferramentas e Abordagens

### 1. WebTestClient

Ferramenta principal para testar aplicações Spring WebFlux. Simula chamadas HTTP para endpoints reativos e pode verificar respostas de forma assíncrona. Pode ser utilizado em **testes unitários** e **de integração**.

**Características:**
- API fluente e expressiva
- Suporte para verificações complexas
- Integração com MockMvc para testes sem servidor
- Suporte a diferentes formatos de resposta (JSON, XML, etc.)

**Exemplo completo:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetAllProducts() {
        webTestClient.get()
                     .uri("/api/products")
                     .exchange()
                     .expectStatus().isOk()
                     .expectHeader().contentType(MediaType.APPLICATION_JSON)
                     .expectBodyList(Product.class)
                     .hasSize(10);
    }
    
    @Test
    void testGetProductById() {
        Product expectedProduct = new Product("1", "Laptop", 999.99);
        
        webTestClient.get()
                     .uri("/api/products/{id}", "1")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody(Product.class)
                     .isEqualTo(expectedProduct);
    }
    
    @Test
    void testCreateProduct() {
        Product newProduct = new Product(null, "Mouse", 29.99);
        
        webTestClient.post()
                     .uri("/api/products")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(newProduct)
                     .exchange()
                     .expectStatus().isCreated()
                     .expectBody(Product.class)
                     .value(product -> {
                         assertNotNull(product.getId());
                         assertEquals("Mouse", product.getName());
                     });
    }
    
    @Test
    void testProductNotFound() {
        webTestClient.get()
                     .uri("/api/products/{id}", "999")
                     .exchange()
                     .expectStatus().isNotFound();
    }
}
```

### 2. StepVerifier

Verifica o comportamento de **Mono** e **Flux** ao longo do tempo, permitindo que você garanta o resultado do fluxo de dados emitido. É a ferramenta recomendada para **testes unitários** de serviços ou componentes que retornam Mono ou Flux.

**Características:**
- Verificação de elementos emitidos
- Teste de erros e completude
- Suporte a operadores temporais
- Verificação de delays e timing

**Exemplos avançados:**
```java
@Test
void testServiceMethodWithMultipleElements() {
    Flux<String> flux = service.getDataFlux();

    StepVerifier.create(flux)
                .expectNext("data1", "data2", "data3")
                .expectComplete()
                .verify();
}

@Test
void testServiceMethodWithError() {
    Mono<String> mono = service.getDataWithError();

    StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
}

@Test
void testServiceMethodWithDelay() {
    Flux<Long> flux = Flux.interval(Duration.ofSeconds(1)).take(3);

    StepVerifier.create(flux)
                .expectNext(0L, 1L, 2L)
                .expectComplete()
                .verify(Duration.ofSeconds(5));
}

@Test
void testServiceMethodWithConditionalExpectation() {
    Flux<Integer> flux = service.getNumbers();

    StepVerifier.create(flux)
                .expectNextMatches(n -> n > 0)
                .expectNextMatches(n -> n % 2 == 0)
                .expectNextCount(3)
                .expectComplete()
                .verify();
}

@Test
void testServiceMethodWithSubscription() {
    Flux<String> flux = service.getDataFlux();

    StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("data1")
                .expectNext("data2")
                .expectComplete()
                .verify();
}
```

### 3. TestPublisher

Uma ferramenta do projeto Reactor para testar código que consome Flux ou Mono, permitindo que você **controle o fluxo de eventos** emitidos para garantir que o consumidor responda corretamente. Simula a emissão de dados para Flux e Mono durante o teste.

**Casos de uso:**
- Simular diferentes cenários de emissão de dados
- Testar comportamento com erros controlados
- Validar backpressure
- Testar completion e cancelamento

**Exemplos avançados:**
```java
@Test
void testWithTestPublisher() {
    TestPublisher<String> publisher = TestPublisher.create();

    Flux<String> flux = publisher.flux();

    StepVerifier.create(flux)
                .then(() -> publisher.emit("data1", "data2"))
                .expectNext("data1", "data2")
                .expectComplete()
                .verify();
}

@Test
void testErrorScenario() {
    TestPublisher<String> publisher = TestPublisher.create();

    Flux<String> flux = publisher.flux();

    StepVerifier.create(flux)
                .then(() -> publisher.next("data1"))
                .expectNext("data1")
                .then(() -> publisher.error(new RuntimeException("Erro simulado")))
                .expectError(RuntimeException.class)
                .verify();
}

@Test
void testNonCompliantPublisher() {
    TestPublisher<String> publisher = TestPublisher.createNoncompliant(
        TestPublisher.Violation.ALLOW_NULL
    );

    Flux<String> flux = publisher.flux();

    StepVerifier.create(flux)
                .then(() -> publisher.next("data1", null, "data3"))
                .expectNext("data1", null, "data3")
                .expectComplete()
                .verify();
}
```

## Testando Métodos @Async

Para testar métodos anotados com `@Async`, utilizamos abordagens específicas:

### Usando CompletableFuture

```java
@SpringBootTest
class EmailServiceTest {
    
    @Autowired
    private EmailService emailService;
    
    @Test
    void testAsyncEmailSending() throws Exception {
        CompletableFuture<String> future = emailService.sendEmail(
            "user@example.com",
            "Test Subject",
            "Test Body"
        );
        
        String result = future.get(5, TimeUnit.SECONDS);
        assertEquals("Email enviado com sucesso", result);
    }
}
```

### Usando Awaitility

```java
@Test
void testAsyncMethodWithAwaitility() {
    emailService.sendEmailAsync("user@example.com", "Subject", "Body");
    
    await().atMost(5, TimeUnit.SECONDS)
           .untilAsserted(() -> {
               verify(emailRepository).save(any(EmailLog.class));
           });
}
```

## Testando Mensageria

### Testando RabbitMQ

```java
@SpringBootTest
@ExtendWith(SpringExtension.class)
class RabbitMQListenerTest {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private OrderService orderService;
    
    @Test
    void testOrderMessageProcessing() {
        Order order = new Order("123", "Product A", 2);
        
        rabbitTemplate.convertAndSend("orders-queue", order);
        
        await().atMost(5, TimeUnit.SECONDS)
               .untilAsserted(() -> {
                   verify(orderService).process(order);
               });
    }
}
```

### Testando Kafka com Testcontainers

```java
@SpringBootTest
@Testcontainers
class KafkaListenerTest {
    
    @Container
    static KafkaContainer kafka = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:latest")
    );
    
    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Test
    void testKafkaMessageConsumption() {
        kafkaTemplate.send("test-topic", "test-message");
        
        await().atMost(10, TimeUnit.SECONDS)
               .untilAsserted(() -> {
                   // Verificar que a mensagem foi processada
               });
    }
}
```

## Boas Práticas

### 1. Evite o uso excessivo de block() e subscribe() em testes

`block()` e `subscribe()` forçam a execução síncrona e bloqueante, o que vai contra a natureza reativa. Use **StepVerifier** e **WebTestClient** para validar o comportamento reativo.

**❌ Evite:**
```java
@Test
void badTest() {
    Mono<String> result = service.getData();
    String value = result.block(); // Não recomendado!
    assertEquals("expected", value);
}
```

**✅ Prefira:**
```java
@Test
void goodTest() {
    Mono<String> result = service.getData();
    
    StepVerifier.create(result)
                .expectNext("expected")
                .verifyComplete();
}
```

### 2. Teste comportamentos de retentativa e backpressure

Verifique se a aplicação lida corretamente com fluxos que falham ou são lentos. Teste a lógica de retentativa (`retry`) e backpressure (`onBackpressureBuffer`, `onBackpressureDrop`).

**Exemplo de teste de retry:**
```java
@Test
void testRetryLogic() {
    AtomicInteger attempts = new AtomicInteger(0);
    
    Mono<String> mono = Mono.defer(() -> {
        if (attempts.incrementAndGet() < 3) {
            return Mono.error(new RuntimeException("Falha temporária"));
        }
        return Mono.just("Sucesso");
    }).retry(3);
    
    StepVerifier.create(mono)
                .expectNext("Sucesso")
                .verifyComplete();
    
    assertEquals(3, attempts.get());
}
```

**Exemplo de teste de backpressure:**
```java
@Test
void testBackpressure() {
    Flux<Integer> flux = Flux.range(1, 100)
                             .onBackpressureBuffer(10);
    
    StepVerifier.create(flux, 5)
                .expectNextCount(5)
                .thenRequest(10)
                .expectNextCount(10)
                .thenCancel()
                .verify();
}
```

### 3. Use contextos de teste para configuração e isolamento

Crie contextos específicos para cada tipo de teste (unitário, integração) para garantir que os testes sejam isolados e não interfiram uns com os outros.

**Exemplo:**
```java
@TestConfiguration
static class TestConfig {
    
    @Bean
    @Primary
    public UserRepository mockUserRepository() {
        return Mockito.mock(UserRepository.class);
    }
}
```

### 4. Simule cenários de falha

Teste como a aplicação responde a falhas de rede, indisponibilidade de serviços externos, ou dados inesperados. O uso de **TestPublisher** e **StepVerifier** é ideal para isso.

**Exemplo:**
```java
@Test
void testNetworkFailure() {
    when(externalService.call())
        .thenReturn(Mono.error(new WebClientException("Network error")));
    
    Mono<String> result = service.processData();
    
    StepVerifier.create(result)
                .expectErrorMatches(e -> 
                    e instanceof WebClientException && 
                    e.getMessage().contains("Network error")
                )
                .verify();
}
```

### 5. Monitoramento e tempo limite nos testes

Defina um tempo limite nos testes usando `.verify(Duration.ofSeconds(5))` para evitar que testes assíncronos perdurem indefinidamente.

**Exemplo:**
```java
@Test
void testWithTimeout() {
    Flux<Long> flux = Flux.interval(Duration.ofMillis(100)).take(10);
    
    StepVerifier.create(flux)
                .expectNextCount(10)
                .expectComplete()
                .verify(Duration.ofSeconds(2)); // Timeout de 2 segundos
}
```

### 6. Teste casos extremos (Edge Cases)

**Exemplo:**
```java
@Test
void testEmptyFlux() {
    Flux<String> emptyFlux = service.getEmptyData();
    
    StepVerifier.create(emptyFlux)
                .expectComplete()
                .verify();
}

@Test
void testNullHandling() {
    Mono<String> mono = service.processNullable(null);
    
    StepVerifier.create(mono)
                .expectComplete()
                .verify();
}
```

### 7. Use @DirtiesContext com cuidado

A anotação `@DirtiesContext` recarrega o contexto Spring, o que pode tornar os testes lentos. Use apenas quando necessário.

```java
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class HeavyIntegrationTest {
    // Use apenas quando realmente necessário
}
```

## Exemplo Completo: Aplicação de E-commerce

### Service
```java
@Service
public class OrderService {
    
    private final OrderRepository repository;
    private final InventoryService inventoryService;
    
    public Mono<Order> createOrder(Order order) {
        return inventoryService.checkAvailability(order.getProductId())
                .filter(available -> available)
                .switchIfEmpty(Mono.error(new ProductUnavailableException()))
                .then(repository.save(order))
                .delayElement(Duration.ofMillis(100));
    }
    
    public Flux<Order> findOrdersByUserId(String userId) {
        return repository.findByUserId(userId)
                .timeout(Duration.ofSeconds(5));
    }
}
```

### Testes
```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock
    private OrderRepository repository;
    
    @Mock
    private InventoryService inventoryService;
    
    @InjectMocks
    private OrderService orderService;
    
    @Test
    void shouldCreateOrderSuccessfully() {
        Order order = new Order("1", "user1", "product1", 2);
        
        when(inventoryService.checkAvailability("product1"))
            .thenReturn(Mono.just(true));
        when(repository.save(any(Order.class)))
            .thenReturn(Mono.just(order));
        
        StepVerifier.create(orderService.createOrder(order))
                    .expectNext(order)
                    .verifyComplete();
    }
    
    @Test
    void shouldFailWhenProductUnavailable() {
        Order order = new Order("1", "user1", "product1", 2);
        
        when(inventoryService.checkAvailability("product1"))
            .thenReturn(Mono.just(false));
        
        StepVerifier.create(orderService.createOrder(order))
                    .expectError(ProductUnavailableException.class)
                    .verify();
    }
    
    @Test
    void shouldHandleTimeout() {
        when(repository.findByUserId("user1"))
            .thenReturn(Flux.never()); // Nunca emite dados
        
        StepVerifier.create(orderService.findOrdersByUserId("user1"))
                    .expectError(TimeoutException.class)
                    .verify(Duration.ofSeconds(10));
    }
}
```

## Recursos Adicionais

### Documentação Oficial
- [Spring WebFlux Testing](https://docs.spring.io/spring-framework/reference/testing/webtestclient.html)
- [Project Reactor Testing](https://projectreactor.io/docs/core/release/reference/#testing)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

### Ferramentas Úteis
- **Testcontainers**: Para testes de integração com bancos de dados e mensageria
- **WireMock**: Para mockar APIs externas
- **AssertJ**: Para assertions mais expressivas

## Conclusão

Testes assíncronos em aplicações Spring requerem uma abordagem diferente dos testes tradicionais síncronos. As ferramentas fornecidas pelo ecossistema Spring e Project Reactor, como **StepVerifier**, **WebTestClient** e **TestPublisher**, tornam esse processo mais simples e eficaz.

Ao seguir as boas práticas apresentadas neste guia, você será capaz de criar testes robustos, confiáveis e que verdadeiramente validam o comportamento assíncrono e reativo de suas aplicações.

**Pontos-chave para lembrar:**
- Use StepVerifier para testar Mono e Flux
- Use WebTestClient para testar endpoints reativos
- Evite block() e subscribe() em testes
- Teste cenários de falha e comportamentos temporais
- Defina timeouts apropriados
- Mantenha os testes isolados e independentes
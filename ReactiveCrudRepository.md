## ReactiveCrudRepository

`ReactiveCrudRepository` é parte do Spring Data Reactive, proporcionando operações CRUD de maneira reativa. É especialmente útil para aplicações que precisam de alta escalabilidade e baixa latência.

### Principais métodos

*   `Mono<S> save(S entity)`: Salva uma entidade.
*   `Flux<S> saveAll(Iterable<S> entities)`: Salva múltiplas entidades.
*   `Mono<T> findById(ID id)`: Encontra uma entidade pelo seu ID.
*   `Mono<Boolean> existsById(ID id)`: Verifica se uma entidade existe pelo seu ID.
*   `Flux<T> findAll()`: Retorna todas as entidades.
*   `Flux<T> findAllById(Iterable<ID> ids)`: Retorna todas as entidades com os IDs fornecidos.
*   `Mono<Long> count()`: Retorna o número de entidades.
*   `Mono<Void> deleteById(ID id)`: Deleta uma entidade pelo seu ID.
*   `Mono<Void> delete(T entity)`: Deleta uma entidade.
*   `Mono<Void> deleteAll(Iterable<? extends T> entities)`: Deleta múltiplas entidades.
*   `Mono<Void> deleteAll()`: Deleta todas as entidades.

### Exemplo de uso

```java
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Flux<User> findByLastName(String lastName);
}
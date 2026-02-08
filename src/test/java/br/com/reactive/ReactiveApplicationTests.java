package br.com.reactive;

import br.com.reactive.application.EventoSinkService;
import br.com.reactive.domain.evento.EventoDto;
import br.com.reactive.domain.evento.TipoEvento;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ReactiveApplicationTests {

    @Autowired
    private WebTestClient webTestClient;
    @Mock
    private EventoSinkService eventoSinkService;

	@Test
	void cadastraNovoEvento() {
        EventoDto dto = new EventoDto(
                null,
                TipoEvento.SHOW,"Aerosmith",
                LocalDate.parse("2026-02-20"),
                10,
                "Aerosmith é uma banda norte-americana de rock, formada em Boston em 1970.");

        webTestClient.post().uri("/eventos").bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(EventoDto.class).value(res -> {
                    assertNotNull(res.id());
                    assertEquals(dto.tipo(), res.tipo());
                    assertEquals(dto.data(), res.data());
                    assertEquals(dto.nome(), res.nome());
                    assertEquals(dto.descricao(), res.descricao());
                    assertEquals(dto.qntIngressos(), res.qntIngressos());
                });
    }
    @Test
    void buscarEvento() {
        EventoDto dto = new EventoDto(
                14L,
                TipoEvento.WORKSHOP,"Café com Ideias",
                LocalDate.parse("2025-01-25"),
                10,
                "Um evento que ensina técnicas de brainstorming e priorização para alavancar projetos.");

        webTestClient.get().uri("/eventos")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(EventoDto.class).value(res -> {
                    EventoDto evRes = res.get(13);
                    assertEquals(dto.id(), evRes.id());
                    assertEquals(dto.tipo(), evRes.tipo());
                    assertEquals(dto.data(), evRes.data());
                    assertEquals(dto.nome(), evRes.nome());
                    assertEquals(dto.descricao(), evRes.descricao());
                    assertEquals(dto.qntIngressos(), evRes.qntIngressos());
                });
    }

}

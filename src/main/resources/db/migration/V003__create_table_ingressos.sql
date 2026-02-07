CREATE TABLE ingressos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(200) UNIQUE,
    evento_id BIGINT,

    CONSTRAINT fk_ingressos_eventos FOREIGN KEY (evento_id) REFERENCES eventos(id)
);
package br.com.reactive.infra.exeptions;

public class TicketUnavailable extends RuntimeException {
    public TicketUnavailable(String message) {
        super(message);
    }
}

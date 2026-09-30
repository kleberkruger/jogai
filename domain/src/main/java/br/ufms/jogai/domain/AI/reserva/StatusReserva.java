package br.ufms.jogai.domain.AI.reserva;

public enum StatusReserva {

    ATIVA("Reserva aguardando atendimento na fila do jogo"),
    ATENDIDA("Reserva atendida e vinculada a um exemplar"),
    CANCELADA("Reserva cancelada antes do atendimento"),
    EXPIRADA("Reserva encerrada por expiração antes do atendimento");

    private final String descricao;

    StatusReserva(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return descricao;
    }
}

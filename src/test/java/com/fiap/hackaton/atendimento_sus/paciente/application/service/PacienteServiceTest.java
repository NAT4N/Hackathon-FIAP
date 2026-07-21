package com.fiap.hackaton.atendimento_sus.paciente.application.service;

import com.fiap.hackaton.atendimento_sus.paciente.application.port.in.CadastrarPacienteUseCase.CadastrarPacienteCommand;
import com.fiap.hackaton.atendimento_sus.paciente.application.port.out.PacienteRepositoryPort;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Paciente;
import com.fiap.hackaton.atendimento_sus.paciente.domain.model.Sexo;
import com.fiap.hackaton.atendimento_sus.shared.exception.ConflictException;
import com.fiap.hackaton.atendimento_sus.shared.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock PacienteRepositoryPort pacienteRepository;
    private final Clock clock = Clock.fixed(Instant.parse("2026-07-04T12:00:00Z"), ZoneOffset.UTC);

    private PacienteService service() {
        return new PacienteService(pacienteRepository, clock);
    }

    private CadastrarPacienteCommand comando() {
        return new CadastrarPacienteCommand("João", "52998224725",
                LocalDate.of(1990, 5, 20), Sexo.MASCULINO, "11999998888", null);
    }

    @Test
    void cadastraPacienteNovo() {
        when(pacienteRepository.existePorCpf(any())).thenReturn(false);
        when(pacienteRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Paciente p = service().cadastrar(comando());

        assertThat(p.getNome()).isEqualTo("João");
        assertThat(p.getCpf().numero()).isEqualTo("52998224725");
    }

    @Test
    void recusaCpfDuplicado() {
        when(pacienteRepository.existePorCpf(any())).thenReturn(true);
        assertThatThrownBy(() -> service().cadastrar(comando()))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void buscaInexistenteLancaNotFound() {
        UUID id = UUID.randomUUID();
        when(pacienteRepository.buscarPorId(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service().buscarPorId(id))
                .isInstanceOf(NotFoundException.class);
    }
}

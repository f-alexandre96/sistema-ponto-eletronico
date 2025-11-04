package com.exemplo.ponto.service;

import com.exemplo.ponto.model.Funcionario;
import com.exemplo.ponto.model.Ponto;
import com.exemplo.ponto.repository.FuncionarioRepository;
import com.exemplo.ponto.repository.PontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class PontoService {

    @Autowired
    private PontoRepository pontoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private final int MAX_ATRASOS = 3; // Máximo de atrasos na semana

    public String registrarPonto(Long funcionarioId) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        // Verifica se está bloqueado
        if (funcionario.isBloqueado()) {
            return "BLOQUEADO: Consulte o RH para desbloquear seu acesso";
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalTime horaAtual = agora.toLocalTime();

        // Converte horário de entrada esperado
        String[] partes = funcionario.getHorarioEntrada().split(":");
        LocalTime horarioEsperado = LocalTime.of(
                Integer.parseInt(partes[0]),
                Integer.parseInt(partes[1])
        );

        // Verifica se está atrasado
        boolean atrasado = horaAtual.isAfter(horarioEsperado);

        // Salva o ponto
        Ponto ponto = new Ponto(funcionario, agora, atrasado);
        pontoRepository.save(ponto);

        // Verifica atrasos na semana
        if (atrasado) {
            int atrasosNaSemana = contarAtrasosNaSemana(funcionario);

            if (atrasosNaSemana >= MAX_ATRASOS) {
                funcionario.setBloqueado(true);
                funcionarioRepository.save(funcionario);
                return "ATENÇÃO: Você excedeu o limite de atrasos. " +
                        "Seu acesso foi bloqueado. Consulte o RH.";
            }

            return "Ponto registrado COM ATRASO às " +
                    agora.toLocalTime() + ". Atrasos na semana: " + atrasosNaSemana;
        }

        return "Ponto registrado com sucesso às " + agora.toLocalTime();
    }

    private int contarAtrasosNaSemana(Funcionario funcionario) {
        LocalDateTime hoje = LocalDateTime.now();

        // Início da semana (segunda-feira)
        LocalDateTime inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(
                DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();

        // Fim da semana (domingo)
        LocalDateTime fimSemana = hoje.with(TemporalAdjusters.nextOrSame(
                DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        List<Ponto> pontos = pontoRepository.findByFuncionarioAndDataHoraBetween(
                funcionario, inicioSemana, fimSemana
        );

        return (int) pontos.stream().filter(Ponto::isAtrasado).count();
    }

    public void desbloquearFuncionario(Long funcionarioId) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        funcionario.setBloqueado(false);
        funcionarioRepository.save(funcionario);
    }

    public List<Ponto> listarPontosFuncionario(Long funcionarioId) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        return pontoRepository.findByFuncionario(funcionario);
    }
}

package com.exemplo.ponto.repository;

import com.exemplo.ponto.model.Funcionario;
import com.exemplo.ponto.model.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PontoRepository extends JpaRepository<Ponto, Long> {
    List<Ponto> findByFuncionarioAndDataHoraBetween(
            Funcionario funcionario,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<Ponto> findByFuncionario(Funcionario funcionario);
}

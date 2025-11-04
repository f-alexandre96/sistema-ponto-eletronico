package com.exemplo.ponto.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Funcionario funcionario;

    private LocalDateTime dataHora;
    private boolean atrasado;

    public Ponto() {}

    public Ponto(Funcionario funcionario, LocalDateTime dataHora, boolean atrasado) {
        this.funcionario = funcionario;
        this.dataHora = dataHora;
        this.atrasado = atrasado;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public boolean isAtrasado() { return atrasado; }
    public void setAtrasado(boolean atrasado) { this.atrasado = atrasado; }
}

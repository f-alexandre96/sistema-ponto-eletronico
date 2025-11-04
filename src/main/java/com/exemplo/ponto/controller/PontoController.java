package com.exemplo.ponto.controller;

import com.exemplo.ponto.model.Funcionario;
import com.exemplo.ponto.model.Ponto;
import com.exemplo.ponto.repository.FuncionarioRepository;
import com.exemplo.ponto.service.PontoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PontoController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PontoService pontoService;

    // Criar funcionário
    @PostMapping("/funcionarios")
    @Operation(summary = "cria funcionario")
    public Funcionario criarFuncionario(@RequestBody Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    // Listar todos os funcionários
    @GetMapping("/funcionarios")
    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.findAll();
    }

    // Registrar ponto
    @PostMapping("/pontos/{funcionarioId}")
    @Operation(summary = "registra ponto funcionario")
    public String registrarPonto(@PathVariable Long funcionarioId) {
        return pontoService.registrarPonto(funcionarioId);
    }

    // Desbloquear funcionário (RH)
    @PutMapping("/funcionarios/{funcionarioId}/desbloquear")
    public String desbloquear(@PathVariable Long funcionarioId) {
        pontoService.desbloquearFuncionario(funcionarioId);
        return "Funcionário desbloqueado com sucesso";
    }

    // Listar pontos de um funcionário (RH)
    @GetMapping("/funcionarios/{funcionarioId}/pontos")
    public List<Ponto> listarPontos(@PathVariable Long funcionarioId) {
        return pontoService.listarPontosFuncionario(funcionarioId);
    }
}
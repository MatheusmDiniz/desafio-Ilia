package com.desafio.ilia.service;

import com.desafio.ilia.service.dto.RelatorioDTO;
import com.desafio.ilia.service.exeption.ExceptionRelatorioNaoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class RelatorioService {

    @Autowired
    ControleDiarioService controleDiarioService;

    @Autowired
    RegistroService registroService;

    @Autowired
    AlocacoesService alocacoesService;

    public RelatorioService(ControleDiarioService controleDiarioService, RegistroService registroService, AlocacoesService alocacoesService) {
        this.controleDiarioService = controleDiarioService;
        this.registroService = registroService;
        this.alocacoesService = alocacoesService;
    }

    public RelatorioDTO getFolhasPonto(String mes) {
        LocalDate dataInicial = LocalDate.parse(mes+"-01");
        LocalDate dataFinal = LocalDate.parse(mes+"-"+ dataInicial.lengthOfMonth());

        if(!registroService.validarRegistros(dataInicial, dataFinal)){
            throw new ExceptionRelatorioNaoEncontrado("Relatório não encontrado");
        }

        RelatorioDTO relatorioDTO = new RelatorioDTO();
        relatorioDTO.setMes(mes);
        relatorioDTO.setHorasTrabalhadas(controleDiarioService.pegarHorasTrabalhadasMes(dataInicial, dataFinal));
        relatorioDTO.setHorasExcedentes(controleDiarioService.pegarHorasExcedentes(relatorioDTO.getHorasTrabalhadas()));
        relatorioDTO.setHorasDevidas(controleDiarioService.pegarHorasDevidas(relatorioDTO.getHorasTrabalhadas()));

        relatorioDTO.setRegistros(new ArrayList<>(registroService.pegarRegistrosRelatorio(dataInicial, dataFinal)));

        relatorioDTO.setAlocacoes(new ArrayList<>(alocacoesService.pegarRegistrosAlocacoes(dataInicial, dataFinal)));


        return relatorioDTO;
    }
}

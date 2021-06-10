package com.desafio.ilia.service;

import com.desafio.ilia.entity.ControleDiario;
import com.desafio.ilia.entity.Registro;
import com.desafio.ilia.service.dto.RegistroDTO;
import com.desafio.ilia.service.dto.RelatorioDTO;
import com.desafio.ilia.service.exeption.ExceptionRelatorioNaoEncontrado;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RelatorioServiceTest {
    private RelatorioService relatorioService;

    @MockBean
    private ControleDiarioService controleDiarioService;

    @MockBean
    private RegistroService registroService;

    @MockBean
    private AlocacoesService alocacoesService;



    @Before
    public void setUp(){
        relatorioService = new RelatorioService(controleDiarioService, registroService, alocacoesService);
    }

    @Test
    public void deveGetFolhasPonto() {
        when(registroService.validarRegistros(any(), any())).thenReturn(true);
        when(controleDiarioService.pegarHorasTrabalhadasMes(any(), any())).thenReturn("PT10H30M50S");
        when(controleDiarioService.pegarHorasExcedentes(any())).thenReturn("PT0S");
        when(controleDiarioService.pegarHorasDevidas(any())).thenReturn("PT189H29M10S");
        when(registroService.pegarRegistrosRelatorio(any(), any())).thenReturn(new ArrayList<>());
        when(alocacoesService.pegarRegistrosAlocacoes(any(), any())).thenReturn(new ArrayList<>());

        RelatorioDTO relatorioDTO = relatorioService.getFolhasPonto("2018-06");

        Assertions.assertThat(relatorioDTO).isNotNull();
    }

    @Test(expected = ExceptionRelatorioNaoEncontrado.class)
    public void deveGetFolhasPontoRelatorioNaoEncontrado() {
        when(registroService.validarRegistros(any(), any())).thenReturn(false);
        RelatorioDTO relatorioDTO = relatorioService.getFolhasPonto("2018-06");
    }
}

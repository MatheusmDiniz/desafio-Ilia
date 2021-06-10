package com.desafio.ilia.service;

import com.desafio.ilia.entity.Alocacao;
import com.desafio.ilia.repository.AlocalcaoRepository;
import com.desafio.ilia.repository.RegistroRepository;
import com.desafio.ilia.service.dto.AlocacaoDTO;
import com.desafio.ilia.service.dto.AlocacoesRelatoriosDTO;
import com.desafio.ilia.service.exeption.ExceptionAlocarHorasAposConcluirDia;
import com.desafio.ilia.service.exeption.ExceptionAlocarTempoMaiorTrabalhado;
import com.desafio.ilia.service.exeption.ExeptionTempoInvalido;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AlocacoesServiceTest {

    private AlocacoesService alocacoesService;

    @MockBean
    private RegistroRepository registroRepository;

    @MockBean
    private AlocalcaoRepository alocalcaoRepository;

    @Before
    public void setUp(){
        alocacoesService = new AlocacoesService(registroRepository,alocalcaoRepository);
    }

    @Test
    public void deveRegistraAlocacao() {
        AlocacaoDTO alocacaoDTO = AlocacaoDTO.builder()
                .dia("2021-06-09")
                .tempo("PT00H30M00S")
                .nomeProjeto("ACME Corporation")
                .build();


        List<LocalTime> ts = Arrays.asList(LocalTime.parse("08:00:00"), LocalTime.parse("13:00:00"), LocalTime.parse("14:00:00"), LocalTime.parse("20:00:00"));
        when(registroRepository.getHoraByData(any())).thenReturn(ts);

        List<String> listaString = Arrays.asList("PT1H00M0S", "PT2H00M0S");
        when(alocalcaoRepository.getTemposByData(any())).thenReturn(listaString);
        when(alocalcaoRepository.save(any())).thenReturn(new Alocacao());

        alocacoesService.registroAlocacao(alocacaoDTO);

        verify(alocalcaoRepository,times(1)).save(any());
    }

    @Test(expected = ExeptionTempoInvalido.class)
    public void deveRegistraAlocacaoRetormarErroTempoInvalido() {
        AlocacaoDTO alocacaoDTO = AlocacaoDTO.builder()
                .dia("2021-06-09")
                .tempo("aerefa")
                .nomeProjeto("ACME Corporation")
                .build();
        List<LocalTime> ts = Arrays.asList(LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"), LocalTime.parse("14:00:00"), LocalTime.parse("15:00:00"));
        when(registroRepository.getHoraByData(any())).thenReturn(ts);

        alocacoesService.registroAlocacao(alocacaoDTO);

        verify(alocalcaoRepository,times(1)).save(any());
    }

    @Test(expected = ExceptionAlocarTempoMaiorTrabalhado.class)
    public void deveRegistraAlocacaoRetormarErroTempoMaiorQueTrabalhadoDia() {
        AlocacaoDTO alocacaoDTO = AlocacaoDTO.builder()
                .dia("2021-06-09")
                .tempo("PT00H30M00S")
                .nomeProjeto("ACME Corporation")
                .build();


        List<LocalTime> ts = Arrays.asList(LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"), LocalTime.parse("14:00:00"), LocalTime.parse("15:00:00"));
        when(registroRepository.getHoraByData(any())).thenReturn(ts);

        List<String> listaString = Arrays.asList("PT2H00M0S", "PT2H00M0S");
        when(alocalcaoRepository.getTemposByData(any())).thenReturn(listaString);

        alocacoesService.registroAlocacao(alocacaoDTO);

        verify(alocalcaoRepository,times(1)).save(any());
    }

    @Test(expected = ExceptionAlocarHorasAposConcluirDia.class)
    public void deveRegistraAlocacaoRetornarErroConcluirTodasBatidas() {
        AlocacaoDTO alocacaoDTO = AlocacaoDTO.builder()
                .dia("2021-06-09")
                .tempo("PT00H30M00S")
                .nomeProjeto("ACME Corporation")
                .build();


        List<LocalTime> ts = Arrays.asList(LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"));
        when(registroRepository.getHoraByData(any())).thenReturn(ts);

        alocacoesService.registroAlocacao(alocacaoDTO);

        verify(alocalcaoRepository,times(1)).save(any());
    }

    @Test()
    public void devePegarRegistrosAlocacoes(){
        Alocacao alocacao = Alocacao.builder()
                .id(1)
                .dia(LocalDate.parse("2021-06-09"))
                .tempo("PT00H30M00S")
                .nomeProjeto("ACME Corporation")
                .build();

        when(alocalcaoRepository.findAllByDiaBetween(any(), any())).thenReturn(Arrays.asList(alocacao,alocacao, alocacao));
        List<AlocacoesRelatoriosDTO> alocacoesRelatoriosDTOS = alocacoesService.pegarRegistrosAlocacoes(LocalDate.parse("2021-06-01"), LocalDate.parse("2021-06-30"));
        Assertions.assertThat(alocacoesRelatoriosDTOS).hasSize(1);
    }
}

package com.desafio.ilia.service;


import com.desafio.ilia.entity.ControleDiario;
import com.desafio.ilia.entity.Registro;
import com.desafio.ilia.repository.ControleDiarioRepository;
import com.desafio.ilia.repository.RegistroRepository;
import com.desafio.ilia.service.dto.RegistroDTO;
import com.desafio.ilia.service.dto.RegistrosRelatoriosDTO;
import com.desafio.ilia.service.exeption.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RegistroServiceTest {

    private RegistroService registroService;

    @MockBean
    private RegistroRepository registroRepository;

    @MockBean
    private ControleDiarioService controleDiarioService;

    @MockBean
    private ControleDiarioRepository controleDiarioRepository;

    @Before
    public void setUp(){
        registroService = new RegistroService(registroRepository, controleDiarioService);
    }


    @Test
    public void deveRegistroPonto() {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .dataHora("2021-06-09T12:54:00")
                .build();;

        when(registroRepository.findByDataAndHora(any(), any())).thenReturn(new ArrayList<>());
        when(registroRepository.getHoraByData(any())).thenReturn(new ArrayList<>());
        when(registroRepository.save(any())).thenReturn(new Registro());
        when(registroRepository.getHoraByData(any())).thenReturn(new ArrayList<>());
        when(controleDiarioRepository.findByDia(any())).thenReturn(Optional.of(new ControleDiario()));
        when(controleDiarioRepository.save(any())).thenReturn(new ArrayList<>());

        registroService.registroPonto(registroDTO);

        verify(registroRepository,times(1)).save(any());
    }

    @Test(expected = ExceptionQuatroBatidas.class)
    public void deveRegistroPontoRetornarErroComMaisQuatroBatidas(){
        RegistroDTO registroDTO = RegistroDTO.builder()
                .dataHora("2021-06-09T12:00:00")
                .build();;

        when(registroRepository.countBatidasDiaByData(any())).thenReturn(4);
        registroService.registroPonto(registroDTO);
    }

    @Test(expected = ExceptionMinimoHoraAlmoco.class)
    public void deveRegistroPontoValidarHoraRetornarErroAlmocoMenorUmaHora() {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .dataHora("2021-06-09T12:00:00")
                .build();;

        when(registroRepository.countBatidasDiaByData(any())).thenReturn(2);
        when(registroRepository.findLastBatidasDia(any())).thenReturn(LocalTime.parse("11:10:00"));
        registroService.registroPonto(registroDTO);
    }

    @Test(expected = ExceptionDataHoraInvalida.class)
    public void deveValidarFormatoDataHoraRetornarErro(){
        RegistroDTO registroDTO = RegistroDTO.builder()
                .dataHora("2021-06-09T100:00:00")
                .build();;

        registroService.registroPonto(registroDTO);
    }

    @Test()
    public void devePegarRegistrosRelatorio(){
        Registro registro = Registro.builder()
                .id(1)
                .data(LocalDate.parse("2021-06-09"))
                .hora(LocalTime.parse("10:00:00"))
                .batidasDia(2)
                .build();

        when(registroRepository.findAllByDataBetween(any(), any())).thenReturn(Arrays.asList(registro,registro, registro));
        List<RegistrosRelatoriosDTO> registrosRelatoriosDTOS = registroService.pegarRegistrosRelatorio(LocalDate.parse("2021-06-01"), LocalDate.parse("2021-06-30"));
        Assertions.assertThat(registrosRelatoriosDTOS).hasSize(1);
    }

    @Test()
    public void deveValidarRegistrosVerdadeiro(){
        Registro registro = Registro.builder()
                .id(1)
                .data(LocalDate.parse("2021-06-09"))
                .hora(LocalTime.parse("10:00:00"))
                .batidasDia(2)
                .build();

        when(registroRepository.findAllByDataBetween(any(), any())).thenReturn(Arrays.asList(registro,registro, registro));
        Boolean validarRegistros = registroService.validarRegistros(LocalDate.parse("2021-06-01"), LocalDate.parse("2021-06-30"));
        Assertions.assertThat(validarRegistros).isTrue();
    }

    @Test()
    public void deveValidarRegistrosFalso(){

        when(registroRepository.findAllByDataBetween(any(), any())).thenReturn(new ArrayList<>());
        Boolean validarRegistros = registroService.validarRegistros(LocalDate.parse("2021-06-01"), LocalDate.parse("2021-06-30"));
        Assertions.assertThat(validarRegistros).isFalse();
    }

    @Test(expected = ExceptionHorarioJaRegistrado.class)
    public void deveRegistroPontoValidarHoraDuplicadaErro() {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .dataHora("2021-06-09T10:00:00")
                .build();;

        Registro registro = Registro.builder()
                .id(1)
                .data(LocalDate.parse("2021-06-09"))
                .hora(LocalTime.parse("10:00:00"))
                .batidasDia(2)
                .build();

        when(registroRepository.findByDataAndHora(any(), any())).thenReturn(Arrays.asList(registro));
        registroService.registroPonto(registroDTO);
    }

    @Test(expected = ExceptionSabadoDomingo.class)
    public void deveRegistroPontoValidarSabadoDomingoErro() {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .dataHora("2021-06-12T10:00:00")
                .build();;
        registroService.registroPonto(registroDTO);
    }
}

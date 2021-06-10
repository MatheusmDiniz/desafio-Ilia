package com.desafio.ilia.service;

import com.desafio.ilia.entity.ControleDiario;
import com.desafio.ilia.entity.Registro;
import com.desafio.ilia.repository.ControleDiarioRepository;
import com.desafio.ilia.service.dto.RegistroDTO;
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
public class ControleDiarioServiceTest {
    private ControleDiarioService controleDiarioService;

    @MockBean
    ControleDiarioRepository controleDiarioRepository;

    @Before
    public void setUp(){
        controleDiarioService = new ControleDiarioService(controleDiarioRepository);
    }

    @Test
    public void deveSalvarControleDiarioComDuasBatidas() {

        when(controleDiarioRepository.findByDia(any())).thenReturn(Optional.of(new ControleDiario()));

        List<LocalTime> listaHoras = Arrays.asList(LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"));

        controleDiarioService.save(LocalDate.parse("2021-06-09"), listaHoras, 2);

        verify(controleDiarioRepository,times(1)).save(any());
    }

    @Test
    public void deveSalvarControleDiarioComQuatroBatidas() {

        when(controleDiarioRepository.findByDia(any())).thenReturn(Optional.of(new ControleDiario()));

        List<LocalTime> listaHoras = Arrays.asList(LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"), LocalTime.parse("15:00:00"), LocalTime.parse("17:00:00"));

        controleDiarioService.save(LocalDate.parse("2021-06-09"), listaHoras, 4);

        verify(controleDiarioRepository,times(1)).save(any());
    }

    @Test
    public void naoDeveSalvarControleDiario() {

        when(controleDiarioRepository.findByDia(any())).thenReturn(Optional.of(new ControleDiario()));

        List<LocalTime> listaHoras = Arrays.asList(LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"), LocalTime.parse("15:00:00"), LocalTime.parse("17:00:00"));

        controleDiarioService.save(LocalDate.parse("2021-06-09"), listaHoras, 3);

        verify(controleDiarioRepository,times(0)).save(any());
    }

    @Test
    public void devePegarHorasTrabalhadasMes() {

        List<String> listaString = Arrays.asList("PT2H00M0S", "PT2H00M0S");
        when(controleDiarioRepository.getHoraBetweenDatas(any(), any())).thenReturn(listaString);

        String totalHorasTrabalhadas = controleDiarioService.pegarHorasTrabalhadasMes(LocalDate.parse("2021-06-01"), LocalDate.parse("2021-06-30"));

        Assertions.assertThat(totalHorasTrabalhadas).isEqualTo("PT4H");
    }

    @Test
    public void devePegarHorasExedentesSemHorasExedentes() {
        String totalHorasTrabalhadas = controleDiarioService.pegarHorasExcedentes("PT4H");

        Assertions.assertThat(totalHorasTrabalhadas).isEqualTo("PT0S");
    }

    @Test
    public void devePegarHorasExedentesComHorasExedentes() {
        String totalHorasTrabalhadas = controleDiarioService.pegarHorasExcedentes("PT201H");

        Assertions.assertThat(totalHorasTrabalhadas).isEqualTo("PT1H");
    }

    @Test
    public void devePegarHorasDevidasSemDeverHoras() {
        String totalHorasTrabalhadas = controleDiarioService.pegarHorasDevidas("PT201H");

        Assertions.assertThat(totalHorasTrabalhadas).isEqualTo("PT0S");
    }

    @Test
    public void devePegarHorasDevidasDevendoHoras() {
        String totalHorasTrabalhadas = controleDiarioService.pegarHorasDevidas("PT199H");

        Assertions.assertThat(totalHorasTrabalhadas).isEqualTo("PT1H");
    }

}

package com.desafio.ilia.config.bd;

import com.desafio.ilia.entity.Alocacao;
import com.desafio.ilia.entity.ControleDiario;
import com.desafio.ilia.entity.Registro;
import com.desafio.ilia.repository.AlocalcaoRepository;
import com.desafio.ilia.repository.ControleDiarioRepository;
import com.desafio.ilia.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Service
public class DbInitialize {

    public static final String DATA1 = "2018-02-20";
    public static final String DATA2 = "2018-02-21";
    @Autowired
    RegistroRepository registroRepository;

    @Autowired
    ControleDiarioRepository controleDiarioRepositoryrio;

    @Autowired
    AlocalcaoRepository alocalcaoRepository;

    public void instantieateTestDatabase() {

        Registro registro1 = Registro.builder()
                .id(1)
                .batidasDia(1)
                .data(LocalDate.parse(DATA1))
                .hora(LocalTime.parse("08:00:00"))
                .build();

        Registro registro2 = Registro.builder()
                .id(2)
                .batidasDia(2)
                .data(LocalDate.parse(DATA1))
                .hora(LocalTime.parse("12:00:00"))
                .build();

        Registro registro3 = Registro.builder()
                .id(3)
                .batidasDia(3)
                .data(LocalDate.parse(DATA1))
                .hora(LocalTime.parse("13:00:00"))
                .build();

        Registro registro4 = Registro.builder()
                .id(4)
                .batidasDia(4)
                .data(LocalDate.parse(DATA1))
                .hora(LocalTime.parse("17:00:00"))
                .build();

        Registro registro5 = Registro.builder()
                .id(5)
                .batidasDia(1)
                .data(LocalDate.parse(DATA2))
                .hora(LocalTime.parse("08:00:00"))
                .build();


        Registro registro6 = Registro.builder()
                .id(6)
                .batidasDia(2)
                .data(LocalDate.parse(DATA2))
                .hora(LocalTime.parse("12:00:00"))
                .build();

        Registro registro7 = Registro.builder()
                .id(7)
                .batidasDia(3)
                .data(LocalDate.parse(DATA2))
                .hora(LocalTime.parse("13:00:00"))
                .build();

        Registro registro8 = Registro.builder()
                .id(8)
                .batidasDia(4)
                .data(LocalDate.parse(DATA2))
                .hora(LocalTime.parse("17:00:00"))
                .build();


        registroRepository.saveAll(Arrays.asList(registro1, registro2, registro3, registro4, registro5, registro6, registro7, registro8));

        ControleDiario c1 = ControleDiario.builder()
                .id(1)
                .dia(LocalDate.parse(DATA1))
                .totalTrabalhadoDia("PT8H")
                .build();

        ControleDiario c2 = ControleDiario.builder()
                .id(2)
                .dia(LocalDate.parse(DATA2))
                .totalTrabalhadoDia("PT2H30M50S")
                .build();

        controleDiarioRepositoryrio.saveAll(Arrays.asList(c1,c2));

        Alocacao a1 = Alocacao.builder()
                .id(1)
                .dia(LocalDate.parse(DATA2))
                .nomeProjeto("ACME Corporation")
                .tempo("PT1H00M0S")
                .build();

        Alocacao a2 = Alocacao.builder()
                .id(2)
                .dia(LocalDate.parse(DATA2))
                .nomeProjeto("ACME Corporation")
                .tempo("PT2H00M0S")
                .build();

        Alocacao a3 = Alocacao.builder()
                .id(3)
                .dia(LocalDate.parse(DATA2))
                .nomeProjeto("ACME TESTE")
                .tempo("PT3H00M0S")
                .build();

        Alocacao a4 = Alocacao.builder()
                .id(4)
                .dia(LocalDate.parse(DATA2))
                .nomeProjeto("ACME TESTE")
                .tempo("PT4H00M0S")
                .build();

        alocalcaoRepository.saveAll(Arrays.asList(a1,a2,a3,a4));

    }
}

package com.desafio.ilia.service;

import com.desafio.ilia.entity.ControleDiario;
import com.desafio.ilia.repository.ControleDiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ControleDiarioService {

    @Autowired
    ControleDiarioRepository controleDiarioRepository;

    public ControleDiarioService(ControleDiarioRepository controleDiarioRepository) {
        this.controleDiarioRepository = controleDiarioRepository;
    }

    public void save(LocalDate dia, List<LocalTime> listTodasHoras, Integer contBatidas) {
        ControleDiario controleDiario = controleDiarioRepository.findByDia(dia).orElse(new ControleDiario());
        Duration totalTrabalhadoDia = Duration.ofSeconds(0);

        if(contBatidas == 2 ){
            totalTrabalhadoDia = Duration.between(listTodasHoras.get(0), listTodasHoras.get(1));
        }else if(contBatidas ==4){
            Duration primeiroPeriodo = Duration.between(listTodasHoras.get(2), listTodasHoras.get(3));
            Duration segundoPeriodo = Duration.between(listTodasHoras.get(0), listTodasHoras.get(1));
            totalTrabalhadoDia = primeiroPeriodo.plus(segundoPeriodo);
        }else{
            return;
        }

        controleDiario.setDia(dia);
        controleDiario.setTotalTrabalhadoDia(totalTrabalhadoDia.toString());

        controleDiarioRepository.save(controleDiario);

    }

    public String pegarHorasTrabalhadasMes( LocalDate dataInicial, LocalDate dataFinal) {

        List<String> listTodasHorasTrabalhadasMes = controleDiarioRepository.getHoraBetweenDatas(dataInicial, dataFinal);

        Duration totalHorasTrabalhadas = Duration.ofSeconds(0);
        if(!listTodasHorasTrabalhadasMes.isEmpty()){
            for(String l : listTodasHorasTrabalhadasMes) {
                totalHorasTrabalhadas = totalHorasTrabalhadas.plus(Duration.parse(l));
            }
        }
        return totalHorasTrabalhadas.toString();
    }

    public String pegarHorasExcedentes(String horasTrabalhadas) {
        //O CALCULO PARA LEVAR EM CONTA AS HORAS EXEDENTES FOI BASEADA EM 40 HORAS SEMANAIS
        //SENDO 5 SEMANAS O MÊS, SERÃO 200 HORAS MENSAIS

        Duration tempoTotal = Duration.parse(horasTrabalhadas);
        Duration horasMensais = Duration.parse("PT200H");

        if(tempoTotal.getSeconds() > horasMensais.getSeconds()){
            return tempoTotal.minus(horasMensais).toString();
        }

        return "PT0S";

    }

    public String pegarHorasDevidas(String horasTrabalhadas) {
        //O CALCULO PARA LEVAR EM CONTA AS HORAS PENDENTES FOI BASEADA EM 40 HORAS SEMANAIS
        //SENDO 5 SEMANAS O MÊS, SERÃO 200 HORAS MENSAIS

        Duration tempoTotal = Duration.parse(horasTrabalhadas);
        Duration horasMensais = Duration.parse("PT200H");

        if(tempoTotal.getSeconds() < horasMensais.getSeconds()){
            return horasMensais.minus(tempoTotal).toString();
        }

        return "PT0S";
    }
}

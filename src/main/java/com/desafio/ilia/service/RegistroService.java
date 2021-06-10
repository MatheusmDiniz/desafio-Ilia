package com.desafio.ilia.service;

import com.desafio.ilia.entity.Registro;
import com.desafio.ilia.repository.RegistroRepository;
import com.desafio.ilia.service.dto.RegistroDTO;
import com.desafio.ilia.service.dto.RegistrosRelatoriosDTO;
import com.desafio.ilia.service.exeption.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class RegistroService {

    @Autowired
    RegistroRepository registroRepository;

    @Autowired
    ControleDiarioService controleDiarioService;

    public RegistroService(RegistroRepository registroRepository, ControleDiarioService controleDiarioService) {
        this.registroRepository = registroRepository;
        this.controleDiarioService = controleDiarioService;
    }

    public void registroPonto(RegistroDTO registroDTO) {

        LocalDateTime horaData = validarFormatoDataHora(registroDTO);

        Registro registro = new Registro(null, horaData.toLocalDate(), horaData.toLocalTime(), null);

        Integer contBatidas = registroRepository.countBatidasDiaByData(registro.getData());


        if(contBatidas < 4){
            registro.setBatidasDia(++contBatidas);
        }else{
            throw new ExceptionQuatroBatidas("Apenas 4 horários podem ser registrados por dia");
        }

        if(registro.getBatidasDia() == 3){
            validarHorarioAlmoco(registro);
        }

        validarDuplicado(registro);
        validarSabadoDomingo(registro.getData());


        registroRepository.save(registro);
        saveControleDiario(horaData.toLocalDate(), contBatidas);
    }

    private void saveControleDiario(LocalDate dia, Integer contBatidaas) {

        List<LocalTime> listTodasHoras = registroRepository.getHoraByData(dia);

        controleDiarioService.save(dia, listTodasHoras, contBatidaas);
    }

    private LocalDateTime validarFormatoDataHora(RegistroDTO registro){

        LocalDateTime horaData;
        try{
            horaData = LocalDateTime.parse(registro.getDataHora());
        }catch (Exception e){
            throw new ExceptionDataHoraInvalida("Data e hora em formato inválido");
        }

        return horaData;
    }

    private void validarHorarioAlmoco(Registro registro)  {
        LocalTime ultimaHora = registroRepository.findLastBatidasDia(registro.getData());

        Duration tempoAlmoco = Duration.between(ultimaHora, registro.getHora());

        if(tempoAlmoco.getSeconds() < 3600){
            throw new ExceptionMinimoHoraAlmoco("Deve haver no mínimo 1 hora de almoço");
        }
    }

    private void validarDuplicado(Registro registro) {

        List<Registro> r = registroRepository.findByDataAndHora(registro.getData(), registro.getHora());
        if(!r.isEmpty()){
            throw new ExceptionHorarioJaRegistrado("Horário já registrado");
        }
    }

    private void validarSabadoDomingo(LocalDate data) {
        DayOfWeek dayOfWeek = data.getDayOfWeek();

        if(dayOfWeek.getValue() == 6 || dayOfWeek.getValue() == 5){
            throw new ExceptionSabadoDomingo("Sábado e domingo não são permitidos como dia de trabalho");
        }

    }

    public List<RegistrosRelatoriosDTO> pegarRegistrosRelatorio(LocalDate dataInicial, LocalDate dataFinal){

        List<Registro> registros = registroRepository.findAllByDataBetween(dataInicial, dataFinal);

        return registros.stream()
                .collect(groupingBy(Registro::getData)).entrySet().stream().map(registro ->
                    RegistrosRelatoriosDTO.builder()
                            .dia(registro.getKey())
                            .horarios(registro.getValue().stream().map(Registro::getHora).collect(Collectors.toList()))
                            .build()
                ).collect(Collectors.toList());

    }

    public Boolean validarRegistros(LocalDate dataInicial, LocalDate dataFinal){
        List<Registro> registros = registroRepository.findAllByDataBetween(dataInicial, dataFinal);

            return !registros.isEmpty();

    }

}

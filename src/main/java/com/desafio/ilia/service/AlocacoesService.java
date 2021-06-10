package com.desafio.ilia.service;

import com.desafio.ilia.entity.Alocacao;
import com.desafio.ilia.repository.AlocalcaoRepository;
import com.desafio.ilia.repository.RegistroRepository;
import com.desafio.ilia.service.dto.AlocacaoDTO;
import com.desafio.ilia.service.dto.AlocacoesRelatoriosDTO;
import com.desafio.ilia.service.exeption.ExceptionAlocarHorasAposConcluirDia;
import com.desafio.ilia.service.exeption.ExceptionAlocarTempoMaiorTrabalhado;
import com.desafio.ilia.service.exeption.ExeptionTempoInvalido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class AlocacoesService {

    @Autowired
    AlocalcaoRepository alocalcaoRepository;

    @Autowired
    RegistroRepository registroRepository;

    @Autowired
    ControleDiarioService controleDiarioService;

    public AlocacoesService(RegistroRepository registroRepository, AlocalcaoRepository alocalcaoRepository) {
        this.registroRepository = registroRepository;
        this.alocalcaoRepository = alocalcaoRepository;
    }


    public void registroAlocacao(AlocacaoDTO alocacaoDTO) {

        Alocacao alocacao = new Alocacao(null, LocalDate.parse(alocacaoDTO.getDia()), alocacaoDTO.getTempo(), alocacaoDTO.getNomeProjeto());

        validarQuantidadeTempoAlocacao(alocacao);

        alocalcaoRepository.save(alocacao);

    }

    private void validarQuantidadeTempoAlocacao(Alocacao alocacao){
        List<LocalTime> listTodasHoras = registroRepository.getHoraByData(alocacao.getDia());

        if(listTodasHoras.size() != 4){
            throw new ExceptionAlocarHorasAposConcluirDia("Só é possível alocar horas apos concluir todas as batidas referentes a data "+alocacao.getDia());
        }

        Duration primeiroPeriodo = Duration.between(listTodasHoras.get(2), listTodasHoras.get(3));
        Duration segundoPeriodo = Duration.between(listTodasHoras.get(0), listTodasHoras.get(1));
        Duration totalTrabalhadoDia = primeiroPeriodo.plus(segundoPeriodo);

        Duration tempoAlocar = null;

        try{
            tempoAlocar = Duration.parse(alocacao.getTempo());
        }catch (Exception e){
            throw new ExeptionTempoInvalido("Tempo Inválido");
        }

        List<String> listTodosTemposAlocados = alocalcaoRepository.getTemposByData(alocacao.getDia());

        Duration totalTempoAlocado = Duration.ofSeconds(0);
        if(!listTodosTemposAlocados.isEmpty()){
            for(String l : listTodosTemposAlocados) {
                totalTempoAlocado = totalTempoAlocado.plus(Duration.parse(l));
            }
        }

        if((totalTempoAlocado.getSeconds()+tempoAlocar.getSeconds()) > totalTrabalhadoDia.getSeconds()){
            throw new ExceptionAlocarTempoMaiorTrabalhado("Não pode alocar tempo maior que o tempo trabalhado no dia");
        }
    }

    public List<AlocacoesRelatoriosDTO> pegarRegistrosAlocacoes(LocalDate dataInicial, LocalDate dataFinal) {

        List<Alocacao> alocacoes = alocalcaoRepository.findAllByDiaBetween(dataInicial, dataFinal);

        return alocacoes.stream()
                .collect(groupingBy(Alocacao::getNomeProjeto)).entrySet().stream().map(alocacao ->
                     AlocacoesRelatoriosDTO.builder()
                            .nomeProjeto(alocacao.getKey())
                            .tempo(somaHoras(alocacao.getValue()))
                            .build()
                ).collect(Collectors.toList());

        }

        private String somaHoras(List<Alocacao> tempos){
             Duration tempoTotal = Duration.ofSeconds(0);

             for(Alocacao t : tempos){
                 tempoTotal = tempoTotal.plus(Duration.parse(t.getTempo()));
             }

             return tempoTotal.toString();

        }







}

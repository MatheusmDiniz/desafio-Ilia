package com.desafio.ilia.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioDTO {


	String mes;
	String horasTrabalhadas;
	String horasExcedentes;
	String horasDevidas;
	List<RegistrosRelatoriosDTO> registros;
	List<AlocacoesRelatoriosDTO> alocacoes;
	
}

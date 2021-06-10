package com.desafio.ilia.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlocacaoDTO {
	@NotNull(message = "Campo obrigatório não informado: dia")
	String dia;

	@NotNull(message = "Campo obrigatório não informado: tempo")
	String tempo;

	@NotNull(message = "Campo obrigatório não informado: nomeProjeto")
	@Size(min = 1, message = "Campo nomeProjeto está vazio")
	String nomeProjeto;
}

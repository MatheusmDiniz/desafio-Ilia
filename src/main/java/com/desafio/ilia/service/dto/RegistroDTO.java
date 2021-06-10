package com.desafio.ilia.service.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroDTO {

	@NotEmpty(message = "Campo obrigatório não informado")
	String dataHora;
}

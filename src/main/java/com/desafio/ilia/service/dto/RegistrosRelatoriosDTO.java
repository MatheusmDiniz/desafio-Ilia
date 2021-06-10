package com.desafio.ilia.service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrosRelatoriosDTO {

    private LocalDate dia;
    private List<LocalTime> horarios;

}

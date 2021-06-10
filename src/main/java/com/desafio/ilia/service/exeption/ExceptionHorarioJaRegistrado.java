package com.desafio.ilia.service.exeption;

public class ExceptionHorarioJaRegistrado extends RuntimeException {
    public ExceptionHorarioJaRegistrado(String mensagem) {
        super(mensagem);
    }
}

package com.desafio.ilia.resource.exeption;
import com.desafio.ilia.service.exeption.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class RessourceExeptionHandler {

    @ExceptionHandler({ExceptionDataHoraInvalida.class, ExceptionAlocarHorasAposConcluirDia.class, ExceptionAlocarTempoMaiorTrabalhado.class, ExeptionTempoInvalido.class})
    public ResponseEntity<StandardError> badRequest(RuntimeException e, HttpServletRequest request) {

        StandardError err = new StandardError(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> bodyNull(RuntimeException e, HttpServletRequest request) {

        StandardError err = new StandardError("Campo obrigatório não informado");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<StandardError> dataInvalidaAlocacao(RuntimeException e, HttpServletRequest request) {

        StandardError err = new StandardError("Data invalida");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler({ExceptionQuatroBatidas.class, ExceptionMinimoHoraAlmoco.class, ExceptionSabadoDomingo.class})
    public ResponseEntity<StandardError> forbiden(RuntimeException e, HttpServletRequest request) {

        StandardError err = new StandardError(e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }
    @ExceptionHandler({ExceptionHorarioJaRegistrado.class})
    public ResponseEntity<StandardError> conflict(RuntimeException e, HttpServletRequest request) {

        StandardError err = new StandardError(e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        StandardError err = new StandardError();

        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            err = new StandardError(x.getDefaultMessage());

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }



    @ExceptionHandler(ExceptionRelatorioNaoEncontrado.class)
    public ResponseEntity<StandardError> notFound(RuntimeException e, HttpServletRequest request) {
        StandardError err = new StandardError(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }





}
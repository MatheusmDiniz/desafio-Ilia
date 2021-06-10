package com.desafio.ilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.desafio.ilia.entity.Registro;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Integer>{

    List<Registro> findByDataAndHora(LocalDate data, LocalTime hora);
    Integer countBatidasDiaByData(LocalDate data);

    @Query(value = "SELECT r.hora FROM Registro r WHERE r.data = ?1 ORDER BY r.hora DESC LIMIT 1", nativeQuery = true)
    LocalTime findLastBatidasDia(LocalDate data);

    @Query(value = "SELECT r.hora FROM Registro r WHERE r.data = ?1")
    List<LocalTime> getHoraByData(LocalDate dia);

    List<Registro> findAllByDataBetween(LocalDate diaInicial, LocalDate diaFinal);
}

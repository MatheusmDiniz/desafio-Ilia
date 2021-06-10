package com.desafio.ilia.repository;

import com.desafio.ilia.entity.ControleDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ControleDiarioRepository extends JpaRepository<ControleDiario, Integer> {

    @Query(value = "SELECT r.totalTrabalhadoDia FROM ControleDiario r WHERE r.dia >= :diaInicial AND r.dia <= :diaFinal ")
    List<String> getHoraBetweenDatas(@Param("diaInicial") LocalDate diaInicial, @Param("diaFinal") LocalDate diaFinal);

    Optional<ControleDiario> findByDia(LocalDate dia);
}

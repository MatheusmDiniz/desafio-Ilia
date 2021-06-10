package com.desafio.ilia.repository;

import com.desafio.ilia.entity.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlocalcaoRepository extends JpaRepository<Alocacao, Integer> {
    @Query(value = "SELECT r.tempo FROM Alocacao r WHERE r.dia = ?1")
    List<String> getTemposByData(LocalDate dia);

    List<Alocacao> findAllByDiaBetween(LocalDate diaInicial, LocalDate diaFinal);
}

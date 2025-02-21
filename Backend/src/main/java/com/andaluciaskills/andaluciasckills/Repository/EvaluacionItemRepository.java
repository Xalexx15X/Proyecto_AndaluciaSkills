package com.andaluciaskills.andaluciasckills.Repository;

import com.andaluciaskills.andaluciasckills.Entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionItemRepository extends JpaRepository<EvaluacionItem, Integer> {
    @Query("SELECT ei FROM EvaluacionItem ei WHERE ei.evaluacion_idEvaluacion = :evaluacionId")
    List<EvaluacionItem> findByEvaluacionIdEvaluacion(@Param("evaluacionId") Integer evaluacionId);
}


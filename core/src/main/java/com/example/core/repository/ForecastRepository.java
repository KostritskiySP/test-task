package com.example.core.repository;

import com.example.core.entity.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ForecastRepository extends JpaRepository<Forecast, Integer> {

    Forecast findByCity(String city);

    void deleteByCity(String city);
}

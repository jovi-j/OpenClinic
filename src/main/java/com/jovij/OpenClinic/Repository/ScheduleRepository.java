package com.jovij.OpenClinic.Repository;

import com.jovij.OpenClinic.Model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    boolean existsByMedicIdAndMonthAndYear(UUID id, Month month, Year year);
}

package com.jovij.OpenClinic.Repository;

import com.jovij.OpenClinic.Model.Attendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttendantRepository extends JpaRepository<Attendant, UUID> {
}

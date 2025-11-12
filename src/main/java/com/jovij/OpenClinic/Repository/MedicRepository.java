package com.jovij.OpenClinic.Repository;

import com.jovij.OpenClinic.Model.Medic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicRepository extends CrudRepository<Medic, UUID> {
    @Query("SELECT m FROM Medic m WHERE m.person.name = :doctorName")
    Optional<Medic> findByName(String doctorName);

    @Query("SELECT m FROM Medic m WHERE m.person.cpf = :cpf")
    Optional<Medic> findByCPF(String cpf);
}

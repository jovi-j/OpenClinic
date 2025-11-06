package com.jovij.OpenClinic.Repository;

import com.jovij.OpenClinic.Model.Medic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicRepository extends CrudRepository<Medic, UUID> {
}

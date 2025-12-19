package com.jovij.OpenClinic.Model.DTO.Person;

import java.util.UUID;

public record PersonResponseDTO(UUID id, String name, String cpf, String dateOfBirth) {
}

package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.InvalidDateFormatException;
import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.Attendant;
import com.jovij.OpenClinic.Model.DTO.Attendant.AttendantDTO;
import com.jovij.OpenClinic.Model.DTO.Attendant.AttendantResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Attendant.AttendantUpdateDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonResponseDTO;
import com.jovij.OpenClinic.Model.Person;
import com.jovij.OpenClinic.Repository.AttendantRepository;
import com.jovij.OpenClinic.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendantService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final AttendantRepository attendantRepository;
    private final PersonRepository personRepository;

    @Autowired
    public AttendantService(AttendantRepository attendantRepository, PersonRepository personRepository) {
        this.attendantRepository = attendantRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public AttendantResponseDTO create(AttendantDTO attendantDTO) {
        try {
            Person person = new Person();
            person.setName(attendantDTO.person().name());
            person.setCpf(attendantDTO.person().cpf());
            person.setBirthDate(LocalDate.parse(attendantDTO.person().dateOfBirth(), DATE_FORMATTER));
            Person savedPerson = personRepository.save(person);

            Attendant attendant = new Attendant();
            attendant.setPerson(savedPerson);
            attendant.setTicketWindow(attendantDTO.ticketWindow());

            Attendant savedAttendant = attendantRepository.save(attendant);
            return mapToDTO(savedAttendant);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public List<AttendantResponseDTO> findAll() {
        return attendantRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AttendantResponseDTO update(UUID id, AttendantUpdateDTO attendantUpdateDTO) {
        Attendant attendant = attendantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attendant not found"));

        if (attendantUpdateDTO.ticketWindow() != null) {
            attendant.setTicketWindow(attendantUpdateDTO.ticketWindow());
        }

        Attendant savedAttendant = attendantRepository.save(attendant);
        return mapToDTO(savedAttendant);
    }

    public void delete(UUID id) {
        if (!attendantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendant not found");
        }
        attendantRepository.deleteById(id);
    }

    private AttendantResponseDTO mapToDTO(Attendant attendant) {
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(
                attendant.getPerson().getId(),
                attendant.getPerson().getName(),
                attendant.getPerson().getCpf(),
                attendant.getPerson().getBirthDate().format(DATE_FORMATTER)
        );
        return new AttendantResponseDTO(attendant.getId(), personResponseDTO, attendant.getTicketWindow());
    }
}

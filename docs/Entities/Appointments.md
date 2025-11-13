# Context

The appointments are created alongside the schedule for the month, so there will be no "create" endpoint available to
the patient, just the medic and the attendants.

The patient can see all the available dates and hours for an appointment, so there will be an endpoint to list all
the dates based on a doctor.

# DTOs

## ScheduleAppointmentDTO

- PatientId -> id for the patient requesting to make an appointment
- AppointmentId -> id for the appointment requested
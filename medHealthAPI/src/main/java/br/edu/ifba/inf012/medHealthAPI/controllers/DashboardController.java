package br.edu.ifba.inf012.medHealthAPI.controllers;

import br.edu.ifba.inf012.medHealthAPI.dtos.dashboard.DashboardStatsDto;
import br.edu.ifba.inf012.medHealthAPI.services.AppointmentService;
import br.edu.ifba.inf012.medHealthAPI.services.DoctorService;
import br.edu.ifba.inf012.medHealthAPI.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        long totalDoctors = doctorService.count(); 
        long totalPatients = patientService.count();
        
        long totalAppointments = appointmentService.countTotal();
        long pendingAppointments = appointmentService.countScheduled();
        long todayAppointments = appointmentService.countToday();

        return ResponseEntity.ok(new DashboardStatsDto(
            totalDoctors,
            totalPatients,
            totalAppointments,
            pendingAppointments,
            todayAppointments
        ));
    }
}
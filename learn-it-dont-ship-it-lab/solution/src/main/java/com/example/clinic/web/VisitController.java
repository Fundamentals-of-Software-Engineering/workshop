package com.example.clinic.web;

import com.example.clinic.domain.Visit;
import com.example.clinic.service.VisitService;

import java.time.LocalDate;
import java.util.List;

public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    public String book(long petId, String date, String reason) {
        Visit visit = visitService.book(petId, LocalDate.parse(date), reason);
        return "Booked " + visit.reason() + " on " + visit.date();
    }

    public List<String> list(long petId) {
        return visitService.visitsFor(petId).stream()
                .map(visit -> visit.date() + ": " + visit.reason())
                .toList();
    }
}

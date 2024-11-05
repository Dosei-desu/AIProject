package com.example.aiproject.api;


import com.example.aiproject.dto.Diagnosis;
import com.example.aiproject.service.ClinicalTableApiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/diagnose")
@CrossOrigin(origins = "*")
public class DiagnosisController {

    private final ClinicalTableApiService clinicalService;

    @GetMapping
    public Diagnosis getTreatmentSuggestion(@RequestParam String symptom){

        return clinicalService.diagnose(symptom);
    }


}

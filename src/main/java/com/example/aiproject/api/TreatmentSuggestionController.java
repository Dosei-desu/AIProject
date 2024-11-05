package com.example.aiproject.api;


import com.example.aiproject.dto.Diagnosis;
import com.example.aiproject.dto.MyResponse;
import com.example.aiproject.service.ClinicalTableApiService;
import com.example.aiproject.service.OpenAiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/treatment")
@CrossOrigin(origins = "*")
public class TreatmentSuggestionController {

    private final OpenAiService service;
    private final ClinicalTableApiService clinicalService;

    static final String PRIMING_MESSAGE = """
            You are a helpful assistant that provides treatment suggestions.
            The user should provide an illness or a list of illnesses.
            You will start off by listing each illness at the top, each on a new paragraph
            and then you will create a space before beginning to provide two-sentence treatment
            suggestions to each illness on separate paragraphs.
            
            If you are provided a question, 'null', or something not an illness, then prompt them
            to 'Try Again.'
            """;

    @GetMapping
    public MyResponse getTreatmentSuggestion(@RequestParam String prompt){
        Diagnosis diagnosis = clinicalService.diagnose(prompt);
        return service.makeRequest(diagnosis.toString(),PRIMING_MESSAGE);
    }


}

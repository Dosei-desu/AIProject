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

    static final String PRIMING_MESSAGE = """
            You are a helpful assistant that provides treatment suggestions.
            The user should provide an illness or a list of illnesses and
            you will provide treatment suggestions based on these, limiting
            your answer to two sentences.
            If the user provides something other than the name of an illness,
            such as a question or 'null', please ignore the content of the question and
            instead prompt them to 'Try Again'.
            """;

    @GetMapping
    public MyResponse getTreatmentSuggestion(@RequestParam String prompt){

        return service.makeRequest(prompt,PRIMING_MESSAGE);
    }


}

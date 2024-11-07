package com.example.aiproject.service;

import com.example.aiproject.dto.Diagnosis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ClinicalTableApiService {

    @Value("${clinical_search.url}")
    private String DIAGNOSIS_URL;

    public Diagnosis diagnose(String symptom) {
        WebClient client = WebClient.create();
        Mono<Object> objectMono = client.get()
                .uri(DIAGNOSIS_URL + symptom)
                .retrieve()
                .bodyToMono(Object.class);
        Diagnosis diagnosis = convertObjectToDiagnosis(symptom,objectMono.block());
        System.out.println("DIAGNOSIS----"+diagnosis+"----!!!");
        return diagnosis;
    }

    private Diagnosis convertObjectToDiagnosis(String prompt, Object object){
        if(object.toString().endsWith("[]]")){
            List<String> nullString = List.of("null");
            return new Diagnosis(prompt,nullString);
        }else {
            List<String> diagnoses = List.of(
                    object.toString()
                            .split("\\[\\[")[1]
                            .split("]]")[0]
                            .split("], \\["));
            return new Diagnosis(prompt, diagnoses);
        }
    }
}

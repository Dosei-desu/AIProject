package com.example.aiproject.service;

import com.example.aiproject.dto.Diagnosis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ClinicalTableApiService {

    @Value("${clinical_search.url}")
    private String DIAGNOSIS_URL;

    public String diagnose(String symptom){
        WebClient client = WebClient.create();

        Mono<Diagnosis> diagnosisMono = client.get()
                .uri(DIAGNOSIS_URL+symptom)
                .retrieve()
                .bodyToMono(Diagnosis.class);


        System.out.println("DIAGNOSIS----"+diagnosisMono.toString()+"----!!!");
        return diagnosisMono.toString();
        //return "dog bite";
    }
}

package com.example.aiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Diagnosis {
    private String prompt;
    private List<String> diagnoses;

    @Override
    public String toString() {
        return String.join(", ", diagnoses);
    }
}

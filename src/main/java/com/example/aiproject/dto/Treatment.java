package com.example.aiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Treatment {
    private String diagnosisName;
    private String treatmentSuggestion;

    @Override
    public String toString() {
        return diagnosisName + " ("+treatmentSuggestion+")";
    }
}

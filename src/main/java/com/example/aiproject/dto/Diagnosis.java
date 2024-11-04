package com.example.aiproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class Diagnosis {
    private int id;
    private List<Integer> idc_9_codes;
    private Object unknownObject;
    private List<List<String>> diagnoses;

    @Override
    public String toString() {

        return diagnoses.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.joining(","));
    }
}

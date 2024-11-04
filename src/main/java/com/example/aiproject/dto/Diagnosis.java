package com.example.aiproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class Diagnosis {
    private int id;
    private int[] idc_9_codes;
    private Object[] object;
    private String[][] diagnoses;

    @Override
    public String toString() {

        return Arrays.stream(diagnoses).flatMap(Arrays::stream).collect(Collectors.joining(","));
    }
}

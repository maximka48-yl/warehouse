package ru.vsu.strelnikov_m_i.entities;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Sample {
    private int id;
    private String name;
    private String sampleTypeName;
    private String manufactureName;
}

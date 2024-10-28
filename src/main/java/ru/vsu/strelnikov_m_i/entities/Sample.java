package ru.vsu.strelnikov_m_i.entities;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Sample implements Identifiable {
    private int id;
    private String name;
    private int sampleTypeId;
    private int manufactureId;

}

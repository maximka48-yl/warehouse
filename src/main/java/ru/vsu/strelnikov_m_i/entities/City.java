package ru.vsu.strelnikov_m_i.entities;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class City implements Identifiable {
    private int id;
    private String name;
}

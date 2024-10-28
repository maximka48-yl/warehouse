package ru.vsu.strelnikov_m_i.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Manufacture {
    private int id;
    private String name;
    private int city_id;

}

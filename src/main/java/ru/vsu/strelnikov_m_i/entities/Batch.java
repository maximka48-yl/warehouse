package ru.vsu.strelnikov_m_i.entities;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Batch implements Identifiable {
    private int id;
    private Date date;
}

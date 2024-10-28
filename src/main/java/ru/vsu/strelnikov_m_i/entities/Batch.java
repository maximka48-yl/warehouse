package ru.vsu.strelnikov_m_i.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Batch {
    private int batch_id;
    private Date batch_date;
}

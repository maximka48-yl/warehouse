package ru.vsu.strelnikov_m_i.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.vsu.strelnikov_m_i.enums.EntryType;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Entry implements Identifiable {
    private int id;
    private EntryType entryType;
    private int sampleId;
    private int batchId;
    private int userId;
    private Date date;
    private int amount;
}

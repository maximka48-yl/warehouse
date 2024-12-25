package ru.vsu.strelnikov_m_i.entities;

import lombok.*;
import ru.vsu.strelnikov_m_i.enums.EntryType;

import java.sql.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Entry {
    private int id;
    private EntryType entryType;
    private int batchId;
    private int userId;
    private Date date;
    private int amount;
}

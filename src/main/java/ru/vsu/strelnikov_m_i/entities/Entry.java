package ru.vsu.strelnikov_m_i.entities;

import java.sql.Date;

public class Entry {
    private int id;
    private EntryType entryType;
    private int sampleId;
    private int batchId;
    private int userId;
    private Date date;
    private int amount;

    public Entry(int id, EntryType entryType, int sampleId, int batchId, int userId, Date date, int amount) {
        this.id = id;
        this.entryType = entryType;
        this.sampleId = sampleId;
        this.batchId = batchId;
        this.userId = userId;
        this.date = date;
        this.amount = amount;
    }
}

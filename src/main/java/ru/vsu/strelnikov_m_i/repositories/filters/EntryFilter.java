package ru.vsu.strelnikov_m_i.repositories.filters;

import lombok.Getter;
import lombok.Setter;
import ru.vsu.strelnikov_m_i.enums.EntryType;

import java.sql.Date;

@Setter
@Getter
public class EntryFilter {
    private EntryType entryType;
    private Integer batchId;
    private Integer userId;
    private Date date;

    public void setFilters(String[] filters) {
        try {
            setEntryType(EntryType.valueOf(filters[0]));
        } catch (RuntimeException ignored) {
            setEntryType(null);
        }
        try {
            setBatchId(Integer.parseInt(filters[1]));
        } catch (RuntimeException ignored) {
            setBatchId(null);
        }
        try {
            setUserId(Integer.parseInt(filters[2]));
        } catch (RuntimeException ignored) {
            setUserId(null);
        }
        try {
            setDate(Date.valueOf(filters[3]));
        } catch (RuntimeException ignored) {
            setDate(null);
        }
    }
}

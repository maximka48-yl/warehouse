package ru.vsu.strelnikov_m_i.entities;

import lombok.*;
import ru.vsu.strelnikov_m_i.enums.RoleType;

@ToString
@AllArgsConstructor
public class Role implements Identifiable {
    private int userId;
    @Getter
    @Setter
    private RoleType role;

    @Override
    public int getId() {
        return userId;
    }

    @Override
    public void setId(int userId) {
        this.userId = userId;
    }
}

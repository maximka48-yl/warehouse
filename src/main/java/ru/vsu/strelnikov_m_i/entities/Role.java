package ru.vsu.strelnikov_m_i.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.vsu.strelnikov_m_i.enums.RoleType;

@Data
@AllArgsConstructor
public class Role {
    private int user_id;
    private RoleType role;

}

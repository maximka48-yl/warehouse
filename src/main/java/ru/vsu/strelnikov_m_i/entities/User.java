package ru.vsu.strelnikov_m_i.entities;

import lombok.*;
import ru.vsu.strelnikov_m_i.enums.RoleType;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {
    private int id;
    private String fullName;
    private int hashedPassword;
    private RoleType role;
    private String email;
    private String phone;
}

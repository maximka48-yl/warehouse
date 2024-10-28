package ru.vsu.strelnikov_m_i.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String full_name;
    private String email;
    private String phone;
    private String password;
    private int role_id;
}

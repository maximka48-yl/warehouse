package ru.vsu.strelnikov_m_i.entities;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User implements Identifiable {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private int roleId;
}

package ru.vsu.strelnikov_m_i.entities;

public class Role {
    private int user_id;
    private RoleType role;

    public Role(int user_id, RoleType role) {
        this.user_id = user_id;
        this.role = role;
    }
}

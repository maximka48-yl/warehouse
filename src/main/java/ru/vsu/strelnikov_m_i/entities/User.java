package ru.vsu.strelnikov_m_i.entities;

public class User {
    private int id;
    private String full_name;
    private String email;
    private String phone;
    private String password;
    private int role_id;

    public User(int id, String full_name, String email, String phone, String password, int role_id) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role_id = role_id;
    }
}

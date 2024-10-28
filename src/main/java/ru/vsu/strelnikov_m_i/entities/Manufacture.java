package ru.vsu.strelnikov_m_i.entities;

public class Manufacture {
    private int id;
    private String name;
    private int city_id;

    public Manufacture(int id, String name, int city_id) {
        this.id = id;
        this.name = name;
        this.city_id = city_id;
    }
}

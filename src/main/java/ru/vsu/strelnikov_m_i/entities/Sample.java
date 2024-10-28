package ru.vsu.strelnikov_m_i.entities;

public class Sample {
    private int id;
    private String name;
    private int sampleTypeId;
    private int manufactureId;

    public Sample(int id, String name, int sampleTypeId, int manufactureId) {
        this.id = id;
        this.name = name;
        this.sampleTypeId = sampleTypeId;
        this.manufactureId = manufactureId;
    }
}

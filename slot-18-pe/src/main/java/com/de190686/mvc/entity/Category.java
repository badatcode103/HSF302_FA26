package com.de190686.mvc.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer category_id;

    @Column(length = 100)
    private String category_name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Device> devices;

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Category() {
    }

    public Category(Integer category_id, String category_name, List<Device> devices) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.devices = devices;
    }
}

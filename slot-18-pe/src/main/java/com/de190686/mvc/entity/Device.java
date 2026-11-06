package com.de190686.mvc.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer device_id;

    @Column
    private String device_code;

    @Column
    private String device_name;

    @Column
    private BigDecimal price;

    @Column
    private int warranty_month;

    @Column
    private LocalDate installation_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Device() {
    }

    public Device(Integer device_id, String device_code, String device_name, int warranty_month, BigDecimal price, LocalDate installation_date, Category category) {
        this.device_id = device_id;
        this.device_code = device_code;
        this.device_name = device_name;
        this.warranty_month = warranty_month;
        this.price = price;
        this.installation_date = installation_date;
        this.category = category;
    }

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getWarranty_month() {
        return warranty_month;
    }

    public void setWarranty_month(int warranty_month) {
        this.warranty_month = warranty_month;
    }

    public LocalDate getInstallation_date() {
        return installation_date;
    }

    public void setInstallation_date(LocalDate installation_date) {
        this.installation_date = installation_date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

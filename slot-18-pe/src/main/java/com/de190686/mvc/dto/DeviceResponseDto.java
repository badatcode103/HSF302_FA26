package com.de190686.mvc.dto;

import com.de190686.mvc.entity.Category;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DeviceResponseDto {

    private Integer device_id;

    private String device_code;

    private String device_name;

    private BigDecimal price;

    private int warranty_months;

    private String status;

    private LocalDate installation_date;

    private Category category;

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setInstallation_date(LocalDate installation_date) {
        this.installation_date = installation_date;
    }

    public void setWarranty_months(int warranty_months) {
        this.warranty_months = warranty_months;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DeviceResponseDto(Integer device_id, String device_code, String device_name, BigDecimal price, int warranty_months, String status, LocalDate installation_date, Category category) {
        this.device_id = device_id;
        this.device_code = device_code;
        this.device_name = device_name;
        this.price = price;
        this.warranty_months = warranty_months;
        this.status = status;
        this.installation_date = installation_date;
        this.category = category;
    }

    public DeviceResponseDto() {
    }

    public Integer getDevice_id() {
        return device_id;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getInstallation_date() {
        return installation_date;
    }

    public int getWarranty_months() {
        return warranty_months;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDevice_name() {
        return device_name;
    }

    public String getDevice_code() {
        return device_code;
    }
}

package com.de190686.mvc.dto;

import com.de190686.mvc.entity.Category;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DeviceRequestDto {

    @NotBlank(message = "Device Code is required")
    @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "Device code must start with 3 uppercase letters followed by 3 digits (e.g., DVC101)")
    private String device_code;

    @NotBlank(message = "Device name is required")
    @Size(max = 200, message = "Device name must not exceed 200 characters")
    @Pattern(regexp = "^\\S(.*\\S)?$", message = "No space at both sides")
    private String device_name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a number greater than 0")
    private BigDecimal price;

    @NotNull(message = "Warranty is required")
    @Min(value = 1, message = "Warranty must be between 1 and 60")
    @Max(value = 60, message = "Warranty must be between 1 and 60")
    private Integer warranty_months;

    @NotNull(message = "Installation date is required")
    @Future(message = "Installation date must be a future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate installation_date;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Category is required")
    private Category category;

    public DeviceRequestDto() {
    }

    public DeviceRequestDto(String device_code, String device_name, BigDecimal price, Integer warranty_months, LocalDate installation_date, String status, Category category) {
        this.device_code = device_code;
        this.device_name = device_name;
        this.price = price;
        this.warranty_months = warranty_months;
        this.installation_date = installation_date;
        this.status = status;
        this.category = category;
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

    public Integer getWarranty_months() {
        return warranty_months;
    }

    public void setWarranty_months(Integer warranty_months) {
        this.warranty_months = warranty_months;
    }

    public LocalDate getInstallation_date() {
        return installation_date;
    }

    public void setInstallation_date(LocalDate installation_date) {
        this.installation_date = installation_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

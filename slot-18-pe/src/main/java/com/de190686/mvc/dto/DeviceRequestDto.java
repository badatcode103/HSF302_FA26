package com.de190686.mvc.dto;

import com.de190686.mvc.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DeviceRequestDto {
    @NotBlank(message = "Device Code is required")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{3}", message = "Device code must start with 3 uppercase letter and follow by 3 number")
    private String device_code;

    @NotBlank(message = "Device name is required")
    @Size(max = 200, message = "Device Name must not exceed 200 characters")
    private String device_name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Warranty Month is required")

    private int warranty_months;


    private LocalDate installation_date;


    private Category category;
}

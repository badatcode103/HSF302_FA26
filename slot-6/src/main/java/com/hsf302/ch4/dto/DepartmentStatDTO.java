package com.hsf302.ch4.dto;


public record DepartmentStatDTO(
    String departmentCode,
    String departmentName,
    long studentCount,
    Double averageGpa
) {}

package com.de190686.mvc.service;

import com.de190686.mvc.dto.DeviceRequestDto;
import com.de190686.mvc.dto.DeviceResponseDto;

import java.util.List;

public interface DeviceService {
    List<DeviceResponseDto> listAllDevice();

    List<DeviceResponseDto> searchDevice(String device_name);

    boolean isDeviceCodeExist(String device_code);

    void addNewDevice(DeviceRequestDto deviceRequestDto);

    void deleteDevice(Integer device_id);

    DeviceResponseDto viewDetail(Integer device_code);


}

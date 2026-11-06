package com.de190686.mvc.repository;

import com.de190686.mvc.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findAllByOrderByInstallation_dateDesc();

    List<Device> findByDevice_nameContainingIgnoreCaseOrderByInstallation_dateOrderByInstallation_date(String device_name);

    boolean exitsByDevice_code(String device_code);

}

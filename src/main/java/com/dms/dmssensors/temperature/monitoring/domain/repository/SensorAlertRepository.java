package com.dms.dmssensors.temperature.monitoring.domain.repository;

import com.dms.dmssensors.temperature.monitoring.domain.model.SensorAlert;
import com.dms.dmssensors.temperature.monitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}

package com.dms.dmssensors.temperature.monitoring.domain.repository;

import com.dms.dmssensors.temperature.monitoring.domain.model.SensorId;
import com.dms.dmssensors.temperature.monitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonitoringRepository extends JpaRepository<SensorMonitoring, SensorId> {
}

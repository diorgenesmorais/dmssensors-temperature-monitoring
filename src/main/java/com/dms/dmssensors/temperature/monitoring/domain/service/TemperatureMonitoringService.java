package com.dms.dmssensors.temperature.monitoring.domain.service;

import com.dms.dmssensors.temperature.monitoring.api.model.TemperatureLogData;
import com.dms.dmssensors.temperature.monitoring.domain.model.SensorId;
import com.dms.dmssensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.dms.dmssensors.temperature.monitoring.domain.model.TemperatureLog;
import com.dms.dmssensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.dms.dmssensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.dms.dmssensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final SensorMonitoringRepository sensorMonitoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {
        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(
                        sensorMonitoring -> saveSensorMonitoring(sensorMonitoring, temperatureLogData),
                        () -> logIgnoredTemperature(temperatureLogData));
    }

    private void saveSensorMonitoring(SensorMonitoring sensorMonitoring, TemperatureLogData temperatureLogData) {
        if (sensorMonitoring.isEnabled()) {
            sensorMonitoring.setLastTemperature(temperatureLogData.getValue());
            sensorMonitoring.setUpdateAt(OffsetDateTime.now());

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .value(temperatureLogData.getValue())
                    .build();

            sensorMonitoringRepository.save(sensorMonitoring);
            temperatureLogRepository.save(temperatureLog);

            log.info("Temperature updated: sensorId: {} value: {}",
                    temperatureLogData.getSensorId(), temperatureLogData.getValue());
        } else {
            logIgnoredTemperature(temperatureLogData);
        }
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Temperature ignored: sensorId: {} value: {}",
                temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }
}

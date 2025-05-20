package com.dms.dmssensors.temperature.monitoring.domain.service;

import com.dms.dmssensors.temperature.monitoring.api.model.TemperatureLogData;
import com.dms.dmssensors.temperature.monitoring.domain.model.SensorId;
import com.dms.dmssensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    public void handleSensorAlert(TemperatureLogData temperatureLogData) {
        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert -> {
                    if (temperatureLogData.greaterThan(alert.getMaxTemperature())) {
                        log.info("Alert Max Temp: sensorId: {} Temp: {}",
                                temperatureLogData.getSensorId(), temperatureLogData.getValue());
                        return;
                    }
                    if (temperatureLogData.lessThan(alert.getMinTemperature())) {
                        log.info("Alert Min Temp: sensorId: {} Temp: {}",
                                temperatureLogData.getSensorId(), temperatureLogData.getValue());
                        return;
                    }

                    logIgnoreAlert(temperatureLogData);
                }, () -> logIgnoreAlert(temperatureLogData));
    }

    private void logIgnoreAlert(@NonNull TemperatureLogData temperatureLogData) {
        log.info("Alerting: sensorId: {} Temp: {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }

}

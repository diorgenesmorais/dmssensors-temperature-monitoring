package com.dms.dmssensors.temperature.monitoring.api.controller;

import com.dms.dmssensors.temperature.monitoring.api.model.SensorAlertInput;
import com.dms.dmssensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.dms.dmssensors.temperature.monitoring.domain.model.SensorAlert;
import com.dms.dmssensors.temperature.monitoring.domain.model.SensorId;
import com.dms.dmssensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public SensorAlertOutput getSensor(@PathVariable TSID sensorId) {
        SensorAlert sensorOutput = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return toModel(sensorOutput);
    }

    private SensorAlertOutput toModel(SensorAlert sensorOutput) {
        return SensorAlertOutput.builder()
                .id(sensorOutput.getId().getValue())
                .maxTemperature(sensorOutput.getMaxTemperature())
                .minTemperature(sensorOutput.getMinTemperature())
                .build();
    }

    @PutMapping
    public SensorAlertOutput updateSensor(@PathVariable TSID sensorId, @RequestBody SensorAlertInput input) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElse(SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .build());

        sensorAlert.setMaxTemperature(input.getMaxTemperature());
        sensorAlert.setMinTemperature(input.getMinTemperature());

        sensorAlertRepository.save(sensorAlert);

        return toModel(sensorAlert);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSensor(@PathVariable TSID sensorId) {
        SensorAlert sensorOutput = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sensorAlertRepository.delete(sensorOutput);
    }
}

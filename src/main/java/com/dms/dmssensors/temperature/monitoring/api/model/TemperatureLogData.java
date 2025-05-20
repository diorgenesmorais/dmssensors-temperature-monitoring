package com.dms.dmssensors.temperature.monitoring.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class TemperatureLogData {
    private UUID id;
    private TSID sensorId;
    private OffsetDateTime registeredAt;
    private Double value;

    public boolean greaterThan(Double threshold) {
        if (threshold == null) {
            return false;
        }
        return value != null && value.compareTo(threshold) >= 0;
    }

    public boolean lessThan(Double threshold) {
        if (threshold == null) {
            return false;
        }
        return value != null && value.compareTo(threshold) <= 0;
    }
}

package ar.edu.utn.frc.tup.lciii.dtos.common;

import ar.edu.utn.frc.tup.lciii.model.Device;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryDto {

    private String hostname;

    private String ip;

    private LocalDateTime dataDate;

    private Double hostDiskFree;

    private Double cpuUsage;

    private String microphoneState;

    private Boolean screenCaptureAllowed;

    private Boolean audioCaptureAllowed;

}

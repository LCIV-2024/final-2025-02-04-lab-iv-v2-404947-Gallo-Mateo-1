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

    private String ip;

    private LocalDateTime dataDate;

    private Double hostDiskFree;

    private String microphoneState;

    //private Double cpuUsage;

    private Boolean screenCaptureAllowed;

    private Boolean audioCaptureAllowed;

    private String hostname;


}

package ar.edu.utn.frc.tup.lciii.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "TELEMETRY")
@EqualsAndHashCode
public class Telemetry {

    @Id
    @SequenceGenerator(name = "telemetry_seq", sequenceName = "TELEMETRY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telemetry_seq")
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "HOSTNAME", referencedColumnName = "HOSTNAME") //, insertable = false, updatable = false => eso no permitia// q se maneje bien la relacion oneToOne
    private Device device;

    @Column(name = "hostnameV2")
    private String hostname;

    @Column(name = "ip")
    private String ip;

    @Column(name = "data_date")
    private LocalDateTime dataDate;

    @Column(name = "host_disk_free")
    private Double hostDiskFree;

    @Column(name = "cpu_usage")
    private Double cpuUsage;

    @Column(name = "microphone_state")
    private String microphoneState;

    @Column(name = "screen_capture_allowed")
    private Boolean screenCaptureAllowed;

    @Column(name = "audio_capture_allowed")
    private Boolean audioCaptureAllowed;

}

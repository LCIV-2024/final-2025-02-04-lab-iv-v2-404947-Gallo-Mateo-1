package ar.edu.utn.frc.tup.lciii.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "DEVICE")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "HOSTNAME", unique = true)
    private String hostName;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToOne(mappedBy = "device") //, cascade = CascadeType.ALL
    private Telemetry telemetry;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @Column(name = "os")
    private String os;

    @Column(name = "ip")
    private String ip;

    @Column(name = "mac_address", unique = true)
    private String macAddress;
}

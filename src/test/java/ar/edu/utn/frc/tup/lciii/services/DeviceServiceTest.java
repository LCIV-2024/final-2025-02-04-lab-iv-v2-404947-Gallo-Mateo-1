package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.repos.DeviceRepo;
import ar.edu.utn.frc.tup.lciii.repos.TelemetryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DeviceServiceTest {
    @InjectMocks
    private DeviceService service;

    @Mock
    private DeviceRepo deviceRepo;
    @Mock
    private TelemetryRepo telemetryRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}

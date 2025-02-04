package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDto;
import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import ar.edu.utn.frc.tup.lciii.repos.DeviceRepo;
import ar.edu.utn.frc.tup.lciii.repos.TelemetryRepo;
import jdk.dynalink.linker.LinkerServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TelemetryServiceTest {
    @InjectMocks
    private TelemetryServiceImpl service;
    @Mock
    private TelemetryRepo telemetryRepo;
    @Mock
    private DeviceRepo deviceRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postTelemetry() {
        //objs
        TelemetryDto telemetryDto = new TelemetryDto("123", LocalDateTime.now(), 100.0,
                            "active", true, true, "mateog");

        Device deviceExists = new Device("mateog", LocalDateTime.now().minusDays(10), null,
                                DeviceType.LAPTOP, "windows", "1424123");
        //fakes
        when(deviceRepo.findByHostName(any())).thenReturn(deviceExists);

        //test
        TelemetryDto resp = service.postTelemetry(telemetryDto);

        //assert
        assertNotNull(resp);
        assertEquals(telemetryDto.getIp(), resp.getIp());
        assertEquals(deviceExists.getHostName(), resp.getHostname());
        verify(deviceRepo).findByHostName(any());
    }

    @Test
    void getAllTelemetry() {
        //objs
        Device device1 = new Device("mateog", LocalDateTime.now(), null, DeviceType.LAPTOP, "windows", "413232");

        Telemetry telemetry1 = new Telemetry(1L, device1, "mateog", "123", LocalDateTime.now(), 100.0,
                100.0, "active", true, true);
        Telemetry telemetry2 = new Telemetry(2L, device1, "mateog", "123", LocalDateTime.now(), 100.0,
                40.0, "active", true, false);


        List<Telemetry> listTelemetries = new ArrayList<>();
        listTelemetries.add(telemetry1); listTelemetries.add(telemetry2);
        //fakes
        when(telemetryRepo.findAll()).thenReturn(listTelemetries);

        //test
        List<TelemetryDto> resp = service.getAllTelemetry();

        //assert
        assertNotNull(resp);
        assertEquals(resp.get(0).getIp(), telemetry1.getIp());
        assertEquals(resp.get(1).getHostname(), telemetry2.getHostname());
        verify(telemetryRepo).findAll();
    }
}

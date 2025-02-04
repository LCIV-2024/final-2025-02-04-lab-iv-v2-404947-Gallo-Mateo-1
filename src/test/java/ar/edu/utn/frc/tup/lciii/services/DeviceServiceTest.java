package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDto;
import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import ar.edu.utn.frc.tup.lciii.repos.DeviceRepo;
import ar.edu.utn.frc.tup.lciii.repos.TelemetryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeviceServiceTest {
    @InjectMocks
    private DeviceServiceImpl service;

    @Mock
    private DeviceRepo deviceRepo;
    @Mock
    private TelemetryRepo telemetryRepo;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postDevice(){
        //objs
        DeviceDto device1 = new DeviceDto("aaaa", DeviceType.LAPTOP, "windows", "1424123", "6156156");

        Device deviceExists = new Device("mateog", LocalDateTime.now().minusDays(10), null,
                DeviceType.LAPTOP, "windows", "1424123");
        //fakes
        when(deviceRepo.findByHostName(any())).thenReturn(deviceExists);

        //test
        //test
        DeviceDto resp = service.postDevice(device1);

        //asserts
        assertNotNull(resp);
        assertEquals(resp.getIp(), device1.getIp());
        assertEquals(resp.getType(), device1.getType());
        verify(deviceRepo).findByHostName(any());
    }

    @Test
    void getDeviceByType(){
        //objs
        Device device1 = new Device("mateog", LocalDateTime.now(), null, DeviceType.LAPTOP, "windows", "413232");
        Device device2 = new Device("nicolas", LocalDateTime.now(), null, DeviceType.LAPTOP, "linux", "782178712");

        List<Device> devices = new ArrayList<>();
        devices.add(device1); devices.add(device2);
        //fakes
        when(deviceRepo.findAllByType(any())).thenReturn(devices);

        //test
        List<DeviceDto> resp = service.getDeviceByType(DeviceType.LAPTOP);

        //asserts
        assertNotNull(resp);
        assertEquals(2, resp.size());
        assertEquals(resp.get(0).getHostName(), device1.getHostName());
        assertEquals(resp.get(1).getHostName(), device2.getHostName());
        verify(deviceRepo).findAllByType(any());
    }

    @Test
    void getDeviceByCpuUsageBetween(){
        //objs
        Device device1 = new Device("mateog", LocalDateTime.now(), null, DeviceType.LAPTOP, "windows", "413232");

        Telemetry telemetry1 = new Telemetry(1L, device1, "mateog", "123", LocalDateTime.now(), 100.0,
                100.0, "active", true, true);

        //fake
        when(telemetryRepo.findTelemetryByCpuUsageBetween(any(), any())).thenReturn(List.of(telemetry1));

        //test
        DeviceDto resp = service.getDeviceByCpuUsageBetween("20", "140");

        //assert
        assertNotNull(resp);
        assertEquals(resp.getHostName(), device1.getHostName());
        assertEquals(resp.getMacAddress(), device1.getMacAddress());
        verify(telemetryRepo).findTelemetryByCpuUsageBetween(any(), any());

    }
}

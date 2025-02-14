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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeviceServiceTest {
    @InjectMocks
    private DeviceServiceImpl service;
    @Mock
    private DeviceRepo deviceRepo;
    @Mock
    private TelemetryRepo telemetryRepo;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void PostRandomDevices(){
        //objs
        List<Map<String, Object>> restResponse = new ArrayList<>();
        restResponse = getDevicesData();
        //fakes
        //1 rest template
        when(restTemplate.getForObject(anyString(), eq(List.class)))
                .thenReturn(restResponse);
        //2 deviceRepo.findByHostName()
        when(deviceRepo.findByHostName(any())).thenReturn(null);
        //3 deviceRepo.saveAll()
        // invocation.getArgument(0) devuelve la lista de dispositivos que se pasan a saveAll(),
        // asegura que el mock devuelva solo los dispositivos insertados, y no siempre los 4 de savedDevices.
        when(deviceRepo.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        // esta configuración asegura que el mock de saveAll() devuelva los dispositivos que realmente se están guardando,
        // en lugar de devolver un valor fijo o siempre la lista completa de dispositivos (como sucedía antes).

        //test
        List<DeviceDto> resp = service.postRandomDevices();

        //asserts
        assertNotNull(resp);
        assertEquals( 2, resp.size());
    }

    @Test
    void postDevice(){
        //objs
        DeviceDto device1 = new DeviceDto("aaaa", DeviceType.LAPTOP, "windows", "1424123", "6156156");

        Device deviceExists = new Device(null, "mateog", LocalDateTime.now().minusDays(10), null,
                DeviceType.LAPTOP, "windows", "1424123", "te43ju76");
        //fakes
        when(deviceRepo.findByHostName(any())).thenReturn(null);

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
        Device device1 = new Device(null, "mateog", LocalDateTime.now(), null, DeviceType.LAPTOP, "windows", "413232", "13re2t5");
        Device device2 = new Device(null, "nicolas", LocalDateTime.now(), null, DeviceType.LAPTOP, "linux", "782178712", "43te78uy");

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
        Device device1 = new Device(null, "mateog", LocalDateTime.now(), null, DeviceType.LAPTOP, "windows", "413232", "12re34rt");

        Telemetry telemetry1 = new Telemetry(1L, device1, "mateog", "123", LocalDateTime.now(), 100.0,
                100.0, "active", true, true);

        //fake
        when(telemetryRepo.findTelemetryByCpuUsageBetween(any(), any())).thenReturn(List.of(telemetry1));

        //test
        DeviceDto resp = service.getDeviceByCpuUsageBetween(20, 140);

        //assert
        assertNotNull(resp);
        assertEquals(resp.getHostName(), device1.getHostName());
        assertEquals(resp.getMacAddress(), device1.getMacAddress());
        verify(telemetryRepo).findTelemetryByCpuUsageBetween(any(), any());

    }

    public static List<Map<String, Object>> getDevicesData() {
        return List.of(
                Map.of(
                        "id", 1L,
                        "hostName", "Device-001",
                        "createdDate", LocalDateTime.of(2025, 2, 10, 12, 0, 0),
                        "type", "LAPTOP",
                        "os", "Windows 10",
                        "ip", "192.168.1.10",
                        "macAddress", "00:1A:2B:3C:4D:5E",
                        "telemetry", Map.of(
                                "id", 101L,
                                "hostname", "Device-001",
                                "ip", "192.168.1.10",
                                "dataDate", LocalDateTime.of(2025, 2, 10, 12, 5, 0),
                                "hostDiskFree", 120.5,
                                "cpuUsage", 35.2,
                                "microphoneState", "ENABLED",
                                "screenCaptureAllowed", true,
                                "audioCaptureAllowed", false
                        )
                ),
                Map.of(
                        "id", 2L,
                        "hostName", "Device-002",
                        "createdDate", LocalDateTime.of(2025, 2, 10, 12, 10, 0),
                        "type", "DESKTOP",
                        "os", "Ubuntu 22.04",
                        "ip", "192.168.1.11",
                        "macAddress", "00:1A:2B:3C:4D:5F",
                        "telemetry", Map.of(
                                "id", 102L,
                                "hostname", "Device-002",
                                "ip", "192.168.1.11",
                                "dataDate", LocalDateTime.of(2025, 2, 10, 12, 15, 0),
                                "hostDiskFree", 200.0,
                                "cpuUsage", 25.7,
                                "microphoneState", "DISABLED",
                                "screenCaptureAllowed", false,
                                "audioCaptureAllowed", false
                        )
                ),
                Map.of(
                        "id", 3L,
                        "hostName", "Device-003",
                        "createdDate", LocalDateTime.of(2025, 2, 10, 12, 20, 0),
                        "type", "TABLET",
                        "os", "Windows Server 2019",
                        "ip", "192.168.1.12",
                        "macAddress", "00:1A:2B:3C:4D:60",
                        "telemetry", Map.of(
                                "id", 103L,
                                "hostname", "Device-003",
                                "ip", "192.168.1.12",
                                "dataDate", LocalDateTime.of(2025, 2, 10, 12, 25, 0),
                                "hostDiskFree", 500.3,
                                "cpuUsage", 70.1,
                                "microphoneState", "N/A",
                                "screenCaptureAllowed", true,
                                "audioCaptureAllowed", true
                        )
                ),
                Map.of(
                        "id", 4L,
                        "hostName", "Device-004",
                        "createdDate", LocalDateTime.of(2025, 2, 10, 12, 30, 0),
                        "type", "SMARTPHONE",
                        "os", "Android 13",
                        "ip", "192.168.1.13",
                        "macAddress", "00:1A:2B:3C:4D:61",
                        "telemetry", Map.of(
                                "id", 104L,
                                "hostname", "Device-004",
                                "ip", "192.168.1.13",
                                "dataDate", LocalDateTime.of(2025, 2, 10, 12, 35, 0),
                                "hostDiskFree", 50.8,
                                "cpuUsage", 15.4,
                                "microphoneState", "ENABLED",
                                "screenCaptureAllowed", false,
                                "audioCaptureAllowed", true
                        )
                )
        );
    }
}

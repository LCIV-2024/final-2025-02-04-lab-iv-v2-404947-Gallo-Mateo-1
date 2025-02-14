package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDto;
import ar.edu.utn.frc.tup.lciii.services.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(" /api/save-consumed-devices")
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class ExtraController {
    @Autowired
    private DeviceService deviceService;
    @PostMapping("")
    private ResponseEntity<List<DeviceDto>> postFiveRandomDevices(){
        return ResponseEntity.ok(deviceService.postRandomDevices());
    }
}

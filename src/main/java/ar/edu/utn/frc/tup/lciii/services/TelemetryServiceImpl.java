package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDto;
import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import ar.edu.utn.frc.tup.lciii.repos.DeviceRepo;
import ar.edu.utn.frc.tup.lciii.repos.TelemetryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TelemetryServiceImpl implements TelemetryService {

    @Autowired
    private TelemetryRepo telemetryRepo;
    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TelemetryDto postTelemetry(TelemetryDto telemetryDto) {
        //POST /telemetry
        //Recibe un JSON con los datos de telemetría.
        Telemetry telemetry = mapToTelemetry(telemetryDto);

        //Valida que el hostname exista en la tabla Device, de lo contrario, rechazar la solicitud.
        Device deviceExists = deviceRepo.findByHostName(telemetry.getHostname());

        if (deviceExists == null){
            throw new IllegalArgumentException("hostname NO esta registrado en Devices");
        }
        telemetry.setDevice(deviceExists);
        //Guarda los datos en la base de datos.

        return mapToTelemetryDto(telemetry);
        //Retorna un 201 Created si la operación fue exitosa.

    }

    @Override
    public List<TelemetryDto> getAllTelemetry() {
        //Devuelve una lista con todas las entradas de telemetría registradas.
        //GET /telemetry
        List<Telemetry> telemetryList = telemetryRepo.findAll();
        List<TelemetryDto> telemetryDtoList = new ArrayList<>();
        for(Telemetry t : telemetryList) {
            TelemetryDto dto = mapToTelemetryDto(t);
            telemetryDtoList.add(dto);
        }
        return telemetryDtoList;
    }

    //mapeo
    private TelemetryDto mapToTelemetryDto(Telemetry telemetry){
        TelemetryDto dto = new TelemetryDto();
        dto.setIp(telemetry.getIp());
        dto.setHostname(telemetry.getDevice().getHostName());
        dto.setDataDate(telemetry.getDataDate());
        dto.setHostDiskFree(telemetry.getHostDiskFree());
        //dto.setCpuUsage(telemetry.getCpuUsage());
        dto.setMicrophoneState(telemetry.getMicrophoneState());
        dto.setScreenCaptureAllowed(telemetry.getScreenCaptureAllowed());
        dto.setAudioCaptureAllowed(telemetry.getAudioCaptureAllowed());

        return dto;
    }

    private Telemetry mapToTelemetry(TelemetryDto dto){
        Telemetry telemetry = new Telemetry();
        telemetry.setIp(dto.getIp());
        telemetry.setDataDate(dto.getDataDate());
        telemetry.setHostDiskFree(dto.getHostDiskFree());
        //telemetry.setCpuUsage(dto.getCpuUsage());
        telemetry.setMicrophoneState(dto.getMicrophoneState());
        telemetry.setScreenCaptureAllowed(dto.getScreenCaptureAllowed());
        telemetry.setAudioCaptureAllowed(dto.getAudioCaptureAllowed());

        return telemetry;
    }

}

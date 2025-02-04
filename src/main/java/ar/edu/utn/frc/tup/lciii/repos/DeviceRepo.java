package ar.edu.utn.frc.tup.lciii.repos;

import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepo extends JpaRepository<Device, String> {

    Device findByHostName(String hostName);

    List<Device> findAllByType(DeviceType type);

    //Obtener todos los dispositivos que tengan asociada la ultima telemetria registrada con un consumo de cpu entre
    //dos valores pasados por parametros. (No permitir que el lowThreshold sea mayor al upThreshold, en dicho caso devolver 400 bad request )
//    @Query("SELECT d FROM Device d " +
//            "JOIN d.telemetry t " +
//            "WHERE t.cpuUsage BETWEEN :upThreshold AND :lowThreshold" +
//            "GROUP BY d.hostName ")
//    List<Device> getAllDevicesByCpuUsageBetween(Double upThreshold, Double lowThreshold);

}

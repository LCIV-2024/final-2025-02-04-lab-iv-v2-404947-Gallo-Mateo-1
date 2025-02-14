package ar.edu.utn.frc.tup.lciii.repos;

import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TelemetryRepo extends JpaRepository<Telemetry, Long> {

    //Obtener todos los dispositivos que tengan asociada la ultima telemetria registrada con un consumo de cpu entre
    //dos valores pasados por parametros. (No permitir que el lowThreshold sea mayor al upThreshold, en dicho caso devolver 400 bad request )

    @Query("SELECT t FROM Telemetry t WHERE t.cpuUsage BETWEEN :lowThreshold AND :upThreshold")
    List<Telemetry> findTelemetryByCpuUsageBetween(Double upThreshold, Double lowThreshold);

}

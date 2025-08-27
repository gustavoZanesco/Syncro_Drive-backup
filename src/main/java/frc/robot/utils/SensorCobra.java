package frc.robot.utils;

import com.studica.frc.Cobra;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SensorCobra {
    
    private Cobra sensor;
    double distancia = 0;
    double filtroAnterior = 0;
    double tempoPassado = 0;

    private ShuffleboardLayout layout;

    private NetworkTableEntry sensor0Volts, sensor1Volts, sensor2Volts, sensor3Volts;
    private NetworkTableEntry sensor0Boolean, sensor1Boolean, sensor2Boolean, sensor3Boolean;

    public SensorCobra( Cobra sensor, String nome ) {
        this.sensor = sensor;

        ShuffleboardTab tab = Shuffleboard.getTab("Sensores");
        layout = tab.getLayout(nome, BuiltInLayouts.kGrid);
        
        sensor0Boolean = layout.add("sensor 0 bool", false).getEntry();
        sensor1Boolean = layout.add("sensor 1 bool", false).getEntry();
        sensor2Boolean = layout.add("sensor 2 bool", false).getEntry();
        sensor3Boolean = layout.add("sensor 3 bool", false).getEntry();

        sensor0Volts = layout.add("sensor 0 voltagem", "0").getEntry();
        sensor1Volts = layout.add("sensor 1 voltagem", "0").getEntry();
        sensor2Volts = layout.add("sensor 2 voltagem", "0").getEntry();
        sensor3Volts = layout.add("sensor 3 voltagem", "0").getEntry();
    }

    public double[] getVolts() {
       return new double[] { truncarValor(sensor.getVoltage(0)), 
        truncarValor(sensor.getVoltage(1)), 
        truncarValor(sensor.getVoltage(2)), 
        truncarValor(sensor.getVoltage(3)) };
    };

    public double truncarValor(double valorCRU) {
        double valorInt = (int) (valorCRU * 10);
        return valorInt = valorInt / 10;
    } 

    public boolean converterToBoolean(double valorSensor) {
        return !
        (valorSensor > 3.0);
    }

    public boolean[] getSensores() {
        return new boolean[] {
            converterToBoolean(getVolts()[0]),
            converterToBoolean(getVolts()[1]),
            converterToBoolean(getVolts()[2]),
            converterToBoolean(getVolts()[3]),
        };
    }

    public void atualizarTelemetria() {
        sensor0Boolean.setBoolean(converterToBoolean(getVolts()[0]));
        sensor1Boolean.setBoolean(converterToBoolean(getVolts()[1]));
        sensor2Boolean.setBoolean(converterToBoolean(getVolts()[2]));
        sensor3Boolean.setBoolean(converterToBoolean(getVolts()[3]));

        sensor0Volts.setString(Double.toString(getVolts()[0]));
        sensor1Volts.setString(Double.toString(getVolts()[1]));
        sensor2Volts.setString(Double.toString(getVolts()[2]));
        sensor3Volts.setString(Double.toString(getVolts()[3]));
        // valorBruto.setString(Double.toString(sensor.getRangeMM() / 10));
    }
}
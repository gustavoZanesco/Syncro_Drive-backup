package frc.robot.utils;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Ultrasonico {

    private Ultrasonic sensor;
    double distancia = 0;
    double filtroAnterior = 0;
    double tempoPassado = 0;

    private ShuffleboardLayout layout;

    private NetworkTableEntry distanciaNetwork, valorBruto;

    public Ultrasonico( Ultrasonic sensor, String nome ) {
      this.sensor = sensor;

      ShuffleboardTab tab = Shuffleboard.getTab("Sensores");
      layout = tab.getLayout(nome, BuiltInLayouts.kGrid);
      
      distanciaNetwork = layout.add("distancia", "0").getEntry();
      valorBruto = layout.add("distancia Sem Filtro", "0").getEntry();
    }

    public void pingTrigger() {
      sensor.ping();
    }

    /**
     * Apenas atualiza ( com filtro ) o valor da variavel que armazena a distancia lida pelo sensor
     * */
    public void atualizarLeitura() {
        
        double alphaBase = 0.2; // 0.25
        double alphaRapido = 0.95;
        double limiarMudanca = 0.2;

        double leituraAtual = sensor.getRangeMM() / 10; // [cm]

        // filtro exponencial
        double delta = Math.abs(leituraAtual - filtroAnterior);
        double alpha = ( delta > limiarMudanca ) ? alphaRapido : alphaBase;
        double leituraFiltro = leituraAtual * alpha + ( 1 - alpha ) * filtroAnterior;
        
        // valor truncado
        distancia = (int) ( leituraFiltro * 10 ); 
        distancia = distancia / 10;

        // atualizacao da variavel
        filtroAnterior = distancia;    
    }

    /**
     * 
     * @return a distancia lida pelo sensor
     */
    public double getDistancia() {
      return this.distancia;
    }

    public void atualizarTelemetria() {
      distanciaNetwork.setString(Double.toString(getDistancia()));
      valorBruto.setString(Double.toString(sensor.getRangeMM() / 10));
    }
  }
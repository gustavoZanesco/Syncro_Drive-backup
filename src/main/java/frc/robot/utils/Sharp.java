package frc.robot.utils;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Sharp {

    private AnalogInput sensor;

    double filtro = 0, alpha = 0.25, tempoPassado = 0;
    private double leitura = 0;

    private ShuffleboardLayout layout;

    private NetworkTableEntry distanciaNetwork;

    public Sharp( AnalogInput sensor, String nome ) {
      this.sensor = sensor;

      ShuffleboardTab tab = Shuffleboard.getTab("Sensores");
      layout = tab.getLayout(nome, BuiltInLayouts.kGrid);
      
      distanciaNetwork = layout.add("distancia", "0").getEntry();
    }

    double[] valoresVoltagem = new double[100];
    double resultado = 0;
    public double getTensaoFiltrada() {
        for (int i = 0; i < 100; i++) {
            valoresVoltagem[i] = sensor.getVoltage();
        }

        double somaTotal = 0;
        for (int j = 0; j < 100; j++) {
            somaTotal = somaTotal + valoresVoltagem[j];
            resultado = somaTotal / 100;
        }
        return resultado;
    }
    double tensaoFiltrada;
    /**
     * Essa função aplica um filtro exponencial na tensão de entrada do sensor e retorna a distância
     * @return a distancia do sensor até o obstáculo em [cm]
     */
    public void atualizarLeitura() {
      double alphaTensao = 0.95;

      // filtro exponencial
      double tensao = getTensaoFiltrada();
      filtro = alphaTensao * tensao + ( 1 - alphaTensao ) * tensaoFiltrada;
      tensaoFiltrada = filtro;

      if ( tensaoFiltrada > 0.42 ) {
        double leituraSensor =  Math.pow( tensaoFiltrada, -1.2045 ) * 29.726;
        double leitura = ( leituraSensor * alpha + ( 1 - alpha ) * leituraSensor );

        leitura = (int) ( leitura * 10 );
        this.leitura = leitura / 10;
      }
        else leitura = -1; 
    }

    public double getDistancia() {
      return this.leitura;
    }

    public void atualizarTelemetria() {
      distanciaNetwork.setString(Double.toString(getDistancia()));
    }
  }
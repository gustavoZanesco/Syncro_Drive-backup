package frc.robot.utils;

import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Constants;

public class Motor {

    private TitanQuad motor;
    private TitanQuadEncoder encoder;
    private PIDControl pid;

    private ShuffleboardLayout layout;
    
    private NetworkTableEntry pulsoEncoder, rpm, variacaoEnc, temperatura, angulo, kp, ki;

    private Timer timer;

    private double anguloAtual = 0;
    private boolean noAngulo;

    public Motor(TitanQuad motor, TitanQuadEncoder encoder, PIDControl pid, String nome) {
        timer = new Timer();
        timer.start();

        String valorDefault = "0";

        this.motor = motor;
        motor.invertRPM();
        this.encoder = encoder;
        this.pid = pid;
        ShuffleboardTab tab = Shuffleboard.getTab("Motores");
        layout = tab.getLayout(nome, BuiltInLayouts.kGrid);
        
        pulsoEncoder = layout.add("Pulsos Encoder", valorDefault).getEntry();
        rpm = layout.add("RPM", valorDefault).getEntry();
        variacaoEnc = layout.add("variacao do encoder", valorDefault).getEntry();
        temperatura = layout.add("Temperatura", valorDefault).getEntry();
        angulo = layout.add("AnguloAtual", valorDefault).getEntry();
        kp = layout.add("var KP", 0).getEntry();
        ki = layout.add("var KI", 0).getEntry();
    }

    public void setVelocidade(double setPoint) {
        // aplicar controle PI no motor e normalizar para duty cicle (-1 a 1)

        double velocidade = pid.calcular(getRPM(), setPoint) + setPoint;

        velocidade = velocidade / 100;
        motor.set(velocidade);
    }

    public void setAngulo(double setPoint) {

        double erro = setPoint - (anguloAtual * 0.5);

        double velocidade = erro;
        
        noAngulo = Math.abs(erro) < 0.5;

        velocidade = Constants.filtrarVelocidade(velocidade, 25, 15);
        
        if (Math.abs(erro) < 0.5) { 
            motor.stopMotor();
            velocidade = 0; 
            noAngulo = true;
        }

        setVelocidade(velocidade);
    }

    public double getAngulo() {
        return anguloAtual;
    }

    public boolean getNoAngulo() {
        return noAngulo;
    }

    public double getRPM() { return motor.getRPM(); }

    public void stop() {
        motor.stopMotor();
    }

    public void resetEncoder() {
        encoder.reset();
    }

    double anterior = 0, leitura = 0, leituraAnt = 0, dist = 0;
    public double getVariacaoEncoder() {

        if (timer.hasElapsed(0.02)) {
            double atual = getEncoderAtual();
            double variacao = (atual - anterior);
            leitura = (0.2 * variacao) + ( 1 - 0.2 ) * leituraAnt;
            leitura = (int) (leitura * 10);
            leitura = leitura / 10;
            dist = variacao ;
            anterior = atual;
            leituraAnt = leitura;
            timer.reset();
        }
        return dist;
    }

    public double getEncoderAtual() { return encoder.getRaw(); }

    public void atualizarTelemetria() {
        anguloAtual = ((getEncoderAtual() * 360) / (1464)); 
        angulo.setString(anguloAtual + "");
        pulsoEncoder.setString(encoder.getRaw() + "");
        rpm.setString(motor.getRPM() + "");
        variacaoEnc.setString(getVariacaoEncoder() + "");
        temperatura.setString((int) motor.getControllerTemp() + " C");
    }

    public double getFimDeCurso_High() {
        return motor.getLimitSwitch(0, true);
    }
}
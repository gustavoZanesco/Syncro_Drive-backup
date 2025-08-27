package frc.robot.subsystems;

import com.studica.frc.Servo;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.FiltroVelocidade;
import frc.robot.utils.Motor;
import frc.robot.utils.PIDControl;

public class ManipuladorSubsystem extends SubsystemBase {

    public boolean manipuladorAtivo = false;

    public Motor motorEixoY, motorEixoZ;

    public Servo servoGarra_EIXO_Z, servoEIXO_X, servoGarra;

    private double posicaoDesejada = 0;
    private double posicaoAtual = 0;
    private double erroPosicao = 0;
    private double velocidadeDesejada = 0;

    private boolean chegouY = false;

    private double posicaoAtualY = 0;

    private FiltroVelocidade filtroBase, filtroElevador, filtroEixoX;

    public ManipuladorSubsystem() {
        TitanQuad titan1 = new TitanQuad(Constants.titanID, 15600, Constants.motorElevadorY);
        motorEixoY = new Motor(titan1, new TitanQuadEncoder(titan1, Constants.motorElevadorY, Constants.distPorRevElevador), new PIDControl(2, 1), "Motor Elevador Y");

        TitanQuad titan2 = new TitanQuad(Constants.titanID, 15600, Constants.motorElevadorZ);
        motorEixoZ = new Motor(titan2, new TitanQuadEncoder(titan2, Constants.motorElevadorZ, Constants.distPorRevElevador), new PIDControl(1, 0.5), "Motor Elevador Z");
        
        servoEIXO_X = new Servo(8);
        servoGarra_EIXO_Z = new Servo(7);
        servoGarra = new Servo(6);

        filtroBase = new FiltroVelocidade(200, 400, 20, 10);
        filtroElevador = new FiltroVelocidade(150, 150, 20, 13);
        filtroEixoX = new FiltroVelocidade(150, 150, 20, 13);
    }
    
    /* ------------------ ELEVADOR ------------------ */
    
    public boolean getChegou() {
        return chegouY;
    }
    
    // movimentar elevador a partir de logica de controle
    public void elevadorY(double setPoint) {
        double tolerancia = 2;

        
        //posicaoDesejada = setPoint;
        double erroY = setPoint - posicaoAtualY;
        //erroPosicao = erroY;

        chegouY = Math.abs(erroY) < tolerancia;

        double vElevator = erroY;
        if (chegouY) {
            vElevator = 0;
        }

        acionarElevadorY(vElevator);
    }

    // movimentar elevador de forma simples com velocidade (RPM)
    public void acionarElevadorY(double velocidade) {
        velocidade = filtroElevador.filtrarVelocidade(velocidade);
        motorEixoY.setVelocidade(velocidade);
    }

    double distPasssada = 0;

    public void calcularDistanciaPercorrida() {
        double distElevadorY = (motorEixoY.getAngulo() / 360) * Constants.distPorRevElevador;

        posicaoAtualY = distElevadorY;
        //anguloAtualZ = atualZ * 0.5; // com relacao 1/2*/
    }

    public void atualizarPosicaoElevador(double posicao) {
        posicaoAtualY = posicao;
    }

    public double getPosicaoElevador() {
        return posicaoAtual;
    }

    /* ------------------ SERVOS ------------------ */

    private double vBaseFinal = 0;
    public boolean setBase(String direcao) {
        double anguloAlvo = 0;

        switch (direcao) {
            case "inicial":
                anguloAlvo = 90;
                break;

            case "guardar":
                anguloAlvo = 183;
                break;

            case "pegar":
                anguloAlvo = 0;
                break;

            case "45_graus":
                anguloAlvo = 45;
                break;

            case "-45_graus":
                anguloAlvo = -45;
                break;

            case "cuboEsquerdo":
                anguloAlvo = 13;
                break;

            case "cuboDireito":
                anguloAlvo = 28.5;
                break;
    }

    // taxa de variação em graus por segundo
        double erroZ = anguloAlvo - (motorEixoZ.getAngulo() * 0.5);
        double vBase = erroZ * 0.25;

        vBase = filtroBase.filtrarVelocidade(vBase);

        vBaseFinal = filtroBase.rampaVelocidade(vBaseFinal, vBase);

        chegouY = Math.abs(vBase) < 0.5;

        if (chegouY) {
            vBaseFinal = 0;
        }

        motorEixoZ.setVelocidade(vBaseFinal);

        return chegouY;
    }

    private double anguloGarra = 55.5;
    public boolean setAnguloGarra(String direcao) {
        double angDesejado = 55.5;
        switch (direcao) {
            case "0_graus":
                angDesejado = 55.5; 
                break;

            case "90_graus":
                angDesejado = 147.45; 
                break;  
        }

        double anguloAtual = servoGarra_EIXO_Z.getAngle();
        
        // aplica rampa no movimento do servo
        servoGarra_EIXO_Z.setAngle(angDesejado);
        
        double erro = angDesejado - anguloAtual;
        boolean alinhado = Math.abs(erro) < 2.0; // tolerância de 2 graus
        
        return alinhado;
    }

    private double angServoGarra = 30;
    public boolean setEstadoGarra(String estado, boolean rapido) {
        double angDesejado = 0;
    
        switch (estado) {
        case "prepararCubo":
            angDesejado = 165;
            break;

        case "pegarCubo":
            angDesejado = 0;
            break;

        case "soltarCubo":
            angDesejado = 84;
            break;

        case "pegarCUBOS":
            angDesejado = 125;
            break;

        case "prepararPalete":
            angDesejado = 238;
            break;

        case "pegarPalete":
            angDesejado = 150;
            break;

        case "soltarPalete":
            angDesejado = 125;
            break;
        }

        double anguloAtual = servoGarra.getAngle();
        
        // aplica rampa no movimento do servo
        
        servoGarra.setAngle(angDesejado);
        
        double erro = angDesejado - anguloAtual;
        boolean alinhado = Math.abs(erro) < 2.0; // tolerância de 2 graus
        
        return alinhado;
    }

    private double angEIXO_X = 150;
    public boolean setEixoX(String posicao) {
        double setPoint = 150;
        switch (posicao) {
            case "central": 
                setPoint = 150;
                break;

            case "esquerda":
                setPoint = 0;
                break;

            case "direita":
                setPoint = 300;
                break;
        }

        double anguloAtual = servoEIXO_X.getAngle();
        
        // aplica rampa no movimento do servo
        angEIXO_X = filtroEixoX.rampaVelocidade(angEIXO_X, setPoint);
        servoEIXO_X.setAngle(angEIXO_X);
        
        double erro = setPoint - anguloAtual;
        boolean alinhado = Math.abs(erro) < 2.0; // tolerância de 2 graus
        
        return alinhado;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Posicao Atual Elevador", posicaoAtualY + "");
        SmartDashboard.putString("Velocidade Elevador", velocidadeDesejada + "");
        SmartDashboard.putString("Posicao Desejada", posicaoDesejada + "");
        SmartDashboard.putString("ERRO Posicao Elevador", erroPosicao + "");
        
        calcularDistanciaPercorrida();
        motorEixoY.atualizarTelemetria();
        motorEixoZ.atualizarTelemetria();

        //manipuladorAtivo = (motorEixoY.getRPM() != 0 || motorEixoZ.getRPM() != 0);
    }
}
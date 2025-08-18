package frc.robot.subsystems;

import com.studica.frc.Servo;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.Motor;
import frc.robot.utils.PIDControl;

public class ManipuladorSubsystem extends SubsystemBase {

    public Motor motor;

    public Servo servoAnguloGarra, servoPosCubos, servoGarra, servoAnguloBase;

    double posicaoDesejada = 0;
    double posicaoAtual = 0;
    double erroPosicao = 0;
    double velocidadeDesejada = 0;

    boolean chegouY = false;

    public ManipuladorSubsystem() {
        TitanQuad titan1 = new TitanQuad(Constants.titanID, 15600, Constants.motorElevador);
        TitanQuadEncoder encoder1 = new TitanQuadEncoder(titan1, Constants.motorElevador, Constants.distPorRevElevador);

        motor = new Motor(titan1, encoder1, new PIDControl(2, 1), "Motor Elevador");

        /*servoAnguloGarra = new Servo(Constants.servoAnguloGarra);
        servoGarra = new Servo(Constants.servoGarra);
        servoPosCubos = new Servo(Constants.servoX);*/
        servoAnguloBase = new Servo(8);
        // motor.resetEncoder();
    }
    
    /* ------------------ ELEVADOR ------------------ */
    
    public boolean getChegou() {
        return chegouY;
    }
    
    // movimentar eleveador a partir de logica de controle
    public void elevadorY(double setPoint) {
        double tolerancia = 0;

        switch ((int) setPoint) {
            case 0:
                tolerancia = 5;
                break;
        
            case 274:
                tolerancia = 5;
                break;
            default:
                tolerancia = 2;
                break;
        }
        
        posicaoDesejada = setPoint;
        double erroY = setPoint - posicaoAtual;
        erroPosicao = erroY;

        chegouY = Math.abs(erroY) < tolerancia;

        double vElevator = erroY;
        if (chegouY) {
            vElevator = 0;
        }

        acionarElevador(vElevator);
    }

    // movimentar elevador de forma simples com velocidade (RPM)
    public void acionarElevador(double velocidade) {
        double vMax = 30;
        double vMin = 5;

        double vel = filtrarVelocidade(velocidade, vMax, vMin);
    
        motor.setVelocidade(vel);
    }

    double distPasssada = 0;

    public void calcularDistanciaPercorrida() {
        double distElevador = (motor.getEncoderAtual() / 1464) * Constants.distPorRevElevador;

        posicaoAtual = distElevador;
    }

    public void atualizarPosicaoElevador(double posicao) {
        posicaoAtual = posicao;
    }

    public double getPosicaoElevador() {
        return posicaoAtual;
    }

 /* ------------------ SERVOS ------------------ */
double anguloAtual = 0;

public boolean setBase(String direcao) {
    double anguloAlvo = 0;

    switch (direcao) {
        case "inicial":
            anguloAlvo = 0;
            break;

        case "guardar":
            anguloAlvo = 180;
            break;

        case "pegar":
            anguloAlvo = 0;
            break;

        case "45_graus":
            anguloAlvo = 237.75;
            break;

        case "-45_graus":
            anguloAlvo = 144.25;
            break;

        case "cuboEsquerdo":
            anguloAlvo = 13;
            break;

        case "cuboDireito":
            anguloAlvo = 28.5;
            break;
    }

    // taxa de variação em graus por segundo
    double taxa = 45; // ex: 90° por segundo
    double dt = 0.02; // 20ms do periodic
    double passo = taxa * dt;

    // aplica a rampa angular
    anguloAtual = rampaAngular(anguloAtual, anguloAlvo, passo);

    servoAnguloBase.setAngle(anguloAtual);

    SmartDashboard.putNumber("servoBase", servoAnguloBase.getAngle());

    // considera no setpoint se a diferença for menor que 1 grau
    boolean noSetPoint = Math.abs(anguloAlvo - anguloAtual) < 1.0;

    return noSetPoint;
}

private double rampaAngular(double atual, double alvo, double passo) {
    double erro = alvo - atual;

    if (Math.abs(erro) <= passo) {
        return alvo; // chegou no alvo
    }

    return atual + Math.copySign(passo, erro);
}


    double anguloGarra = 55.5;

    public boolean setAnguloGarra(String direcao) {
        double anguloBase = 55.5;
        switch (direcao) {
        case "0_graus":
            anguloBase = 55.5; 
            break;

        case "90_graus":
            anguloBase = 147.45; 
            break;  

        case "cuboEsquerdo":
            anguloBase = 64.5;
            break;
        
        case "cuboDireito":
            anguloBase = 49;
            break;
        }

        anguloGarra = rampaVelocidade(anguloGarra, anguloBase, 4, 4);
        servoAnguloGarra.setAngle(anguloGarra);

        double erro = anguloBase - anguloGarra;

        boolean noSetPoint = Math.abs(erro) == 0;

        SmartDashboard.putString("servoAngGarra", servoAnguloGarra.getAngle() + "");

        return noSetPoint;
    }

    double anguloPos = 150;

    public boolean moverMesa(String posicao) {
        double angDesejado = 150;

        switch (posicao) {
        case "central":
            angDesejado = 150;
            break;

        case "bloco1":
            angDesejado = 232;  
            break;

        case "bloco2":
            angDesejado = 300;
            break;

        case "bloco3":
            angDesejado = 55;
            break;

        case "bloco4":
            angDesejado = 13;
            break;

        case "conjunto1":
            angDesejado = 34.95;
            break;  

        case "conjunto2":
            angDesejado = 300;
        }

        anguloPos = rampaVelocidade(anguloPos, angDesejado, 5, 5);

        double erro = angDesejado - anguloPos;

        boolean noSetPoint = Math.abs(erro) == 0;

        if (noSetPoint) {
            servoPosCubos.stopMotor();
        } else {
            servoPosCubos.setAngle(anguloPos);
        }

        SmartDashboard.putString("servoMesa", servoPosCubos.getAngle() + "");
     
        return noSetPoint;
    }

    double angServoGarra = 30;
    public boolean setEstadoGarra(String estado, boolean rapido) {
        double anguloDesejado = 0;
    
        switch (estado) {
        case "prepararCubo":
            anguloDesejado = 165;
            break;

        case "pegarCubo":
            anguloDesejado = 17;
            break;

        case "soltarCubo":
            anguloDesejado = 84;
            break;

        case "pegarCUBOS":
            anguloDesejado = 125;
            break;

        case "prepararPalete":
            anguloDesejado = 238;
            break;

        case "pegarPalete":
            anguloDesejado = 165;
            break;

        case "soltarPalete":
            anguloDesejado = 125;
            break;
        }

        if (rapido) {
            servoGarra.setAngle(anguloDesejado);
            angServoGarra = servoGarra.getAngle();
            return true;
        }
        else {
            angServoGarra = rampaVelocidade(angServoGarra, anguloDesejado, 7, 7); 

            servoGarra.setAngle(angServoGarra);
        }

        return angServoGarra == anguloDesejado;
    }

    public void atualizarPos(double valor) {
        posicaoAtual = valor;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Posicao Atual Elevador", posicaoAtual + "");
        SmartDashboard.putString("Velocidade Elevador", velocidadeDesejada + "");
        SmartDashboard.putString("Posicao Desejada", posicaoDesejada + "");
        SmartDashboard.putString("ERRO Posicao Elevador", erroPosicao + "");
        
        calcularDistanciaPercorrida();
        motor.atualizarTelemetria();
    }

    public double rampaVelocidade(double atual, double desejada, double deltaAcel, double deltaDesacel) {

        double erro = desejada - atual;

        // Se já está dentro da margem, vai direto
        if (Math.abs(erro) < Math.min(deltaAcel, deltaDesacel)) {
            return desejada;
        }

        double delta;
        // Acelerando (se aproximando da desejada a partir de velocidade menor)
        if (Math.signum(erro) == Math.signum(desejada)) {
            // mesma direção → aceleração
            delta = Math.copySign(deltaAcel, erro);

        } else {
            // direções opostas → desaceleração
            delta = Math.copySign(deltaDesacel, erro);

        }
        double resultado = atual + delta;

        if (Math.abs(resultado) < 0.01 && Math.abs(desejada) < 0.01) {
            return 0.0;
        }

        return resultado;
    }

    /*
     * -------------------------------------- CLASSES AUXILIARES --------------------------------------
     */
    private double filtrarVelocidade(double velAtual, double velMax, double velMin) {
        double velocidade = aplicaVelocidadeMaxima(velAtual, velMax);
        double velocidadeFinal = aplicaVelocidadeMinima(velocidade, velMin);
        return velocidadeFinal;
    }

    private double aplicaVelocidadeMinima(double velNorm, double min) {
        if (velNorm == 0)
            return 0;
        if (Math.abs(velNorm) < min) {
            return Math.copySign(min, velNorm);
        }
        return velNorm;
    }

    private double aplicaVelocidadeMaxima(double velNorm, double max) {
        if (velNorm == 0)
            return 0;
        if (Math.abs(velNorm) > max) {
            return Math.copySign(max, velNorm);
        }
        return velNorm;
    }
}
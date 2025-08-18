package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.utils.*;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.*;
import com.studica.frc.Servo;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
    // posicões setadas para servo motor de angulação das rodas
    private enum PosicaoRodas {
        EIXO_X(0.0),
        ROTACAO(45 * 3),
        EIXO_Y(90 * 3);
        // multiplicar o angulo por 3 para ser equivalente a engrenagem conectada a roda

        private double angulo;

        private PosicaoRodas(double angulo) {
            this.angulo = angulo;
        }

        public double getAngulo() { return angulo; }
    };

    public AHRS giroscopio;
    public Ultrasonico ultraDireito, ultraEsquerdo;
    public boolean direcaoPronta = true,
    chegouZ = true,
    chegouXYZ = false;
    public double posicaoDesejadaZ = 0;
    
    double raioRoda = Constants.raioRoda;
    private final Motor roda1, roda2;

    //Motor motorDirecao;
    private final Servo servoDirecao;
    private PosicaoRodas posicaoAtualRodas = PosicaoRodas.EIXO_X;
    private final SensorFiltro filtroUltra;
    private final PIDControl controleAngular;
    private final Timer tempoEsperaServo;
    private double[] posicaoAtual = new double[] {0, 0, 0};

    double erroZ = 0;
    double VY_MIN = 10;
    double zArmazenado = 0;

    FiltroVelocidade filtroLinear,
    filtroAngular,
    filtroServo;

    public DriveSubsystem() {
        // Objetos de locomoção
        TitanQuad titan1 = new TitanQuad(Constants.titanID, Constants.roda1);
        roda1 = new Motor(titan1, new TitanQuadEncoder(titan1, Constants.roda1, Constants.distPorTick), new PIDControl(0.65, 0.4), "RODA 1");

        TitanQuad titan2 = new TitanQuad(Constants.titanID, Constants.roda2);
        roda2 = new Motor(titan2, new TitanQuadEncoder(titan2, Constants.roda2, Constants.distPorTick), new PIDControl(0.65, 0.4), "RODA 2");
        
        servoDirecao = new Servo(9);
        
        roda1.resetEncoder();
        roda2.resetEncoder();
        tempoEsperaServo = new Timer();
        tempoEsperaServo.start();
        /*TitanQuad titan3 = new TitanQuad(Constants.titanID, Constants.motorDirecao);
        motorDirecao = new Motor(titan3, new TitanQuadEncoder(titan3, Constants.motorDirecao, 0), new PIDControl(0.2, 0.1), "MOTOR DIRECAO");
        motorDirecao.resetEncoder();*/

        controleAngular = new PIDControl(1, 0.5);
        
        giroscopio = new AHRS(SPI.Port.kMXP);
        giroscopio.reset();
        
        // Sensores acoplados na base
        ultraDireito = new Ultrasonico(new Ultrasonic(1, 0), "ultra Direito");
        ultraEsquerdo = new Ultrasonico(new Ultrasonic(3, 2), "ultra Esquerdo");
        filtroUltra = new SensorFiltro();   

        filtroLinear = new FiltroVelocidade(150, 150, 50, 5);
        filtroAngular = new FiltroVelocidade(100, 100, 30, 5);
        filtroServo = new FiltroVelocidade(300, 300, 0, 0);
    }
    
    
    /* ------------------ GIROSCOPIO ------------------ */
    
    public void resetZ() {
        if (inAngle(this.posicaoAtual[2], 0, 10)) {
            zArmazenado = 0;
            giroscopio.reset();
        } else if (inAngle(this.posicaoAtual[2], 90, 10)) {
            zArmazenado = 90;
            giroscopio.reset();
        } else if (inAngle(this.posicaoAtual[2], 180, 10)) {
            zArmazenado = 180;
            giroscopio.reset();
        } else if (inAngle(this.posicaoAtual[2], -90, 10)) {
            zArmazenado = -90;
            giroscopio.reset();
        }
    }
    
    public boolean inAngle(double anguloAtual, double anguloDesejado, double tolerancia) {
        double erro = Math.abs(anguloDesejado - anguloAtual);
        return erro <= tolerancia;
    }
    
    double leituraAnterior = 0;
    
    public double getGiroscopio() {
        double leiturAtual = giroscopio.getAngle();
        double filtro = (leiturAtual * 0.2) + ((1 - 0.2) * leituraAnterior);
        leituraAnterior = filtro;
        
        return filtro;
    }
    
    /* ------------------ MOVIMENTACAO ------------------ */
    /**
     * Classe princiapl para executar movimentações com o SynchroDrive
     * <p>
     * Velocidades todas em <b> RPM 
     */
    public void synchroDrive_local(double vx, double vy, double omega) {
        PosicaoRodas direcaoDesejada = determinarModoDirecao(vx, vy, omega);
        SmartDashboard.putString("direcaoDesejada", direcaoDesejada.name());
        SmartDashboard.putString("direcaoAtual", posicaoAtualRodas.name());

        boolean mudandoDirecao = false;
        if ((roda1.getRPM() == 0) && (roda2.getRPM() == 0)) {
            if (posicaoAtualRodas.name() != direcaoDesejada.name()) {
                mudandoDirecao = true;
                if (!prepararDirecaoRodas(direcaoDesejada)) {
                        //direcaoPronta = false;
                    return;
                }
            }
        } else { servoDirecao.setAngle(posicaoAtualRodas.getAngulo()); /*direcaoPronta = true;*/ }

        boolean andar = direcaoPronta && !mudandoDirecao;

        if (andar) { movimentar(vx, vy, omega, direcaoDesejada); }
        else { velocidadeBaseFinal = 0; velocidadeAngularFinal = 0; }
        SmartDashboard.putBoolean("andar", andar);
    }
    
    /**
     * Determina o modo de direção baseada nas velocidades Desejadas
     * @return o modo de direcao
     */
    public PosicaoRodas determinarModoDirecao(double vx, double vy, double omega) {
        // prioridade: rotacao > movimento Y > movimento X
        
        if(Math.abs(omega) > 2.5) { return PosicaoRodas.ROTACAO; }
        
        else if (Math.abs(vy) > 0) { return PosicaoRodas.EIXO_Y; }
        
        else { return PosicaoRodas.EIXO_X; }
        
    }
    
    
    public boolean prepararDirecaoRodas(PosicaoRodas direcaoDesejada) {
        
        pararMotoresTracao();
        
        //tempoAlvoAtingido = 0;
        
        if(moverDirecaoRodas(direcaoDesejada.getAngulo())) {
            posicaoAtualRodas = direcaoDesejada;
            //servoDirecao.stopMotor();
            velocidadeBaseFinal = 0;
            return true;
        }
        else /*direcaoPronta = false*/;
        
        return false;
    }
    double anguloNovo = 0;
    /**
     * Executar movimento do servo direcional das rodas
     * @return <b> true </b> se chegou no setPoint
     */
    private boolean moverDirecaoRodas(double anguloAlvo) {
        double anguloAtual = servoDirecao.getAngle();
        
        // aplica rampa no movimento do servo
        anguloNovo = filtroServo.rampaVelocidade(anguloNovo, anguloAlvo);
        servoDirecao.setAngle(anguloNovo);
        
        double erro = anguloAlvo - anguloAtual;
        boolean alinhado = Math.abs(erro) < 2.0; // tolerância de 2 graus
        direcaoPronta = alinhado;
        
        SmartDashboard.putNumber("Servo Angulo Atual", anguloNovo);
        SmartDashboard.putNumber("Servo Erro", erro);
        SmartDashboard.putBoolean("Servo Alinhado", alinhado);
        SmartDashboard.putNumber("servo setpoint", anguloAlvo);
        
        return alinhado;
    }
    
    double tempoAlvoAtingido = 0;
    public boolean estavel ( boolean alinhado ) {
        if ( alinhado ) {
            if (tempoAlvoAtingido == 0) tempoAlvoAtingido = System.currentTimeMillis();
            return (System.currentTimeMillis() - tempoAlvoAtingido) > 1000;
        } else {
            tempoAlvoAtingido = 0;
            return false;
        }
    }
    
    double velocidadeBase = 0.0, velocidadeAngularFinal = 0.0, velocidadeBaseFinal = 0.0;
    /**
     * Executa o movimento após steering estar posicionado
     */
    private void movimentar(double vx, double vy, double omega, PosicaoRodas mode) {
        double velocidadeBaseDesejada = 0.0;
        double velocidadeRotacionalDesejada = 0.0;
        
        SmartDashboard.putString("vx", vx + "");
        SmartDashboard.putString("vy", vy + "");
        SmartDashboard.putString("vz", omega + "");
        SmartDashboard.putString("Modo de locomocao", mode.name());
        // Definir a velocidade alvo conforme o modo
        switch (mode) {
            case EIXO_X:
            velocidadeBaseDesejada = vx;
            break;
            case EIXO_Y:
            velocidadeBaseDesejada = -vy;
            break;
            case ROTACAO:
            velocidadeBaseDesejada = 0.0;
            break;
        }
        
        // Controle de rotação com giroscópio
        if (chegouZ) {
            velocidadeRotacionalDesejada = controleAngular.calcular(posicaoAtual[2], posicaoDesejadaZ);
        }
        else velocidadeRotacionalDesejada = omega;
        
        if (Math.abs(velocidadeRotacionalDesejada) <= 0.5) {
            velocidadeRotacionalDesejada = 0;
            tempoAlvoAtingido = 0;
        }
        
        if(chegouXYZ) {
            velocidadeBaseDesejada = 0;
            velocidadeRotacionalDesejada = 0;
        }
        
        // Limitar velocidades
        velocidadeBaseDesejada = filtroLinear.filtrarVelocidade(velocidadeBaseDesejada);
        velocidadeRotacionalDesejada = filtroAngular.filtrarVelocidade(velocidadeRotacionalDesejada);
        
        // Aplicar rampa (suavização)
        velocidadeBaseFinal = filtroLinear.rampaVelocidade(velocidadeBaseFinal, velocidadeBaseDesejada);
        velocidadeAngularFinal = filtroAngular.rampaVelocidade(velocidadeAngularFinal, velocidadeRotacionalDesejada);
        
        if (!direcaoPronta) {
            velocidadeBaseFinal = 0;
        }
        // Calcular velocidades finais dos motores
        double velocidadeMotor1 = -velocidadeBaseFinal + velocidadeAngularFinal;
        double velocidadeMotor2 =  velocidadeBaseFinal + velocidadeAngularFinal;
        
        SmartDashboard.putString("velocidadeLinear", velocidadeBaseFinal + "");
        SmartDashboard.putString("velocidadeAngular", velocidadeAngularFinal + "");
        
        // Acionar motores de tração
        roda1.setVelocidade(velocidadeMotor1);
        roda2.setVelocidade(velocidadeMotor2);
        
    }
    
    public void pararMotoresTracao() {
        roda1.stop();
        roda2.stop();
    }
    double distLinearpassado = 0;
    
    /**
     * Classe utilizada para calcular a distancia percorrida pela Base SynchroDrive
     */
    public void odometria() {
        double distRoda1 = roda1.getEncoderAtual() * Constants.distPorTickRoda;
        double distRoda2 = roda2.getEncoderAtual() * Constants.distPorTickRoda;
        
        double distLinear = (-distRoda1 / 2) + (distRoda2 / 2);
        
        double distLinear_atual = distLinear;
        double variacaoLinear = distLinear_atual - distLinearpassado;
        distLinearpassado = distLinear_atual;
        
        double variacaoY = variacaoLinear * Math.sin(Math.toRadians(posicaoAtualRodas.getAngulo()));
        double variacaoX = variacaoLinear * Math.cos(Math.toRadians(posicaoAtualRodas.getAngulo()));
        
        posicaoAtual[0] = posicaoAtual[0] + variacaoX;
        posicaoAtual[1] = posicaoAtual[1] + variacaoY;
        posicaoAtual[2] = zArmazenado + getGiroscopio();
    }
    
    public Posicao getPosicaoAtual() {
        return new Posicao(posicaoAtual[0], posicaoAtual[1], posicaoAtual[2]);
    }
    
    @Override
    public void periodic() {
        
        if (chegouXYZ) {
            pararMotoresTracao();
        }
        ///SmartDashboard.putBoolean("teste", chegou)
        filtroUltra.ultrasonicos(ultraDireito, ultraEsquerdo);
        SmartDashboard.putString("servo Direcao", servoDirecao.getAngle() + "");
        roda1.atualizarTelemetria();
        roda2.atualizarTelemetria();
        
        odometria();
        SmartDashboard.putString("X robo", posicaoAtual[0] + "");
        SmartDashboard.putString("Y robo", posicaoAtual[1] + "");
        SmartDashboard.putString("Z robo", posicaoAtual[2] + "");
        
        ultraDireito.atualizarTelemetria();
        ultraEsquerdo.atualizarTelemetria();
        SmartDashboard.putString("direcao_BORAPORRA", direcaoPronta + "");
    }
    
}
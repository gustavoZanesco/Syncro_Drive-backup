package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.utils.*;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.*;
import com.studica.frc.Servo;
import com.studica.frc.Lidar.ScanData;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
    // posicões setadas para servo motor de angulação das rodas
    
    public enum PosicaoRodas {
        EIXO_X(90*3),
        ROTACAO(45 * 3),
        EIXO_Y(0);
        // multiplicar o angulo por 3 para ser equivalente a engrenagem conectada a roda

        private double angulo;

        private PosicaoRodas(double angulo) {
            this.angulo = angulo;
        }

        public double getAngulo() { return angulo; }
    };

    public AHRS giroscopio;
    public Ultrasonico ultraDireito, ultraEsquerdo;
    public boolean chegouZ = true,
    chegouXYZ = false;
    public double posicaoDesejadaZ = 0;
    
    double raioRoda = Constants.raioRoda;
    private Motor roda1, roda2;
    private Servo servoDirecao;

    private PosicaoRodas posicaoAtualRodas = PosicaoRodas.EIXO_X;
    private double[] posicaoAtual = new double[] {0, 0, 0};
    
    private FiltroVelocidade filtroLinear,
    filtroAngular,
    filtroServo;
    private SensorFiltro filtroUltra/*, filtroSharp*/;
    private PIDControl controleAngular;
    
    public boolean manterEixo = false;
    public double anguloManter = PosicaoRodas.EIXO_X.getAngulo();

    private double zArmazenado = 0;

    private Sharp sharp1, sharp2;

    private Lidar lidar;
    public ScanData scanData;

    private SensorCobra cobra;

    public DriveSubsystem() {
        // Objetos de locomoção
        TitanQuad titan1 = new TitanQuad(Constants.titanID, Constants.roda1);   
        roda1 = new Motor(titan1, new TitanQuadEncoder(titan1, Constants.roda1, Constants.distPorTick), new PIDControl(0.2, 0.1), "RODA 1");

        TitanQuad titan2 = new TitanQuad(Constants.titanID, Constants.roda2);
        roda2 = new Motor(titan2, new TitanQuadEncoder(titan2, Constants.roda2, Constants.distPorTick), new PIDControl(0.2, 0.1), "RODA 2");
        
        lidar = new Lidar(Lidar.Port.kUSB2);
        scanData = new ScanData();

        cobra = new SensorCobra(new Cobra(), "Cobra");
        
        servoDirecao = new Servo(Constants.servoDirecao);
        
        roda1.resetEncoder();
        roda2.resetEncoder();

        controleAngular = new PIDControl(1, 0.5);
        
        giroscopio = new AHRS(SPI.Port.kMXP);
        giroscopio.reset();
        
        // Sensores acoplados na base 
        ultraDireito = new Ultrasonico(new Ultrasonic(1, 0), "ultra Direito");
        ultraEsquerdo = new Ultrasonico(new Ultrasonic(3, 2), "ultra Esquerdo");
        filtroUltra = new SensorFiltro();   

        /*sharp1 = new Sharp(new AnalogInput(0), "sharpEsquerdo");
        sharp2 = new Sharp(new AnalogInput(1), "sharpDireito");
        filtroSharp = new SensorFiltro();*/

        filtroLinear = new FiltroVelocidade(200, 200, 50, 9);
        filtroAngular = new FiltroVelocidade(300, 200, 20, 6);
        filtroServo = new FiltroVelocidade(800, 800, 0, 0);
        lidar.start();
        
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
    
    double anguloNovo = 0;
    /**
     * Executar movimento do servo direcional das rodas
     * @return <b> true </b> se chegou no setPoint
     */
    public boolean moverDirecaoRodas(PosicaoRodas desejada) {
        double anguloAlvo = desejada.getAngulo();
        double anguloAtual = servoDirecao.getAngle();
        
        // aplica rampa no movimento do servo
        anguloNovo = filtroServo.rampaVelocidade(anguloNovo, anguloAlvo);
        servoDirecao.setAngle(anguloNovo);
        
        double erro = anguloAlvo - anguloAtual;
        boolean alinhado = Math.abs(erro) < 2.0; // tolerância de 2 graus
        
        if (alinhado) {
            posicaoAtualRodas = desejada;
        }
    
        return alinhado && posicaoAtualRodas == desejada;
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
    public void movimentar(double vLinear, double vAngular) {
        double velocidadeBaseDesejada = vLinear;
        double velocidadeRotacionalDesejada = vAngular;
        
        // Controle de rotação com giroscópio
        if (chegouZ) {
            velocidadeRotacionalDesejada = controleAngular.calcular(posicaoAtual[2], posicaoDesejadaZ);
        }
        else velocidadeRotacionalDesejada = vAngular;
        
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
        
        // Calcular velocidades finais dos motores
        double velocidadeMotor1 = velocidadeBaseFinal + velocidadeAngularFinal;
        double velocidadeMotor2 = -velocidadeBaseFinal + velocidadeAngularFinal;
        
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
        
        double distLinear = (distRoda1 / 2) + (-distRoda2 / 2);
        
        double distLinear_atual = distLinear;
        double variacaoLinear = distLinear_atual - distLinearpassado;
        distLinearpassado = distLinear_atual;
        
        double variacaoXlocal = variacaoLinear * Math.sin(Math.toRadians(posicaoAtualRodas.getAngulo() / 3));
        double variacaoYlocal = variacaoLinear * Math.cos(Math.toRadians(posicaoAtualRodas.getAngulo() / 3));
        
        double ZgRad = Math.toRadians(posicaoAtual[2]);
        double variacaoXglobal = (variacaoXlocal * Math.cos(ZgRad)) + (variacaoYlocal * Math.sin(ZgRad));
        double variacaoYglobal = (variacaoXlocal * -Math.sin(ZgRad)) + (variacaoYlocal * Math.cos(ZgRad)); 
        
        posicaoAtual[0] = posicaoAtual[0] + variacaoXglobal;
        posicaoAtual[1] = posicaoAtual[1] + variacaoYglobal;
        posicaoAtual[2] = zArmazenado + getGiroscopio();
    }
    
    public Posicao getPosicaoAtual() {
        return new Posicao(posicaoAtual[0], posicaoAtual[1], posicaoAtual[2]);
    }

    public void atualizarPosicaoAtual(Posicao posicaoAtual) {
        this.posicaoAtual[0] = posicaoAtual.getX();
        this.posicaoAtual[1] = posicaoAtual.getY();
        this.posicaoAtual[2] = posicaoAtual.getZ();
    }

    public void manterEixo() {
        if (manterEixo) {
            servoDirecao.setAngle(anguloManter);
        }
        else { return; }
    }
    
    @Override
    public void periodic() {
        scanData = lidar.getData();

        SmartDashboard.putString("angulo0", scanData.distance[0] + "");
        SmartDashboard.putString("angulo180", scanData.distance[180] + "");
        manterEixo();
        
        if (chegouXYZ) {
            pararMotoresTracao();
        }
        
        SmartDashboard.putString("servo Direcao", servoDirecao.getAngle() + "");
        SmartDashboard.putString("direcaoRodas", posicaoAtualRodas.name());
        
        SmartDashboard.putString("X robo", posicaoAtual[0] + "");
        SmartDashboard.putString("Y robo", posicaoAtual[1] + "");
        SmartDashboard.putString("Z robo", posicaoAtual[2] + "");
        
        /*_____________________TELEMETRIA_____________________*/

        filtroUltra.ultrasonicos(ultraDireito, ultraEsquerdo);
        ultraDireito.atualizarTelemetria();
        ultraEsquerdo.atualizarTelemetria();

        //filtroSharp.sharps(sharp1, sharp2);
        //sharp1.atualizarTelemetria();
        //sharp2.atualizarTelemetria();
        
        odometria();
        roda1.atualizarTelemetria();
        roda2.atualizarTelemetria();
        cobra.atualizarTelemetria();
    }
}
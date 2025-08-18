package frc.robot.utils;

public class PIDControl {

    double kP = 0, kI = 0;

    double erro = 0;
    double erroAcumulado = 0;
    double correcao = 0;

    public PIDControl(double kp, double ki) {
        this.kP = kp;
        this.kI = ki;
    }

    public void setP(double kP) { this.kP = kP; }

    public void setI(double kI) { this.kI = kI; }

    public double calcular(double leitura, double setpoint) {
        erro = setpoint - leitura;
        double proporcional = erro * kP;

        erroAcumulado =+ erro;
        double integral = erroAcumulado * kI;
        
        correcao = proporcional + integral;

        return correcao;
    }

    public void reset() {
        erro = 0; erroAcumulado = 0; correcao = 0;
    }

    public double getErro() {
        return this.erro;
    }
}
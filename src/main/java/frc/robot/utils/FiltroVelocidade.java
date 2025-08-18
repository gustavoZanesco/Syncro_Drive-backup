package frc.robot.utils;

public class FiltroVelocidade {

    double min = 0, max = 0;

    double taxaAcel = 0,
    taxaDesacel = 0;

    public FiltroVelocidade(double taxaAcel, double taxaDesacel, double max, double min) {
        this.taxaAcel = taxaAcel;
        this.taxaDesacel = taxaDesacel;
        this.max = max;
        this.min = min;
    }

    public double rampaVelocidade(double atual, double desejada) {
        double dt = 0.02; // 20 ms do periodic da WPILib
        double erro = desejada - atual;
        double delta;
    
        // Escolher taxa de variação conforme aceleração ou desaceleração
        if (Math.signum(erro) == Math.signum(desejada)) {
            delta = Math.copySign(taxaAcel * dt, erro);
        } else {
            delta = Math.copySign(taxaDesacel * dt, erro);
        }
    
        // Se já está próximo do desejado, ir direto
        if (Math.abs(erro) < Math.abs(delta)) {
            return desejada;
        }
    
        return atual + delta;
    }

    public double filtrarVelocidade(double velAtual) {
        double velocidade = aplicaVelocidadeMaxima(velAtual, max);
        double velocidadeFinal = aplicaVelocidadeMinima(velocidade, min);
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
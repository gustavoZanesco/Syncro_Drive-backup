package frc.robot;

public final class Constants {
    public static int titanID = 42,
    roda1 = 1,
    roda2 = 3,
    motorDirecao = 2,
    motorElevador = 0;
    

    public static double distPorTick = 73.92 / 1464,
    raioRoda = 62.5;

    public static double distPorTickRoda = (2 * Math.PI * raioRoda) / 1464;

    /* ----------- ELEVADOR ----------- */

    public static double raioEngrenagem_Elevador = (22.5 / 2),
    distPorRevElevador = 2 * Math.PI * raioEngrenagem_Elevador;
    
    public static double filtrarVelocidade(double velAtual, double velMax, double velMin) {
        double velocidade = aplicaVelocidadeMaxima(velAtual, velMax);
        double velocidadeFinal = aplicaVelocidadeMinima(velocidade, velMin);
        return velocidadeFinal;
    }

    private static double aplicaVelocidadeMinima(double velNorm, double min) {
        if (velNorm == 0)
            return 0;
        if (Math.abs(velNorm) < min) {
            return Math.copySign(min, velNorm);
        }
        return velNorm;
    }

    private static double aplicaVelocidadeMaxima(double velNorm, double max) {
        if (velNorm == 0)
            return 0;
        if (Math.abs(velNorm) > max) {
            return Math.copySign(max, velNorm);
        }
        return velNorm;
    }
}
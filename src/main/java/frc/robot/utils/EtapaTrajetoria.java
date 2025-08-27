package frc.robot.utils;

public class EtapaTrajetoria {

    public final Posicao destino;
    public final boolean alinharComParede;
    public final String poseString;

    public EtapaTrajetoria(Posicao destino, String poseString, boolean alinharComParede) {
        this.destino = destino;
        this.alinharComParede = alinharComParede;
        this.poseString = poseString;
    }
}
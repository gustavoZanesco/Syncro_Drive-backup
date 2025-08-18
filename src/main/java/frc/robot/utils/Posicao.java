package frc.robot.utils;

public class Posicao {

    private double x = 0,
    y = 0,
    z = 0;

    public Posicao(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() { return this.x; }

    public double getY() { return this.y; }

    public double getZ() { return this.z; }
}   
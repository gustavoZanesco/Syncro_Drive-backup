package frc.robot.utils;

import edu.wpi.first.wpilibj.DigitalOutput;

public class PiscarLed {
    int contador = 0;
    public void piscarLed(DigitalOutput led, int tempo) {
        if (contador == 0) {
            for (int i = 0; i < tempo; i++) {
                led.set(true);
                if (i == (tempo - 1)) { contador = 1; }
            }
        } else {
            for (int i = 0; i < tempo; i++) {
                led.set(false);
                if (i == (tempo - 1)) { contador = 0; }
            }
        }
    }
}
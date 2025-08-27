package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.utils.PiscarLed;

public class PainelSubsystem extends SubsystemBase {
    private DigitalOutput ledStopped, ledRunning;
    private DigitalInput ledStop, ledStart;
    PiscarLed piscarStopped = new PiscarLed(), piscarRunning = new PiscarLed();

    public PainelSubsystem() {
        ledStopped = new DigitalOutput(11);
        ledRunning = new DigitalOutput(9);
    }

    public void setRunning(boolean estado) {
        ledRunning.set(estado);
    }

    public void setStopped(boolean estado) {
        ledStopped.set(estado);
    }

    public void piscarStopped() {
        piscarStopped.piscarLed(ledStopped, 2000);
    }

    public void piscarRunning() {
        piscarRunning.piscarLed(ledRunning, 2000);
    }

    @Override
    public void periodic() {
        /*setRunning(true);
        if (RobotContainer.subManipulador.manipuladorAtivo) {
            RobotContainer.subPainel.piscarStopped();
          }
          else { RobotContainer.subPainel.setStopped(false); }*/
    }
}
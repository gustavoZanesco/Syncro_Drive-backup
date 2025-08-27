package frc.robot.commands.manipulador.comandosBasicos;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ManipuladorSubsystem;

public class MovimentarElevador extends CommandBase {
    private final ManipuladorSubsystem subManipulador = RobotContainer.subManipulador;
    private double setPoint;

    public MovimentarElevador(double setPoint) {
        addRequirements(subManipulador);
        this.setPoint = setPoint;
    }

    @Override
    public void execute() {
        subManipulador.elevadorY(setPoint);
    }

    @Override
    public boolean isFinished() {
        return (subManipulador.getChegou());
    }

    @Override
    public void end(boolean interrupted) {
        subManipulador.motorEixoY.stop();
    }
}
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class Teste extends CommandBase {

    public Teste() {
        //addRequirements(RobotContainer.subPainel);
    }

    @Override
    public void initialize() {
        //RobotContainer.subPainel.setRunning(false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
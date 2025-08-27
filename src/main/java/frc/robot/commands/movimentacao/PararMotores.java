package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;

public class PararMotores extends CommandBase {
    private DriveSubsystem subDrive = RobotContainer.subDrive;

    public PararMotores() {
        addRequirements(subDrive);
    }

    @Override
    public void execute() {
        subDrive.pararMotoresTracao();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        subDrive.pararMotoresTracao();
    }
}
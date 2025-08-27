package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.DriveSubsystem.*;

public class AlinharRodas extends CommandBase {
    private DriveSubsystem subDrive = RobotContainer.subDrive;
    private PosicaoRodas eixo;
    private boolean alinhado;

    public AlinharRodas(PosicaoRodas eixo) {
        addRequirements(subDrive);
        this.eixo = eixo;
    }

    @Override
    public void initialize() {
        alinhado = false;
        subDrive.manterEixo = false;
    }

    @Override
    public void execute() {
        alinhado = subDrive.moverDirecaoRodas(eixo);
    }

    @Override
    public boolean isFinished() {
        return alinhado;
    }

    @Override
    public void end(boolean interrupted) {
        subDrive.anguloManter = eixo.getAngulo();
        subDrive.manterEixo = true;
    }
}
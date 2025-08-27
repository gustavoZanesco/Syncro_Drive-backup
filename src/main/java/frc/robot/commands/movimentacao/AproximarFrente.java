package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.movimentacao.AlinharRodas;
import frc.robot.commands.movimentacao.Aproximar;
import frc.robot.subsystems.DriveSubsystem.PosicaoRodas;

public class AproximarFrente extends SequentialCommandGroup {

    public AproximarFrente(double setPoint) {
        addCommands(
            new AlinharRodas(PosicaoRodas.EIXO_Y),
            new WaitCommand(0.2),
            new Aproximar(setPoint, "FRENTE")
        );
    }
}
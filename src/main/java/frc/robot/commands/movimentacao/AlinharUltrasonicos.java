package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem.PosicaoRodas;

public class AlinharUltrasonicos extends SequentialCommandGroup {

    public AlinharUltrasonicos() {
        addCommands(
            new AlinharRodas(PosicaoRodas.ROTACAO),
            new WaitCommand(0.2),
            new AlinharComUltra()
        );
    }
}
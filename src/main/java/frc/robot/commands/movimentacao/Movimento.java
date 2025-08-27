package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem.PosicaoRodas;

public class Movimento extends SequentialCommandGroup {

    public Movimento(double x, double y, double z, PosicaoRodas eixo) {
        addCommands(
            new AlinharRodas(eixo),
            new WaitCommand(0.2), 
            new Deslocamento(x, y, z, eixo),
            new PararMotores()
        );
    }
}
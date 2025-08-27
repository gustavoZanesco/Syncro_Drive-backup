package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem.PosicaoRodas;
import frc.robot.utils.Posicao;

public class DeslocamentoXYZ extends SequentialCommandGroup {
    double x, y, z;

    public DeslocamentoXYZ(Posicao posicaoDesejada) {
        x = posicaoDesejada.getX();
        y = posicaoDesejada.getY();
        z = posicaoDesejada.getZ();
    }

    @Override
    public void initialize() {
        Posicao posicaoAtual = RobotContainer.subDrive.getPosicaoAtual();
        SmartDashboard.putString("posicaoArmazenada", 
            posicaoAtual.getX() + " " + posicaoAtual.getY() + " " + posicaoAtual.getZ());

        this.addCommands(
            // Gira se necessário
            new Movimento(posicaoAtual.getX(), posicaoAtual.getY(), z, PosicaoRodas.ROTACAO),

            // Move no eixo Y 
            (Math.abs(y - posicaoAtual.getY()) > 5)
                ? new Movimento(posicaoAtual.getX(), y, z, (Math.abs(z) == 90) ? PosicaoRodas.EIXO_X : PosicaoRodas.EIXO_Y)
                : new InstantCommand(),

            new Movimento(x, y, z, PosicaoRodas.ROTACAO),

            // Move no eixo X
            (Math.abs(x - posicaoAtual.getX()) > 5)
                ? new Movimento(x, y, z, (Math.abs(z) == 90) ? PosicaoRodas.EIXO_Y : PosicaoRodas.EIXO_X)
                : new InstantCommand(),

            // Ajusta orientação final
            new Movimento(x, y, z, PosicaoRodas.ROTACAO)
        );

        super.initialize();
    } 
}
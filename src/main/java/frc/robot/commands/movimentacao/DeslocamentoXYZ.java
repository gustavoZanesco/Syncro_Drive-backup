package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.utils.Posicao;

public class DeslocamentoXYZ extends SequentialCommandGroup {

    public DeslocamentoXYZ(double x, double y, double z) {
        Posicao posicaoDesejada = new Posicao(x, y, z);
        Posicao posicaoAtual = RobotContainer.subDrive.getPosicaoAtual();
        addCommands(
            new MovimentoPorEixo(posicaoAtual.getX(), posicaoAtual.getY(), posicaoDesejada.getZ()),
            new WaitCommand(0.25),
            new MovimentoPorEixo(posicaoAtual.getX(), posicaoDesejada.getY(), posicaoDesejada.getZ()),
            new WaitCommand(0.25),
            new MovimentoPorEixo(posicaoDesejada.getX(), posicaoDesejada.getY(), posicaoDesejada.getZ())
        );
    }

}
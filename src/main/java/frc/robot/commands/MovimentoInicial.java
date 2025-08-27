package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.manipulador.comandosBasicos.GirarBase;
import frc.robot.commands.manipulador.comandosBasicos.MovimentarElevador;
import frc.robot.commands.movimentacao.AlinharComUltra;
import frc.robot.commands.movimentacao.AlinharUltrasonicos;

public class MovimentoInicial extends SequentialCommandGroup {

    public MovimentoInicial() {
        addCommands(
            //new GirarBase("inicial"),
            new MovimentarElevador(-5),
            new GirarBase("guardar"),
            new AlinharUltrasonicos()
        );
    }
}
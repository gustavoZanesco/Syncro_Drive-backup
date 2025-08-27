package frc.robot.commands.performance;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.manipulador.comandosBasicos.GirarBase;
import frc.robot.commands.manipulador.comandosBasicos.MovimentarElevador;
import frc.robot.commands.movimentacao.MovimentoAstar;
import frc.robot.commands.visao.LerQRCODE;
import frc.robot.utils.Mapa;

public class ObterQRCODE_DefinirGrupos extends SequentialCommandGroup {

    public ObterQRCODE_DefinirGrupos(Mapa mapa) {
        addCommands(
            new MovimentoAstar(mapa, "A", "B"),
            new GirarBase("pegar"),
            new MovimentarElevador(-377),
            new LerQRCODE(),
            new MovimentarElevador(-5)
        );
    }
}
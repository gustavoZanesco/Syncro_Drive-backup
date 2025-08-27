package frc.robot.commands.performance;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AproximarColeta;
import frc.robot.commands.manipulador.comandosBasicos.*;
import frc.robot.commands.movimentacao.*;
import frc.robot.commands.visao.LerConjunto;
import frc.robot.utils.Mapa;

public class LerBlocos_ArmazenarPosicoes extends SequentialCommandGroup {

    public LerBlocos_ArmazenarPosicoes(Mapa mapa) {
        addCommands(
            new MovimentoAstar(mapa, "B", "D-5"),
            new GirarBase("pegar"),
            new AlinharUltrasonicos(),
            new ParallelCommandGroup(
                new AproximarColeta("D-5"),
                new MovimentarElevador(-377)
            ),
            new LerConjunto(10),
            new ParallelCommandGroup(
                new DeslocamentoXYZ(mapa.getXYPonto("D-2")),
                new SequentialCommandGroup( 
                    new MovimentarElevador(-5),
                    new GirarBase("guardar"),
                    new GirarBase("pegar")
                )
            ),
            new AlinharUltrasonicos(),
            new ParallelCommandGroup(
                new AproximarColeta("D-2"),
                new MovimentarElevador(-377)
            ),
            new LerConjunto(7),
            new ParallelCommandGroup(
                new DeslocamentoXYZ(mapa.getXYPonto("C-5")),
                new SequentialCommandGroup( 
                    new MovimentarElevador(-5),
                    new GirarBase("guardar"),
                    new GirarBase("pegar")
                )
            ),
            new AlinharUltrasonicos(),
            new ParallelCommandGroup(
                new AproximarColeta("C-5"),
                new MovimentarElevador(-377)
            ),
            new LerConjunto(4),
            new ParallelCommandGroup(
                new DeslocamentoXYZ(mapa.getXYPonto("C-2")),
                new SequentialCommandGroup( 
                    new MovimentarElevador(-5),
                    new GirarBase("guardar"),
                    new GirarBase("pegar")
                )
            ),
            new AlinharUltrasonicos(),
            new ParallelCommandGroup(
                new AproximarColeta("C-2"),
                new MovimentarElevador(-377)
            ),
            new LerConjunto(1),
            new ParallelCommandGroup(
                new DeslocamentoXYZ(mapa.getXYPonto("A")), 
                new MovimentarElevador(-5))
        );
    }
}
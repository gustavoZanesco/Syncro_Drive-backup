package frc.robot.commands.manipulador.comandosBasicos;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AbrirGarra extends SequentialCommandGroup {

    public AbrirGarra(String objeto, boolean rapido) {
        if (objeto == "Palete") {
            addCommands(new MovimentarGarra("soltarPalete", rapido));
        }
        else if (objeto == "Cubo") {
            addCommands(new MovimentarGarra("soltarCubo", rapido));
        }
        addCommands(new WaitCommand(0.5));
    }
}
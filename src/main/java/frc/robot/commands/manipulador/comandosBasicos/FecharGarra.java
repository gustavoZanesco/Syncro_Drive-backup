package frc.robot.commands.manipulador.comandosBasicos;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class FecharGarra extends SequentialCommandGroup {

    public FecharGarra(String objeto, boolean rapido) {
        if (objeto == "Palete") {
            addCommands(new MovimentarGarra("pegarPalete", rapido));
        }
        else if (objeto == "Cubo") {
            addCommands(new MovimentarGarra("pegarCubo", rapido));
        }
        else if (objeto == "Cubos") {
            addCommands(new MovimentarGarra("pegarCUBOS", rapido));
        }
        addCommands(new WaitCommand(0.5));
    }
}
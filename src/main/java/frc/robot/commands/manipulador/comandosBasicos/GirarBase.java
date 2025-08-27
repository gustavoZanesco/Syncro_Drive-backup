package frc.robot.commands.manipulador.comandosBasicos;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ManipuladorSubsystem;

public class GirarBase extends CommandBase {
    private final ManipuladorSubsystem subManipulador = RobotContainer.subManipulador;
    private String posicao;

    private boolean stop = false;

    public GirarBase(String posicao) {
        addRequirements(subManipulador);
        this.posicao = posicao;
    }

    @Override
    public void execute() {
        if (subManipulador.setBase(posicao)) {
            stop = true;
        }
        else stop = false;
    }

    @Override
    public boolean isFinished() {
        return stop;
    }
}
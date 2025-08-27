package frc.robot.commands.manipulador.comandosBasicos;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ManipuladorSubsystem;

public class MovimentarGarra extends CommandBase {
    private final ManipuladorSubsystem subManipulador = RobotContainer.subManipulador;
    String estadoGarra;
    boolean rapido;

    boolean stop = false;

    public MovimentarGarra(String estadoGarra, boolean rapido) { addRequirements(subManipulador); this.estadoGarra = estadoGarra; this.rapido = rapido; }

    @Override
    public void execute() { stop = subManipulador.setEstadoGarra(estadoGarra, rapido); }

    @Override
    public boolean isFinished() { return stop; }
}
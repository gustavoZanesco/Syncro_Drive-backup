package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;

public class Aproximar extends CommandBase {

    private DriveSubsystem subDrive = RobotContainer.subDrive;

    private double setPointSensor;
    private String lado;

    boolean chegouLinear = false;

    public Aproximar(double setPointSensor, String lado) {
        this.setPointSensor = setPointSensor;
        this.lado = lado;
        addRequirements(subDrive);
    }

    @Override
    public void execute() {
        double vLinear = 0;

        switch (lado) {
            case "ESQUERDO":
                vLinear = (setPointSensor - subDrive.scanData.distance[0]);
                break;
            case "DIREITO":
                vLinear = -(setPointSensor - subDrive.scanData.distance[180]);
                break;
            case "FRENTE":
                vLinear = -(setPointSensor - subDrive.ultraDireito.getDistancia());
                break;
        }
        
        chegouLinear = Math.abs(vLinear) < 1;
        
        if (chegouLinear) { vLinear = 0; }
        
        SmartDashboard.putString("velocidadeUltra", chegouLinear + "");

        subDrive.movimentar(vLinear, 0);
    }

    @Override
    public boolean isFinished() {
        return chegouLinear;
    }

    @Override
    public void end(boolean interrupted) {
        subDrive.pararMotoresTracao();
    }
}
package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.DriveSubsystem.PosicaoRodas;
import frc.robot.utils.Posicao;

public class Deslocamento extends CommandBase {

    private DriveSubsystem subDrive = RobotContainer.subDrive;

    private double x, y, z;
    private PosicaoRodas eixo;

    boolean chegouXYZ = false;

    public Deslocamento(double x, double y, double z, PosicaoRodas eixo) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.eixo = eixo;
        addRequirements(subDrive);
    }

    @Override
    public void execute() {
        Posicao posicaoAtual = subDrive.getPosicaoAtual();
        double ZgRad = Math.toRadians(posicaoAtual.getZ());
        double vLinear = 0;
        double vAngular = 0;

        double vXglobal = x - posicaoAtual.getX();
        double vYglobal = y - posicaoAtual.getY();
        double vZglobal = z - posicaoAtual.getZ();

        double vXlocal = (vXglobal * Math.cos(ZgRad)) + (vYglobal * -Math.sin(ZgRad));
        double vYlocal = (vXglobal * Math.sin(ZgRad)) + (vYglobal * Math.cos(ZgRad));

        switch (eixo) {
            case EIXO_X:
                vLinear = vXlocal;
                break;
            case EIXO_Y:
                vLinear = vYlocal;
                break;
            case ROTACAO:
                vAngular = vZglobal;
                break;
        }

        SmartDashboard.putString("vX", vXlocal + "");
        SmartDashboard.putString("vY", vYlocal + "");

        boolean chegouLinear = Math.abs(vLinear) < 5;
        boolean chegouAngular = Math.abs(vAngular) < 1;

        chegouXYZ = chegouLinear && chegouAngular;
        
        if (chegouLinear) { vLinear = 0; }
        if (chegouAngular) { 
            vAngular = 0;
            subDrive.chegouZ = true;
            subDrive.posicaoDesejadaZ = z;
        }
        else { subDrive.chegouZ = false; }

        subDrive.movimentar(vLinear * 0.35, vAngular);
    }

    @Override
    public boolean isFinished() {
        return chegouXYZ;
    }

    @Override
    public void end(boolean interrupted) {
        subDrive.pararMotoresTracao();
    }
}
package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utils.Posicao;

public class MovimentoPorEixo extends CommandBase {
    private DriveSubsystem subDrive = RobotContainer.subDrive;

    double x = 0, y = 0, z = 0;

    public MovimentoPorEixo(double x, double y, double z) {
        addRequirements(subDrive);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void initialize() {
        //subDrive.giroscopio.reset();
    }

    @Override
    public void execute() {
        Posicao posicaoAtual = subDrive.getPosicaoAtual();

        double erroX = x - posicaoAtual.getX();
        double erroY = y - posicaoAtual.getY();
        double erroZ = z - posicaoAtual.getZ();

        subDrive.chegouZ = Math.abs(erroZ) < 2.5;
        boolean chegouX = Math.abs(erroX) < 20;
        boolean chegouY = Math.abs(erroY) < 20;

        if (subDrive.chegouZ) {
            subDrive.posicaoDesejadaZ = z;
            erroZ = 0;
        }

        if (chegouY) {
            erroY = 0;
        }

        if (chegouX) {
            erroX = 0;
        }
 
        boolean chegouXY = chegouX && chegouY;

        subDrive.chegouXYZ = chegouXY && subDrive.chegouZ;
        
        erroY = erroY * 0.5;
        erroX = erroX * 0.5;
        
        subDrive.synchroDrive_local(erroX, erroY, erroZ);
    }

    @Override
    public boolean isFinished() {
        return subDrive.chegouXYZ;
    }
}
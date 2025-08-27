package frc.robot.commands.visao;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.QRcodeSubsystem;
import frc.robot.utils.BancoGrupos;

public class LerQRCODE extends CommandBase {
    
    private static QRcodeSubsystem subQRcode = RobotContainer.subQRcode;
    private static BancoGrupos banco = RobotContainer.bancoGrupos;

    public LerQRCODE() {
        addRequirements(subQRcode);
    }

    @Override
    public void initialize() {
        subQRcode.ativarQROCDE();
        
    }
    
    @Override
    public void execute() {
        if (!banco.grupos.isEmpty()) {
            banco.modificarGrupo(0, subQRcode.separarElementos(NetworkTableInstance.getDefault().getTable("qrCode").getEntry("grupo0").getString("null")));
            banco.modificarGrupo(1, subQRcode.separarElementos(NetworkTableInstance.getDefault().getTable("qrCode").getEntry("grupo1").getString("null")));
            banco.modificarGrupo(2, subQRcode.separarElementos(NetworkTableInstance.getDefault().getTable("qrCode").getEntry("grupo2").getString("null")));
        }
        for (int i = 0; i < banco.grupos.size(); i++) {
            SmartDashboard.putString("grupo" + i, banco.grupos.get(i).toString());
        }
    }

    @Override
    public boolean isFinished() {
        return subQRcode.isFinalizado();
    }

    @Override
    public void end(boolean interrupted) {
        subQRcode.desativarQRCODE();
    }
}
package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Grupo;

public class QRcodeSubsystem extends SubsystemBase {

    private NetworkTableInstance inst = NetworkTableInstance.getDefault();
    private NetworkTable qrcodeTable = inst.getTable("qrCode");
 
    public QRcodeSubsystem() {
        qrcodeTable.getEntry("ativar").setBoolean(false);
        qrcodeTable.getEntry("executado").setBoolean(false);
    }

    public void ativarQROCDE() { qrcodeTable.getEntry( "ativar" ).setBoolean(true); }

    public void desativarQRCODE() { 
        qrcodeTable.getEntry("ativar").setBoolean(false); 
        qrcodeTable.getEntry("executado").setBoolean(false);
    }

    public boolean isFinalizado() { return qrcodeTable.getEntry("executado").getBoolean(false); }

    public Grupo separarElementos(String grupo) {
        String palete = grupo.substring(grupo.length() -1);
        
        String[] cores = new String[grupo.length() - 1];
        
        for (int i = 0; i < grupo.length() - 1; i++) {
            cores[i] = grupo.substring(i, i + 1);
        }
        
        return new Grupo(cores, palete);
    }
}
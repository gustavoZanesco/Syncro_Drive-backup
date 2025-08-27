package frc.robot.commands.movimentacao;

import java.util.List;
import java.util.Set;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.utils.EtapaTrajetoria;
import frc.robot.utils.Mapa;

public class MovimentoAstar extends SequentialCommandGroup {

    public MovimentoAstar(Mapa mapa, String inicio, String destino) {
        //mapa.resetarCaminho();
        Set<String> pontosAlinhar = Set.of("H", "J"); // pode ser carregado de arquivo também

        List<EtapaTrajetoria> etapas = mapa.gerarEtapas(inicio, destino, pontosAlinhar);

        for (EtapaTrajetoria etapa : etapas) {
            SmartDashboard.putString("xDesejado", etapa.destino.getX() + "");
            SmartDashboard.putString("yDesejado", etapa.destino.getY() + "");
            SmartDashboard.putString("zDesejado", etapa.destino.getZ() + "");
            addCommands(new DeslocamentoXYZ(etapa.destino),
            new PararMotores());
        }
    }
}
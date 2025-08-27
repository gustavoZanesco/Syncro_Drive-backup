package frc.robot.commands.visao;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.utils.BancoBlocos;

public class LerConjunto extends CommandBase {

    private final BancoBlocos banco = RobotContainer.bancoBlocos;
    private final int indice;
    private final NetworkTable table = NetworkTableInstance.getDefault().getTable("cores");
    private final ShuffleboardTab tab = Shuffleboard.getTab("Elementos");

    private boolean ativado = false;
    private boolean executado = false;
    private int tentativas = 0;
    private static final int MAX_TENTATIVAS = 500;

    private String cor0 = "null";
    private String cor1 = "null";
    private String cor2 = "null";

    private boolean encerrarComando = false;

    public LerConjunto(int indice) {
        this.indice = indice;
    }

    @Override
    public void initialize() {
        // Ativa o processamento Python
        table.getEntry("ativar").setBoolean(true);
        ativado = true;
        executado = false;
        tentativas = 0;
    }

    @Override
    public void execute() {
        if (ativado && !executado) {
            executado = table.getEntry("executado").getBoolean(false);
            if (executado) {
                cor0 = table.getEntry("cor2").getString("null");
                cor1 = table.getEntry("cor1").getString("null");
                cor2 = table.getEntry("cor0").getString("null");

                System.out.println("Cores detectadas: " + cor0 + ", " + cor1 + ", " + cor2);

                banco.mudarCores(indice, cor2, cor1, cor0);

                for (int i = (indice + 1); i >= (indice - 1); i--) {
                    tab.add("bloco"+i, banco.blocos.get(i).toString());
                }

                // Reseta flags e sinaliza fim
                table.getEntry("ativar").setBoolean(false);
                table.getEntry("executado").setBoolean(false);
                encerrarComando = true;
                ativado = false;
            } else {
                tentativas++;
                if (tentativas >= MAX_TENTATIVAS) {
                    System.out.println("Script Python não respondeu a tempo.");
                    // Reseta para tentar de novo depois
                    if (banco.blocos.get(indice - 1).cor == "null" || banco.blocos.get(indice).cor == "null" || banco.blocos.get(indice).cor == "null") {
                        table.getEntry("ativar").setBoolean(true);
                        ativado = true;
                        encerrarComando = false;
                        tentativas = 0;
                    }
                    else 
                        table.getEntry("ativar").setBoolean(false);
                        table.getEntry("executado").setBoolean(false);
                        ativado = false;
                        encerrarComando = true;
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return encerrarComando;
    }
}
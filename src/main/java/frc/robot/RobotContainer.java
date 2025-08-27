package frc.robot;

import java.time.Period;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.performance.*;
import frc.robot.commands.ColetarCaixas;
import frc.robot.commands.MovimentoInicial;
import frc.robot.commands.Teste;
import frc.robot.commands.manipulador.comandosBasicos.GirarBase;
import frc.robot.commands.manipulador.comandosBasicos.MovimentarElevador;
import frc.robot.commands.movimentacao.*;
import frc.robot.commands.movimentacao.teleoperado.Teleop;
import frc.robot.gamepad.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.DriveSubsystem.PosicaoRodas;
import frc.robot.utils.*;

public class RobotContainer {

  public static DriveSubsystem subDrive = new DriveSubsystem();
  public static ManipuladorSubsystem subManipulador = new ManipuladorSubsystem();
  public static QRcodeSubsystem subQRcode = new QRcodeSubsystem();
  //public static PainelSubsystem subPainel = new PainelSubsystem();

  public static OI oi = new OI();

  public static Mapa mapa = new Mapa();

  public static BancoGrupos bancoGrupos = new BancoGrupos();

  public static BancoBlocos bancoBlocos = new BancoBlocos(mapa);
  
  public RobotContainer() {
    
    bancoGrupos.adicionarGrupo(new Grupo(new String[] {"R", "R", "R", "R"}, "A"));
    bancoGrupos.adicionarGrupo(new Grupo(new String[] {"X", "X", "X", "X"}, "B"));
    bancoGrupos.adicionarGrupo(new Grupo(new String[] {"X", "X", "X", "X"}, "C"));
    bancoBlocos.adicionarBloco("null", "C-1");
    bancoBlocos.adicionarBloco("null", "C-2");
    bancoBlocos.adicionarBloco("null", "C-3");
    bancoBlocos.adicionarBloco("null", "C-4");
    bancoBlocos.adicionarBloco("null", "C-5");
    bancoBlocos.adicionarBloco("null", "C-6");
    bancoBlocos.adicionarBloco("null", "D-1");
    bancoBlocos.adicionarBloco("null", "D-2");
    bancoBlocos.adicionarBloco("null", "D-3");
    bancoBlocos.adicionarBloco("null", "D-4");
    bancoBlocos.adicionarBloco("null", "D-5");
    bancoBlocos.adicionarBloco("null", "D-6");
  }

  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    /*return new SequentialCommandGroup(
      new DeslocamentoXYZ(0, -1000, 0),
      new DeslocamentoXYZ(0, 0, 0),
      new DeslocamentoXYZ(0, 0, 90)
    );*/
 
    return new SequentialCommandGroup(
      //new MovimentarElevador(10)
      //new GirarBase("inicial")
      //new Teste(),
      new DeslocamentoXYZ(new Posicao(-600, 0, 90))
      //new MovimentoInicial(),
      /*new AproximarFrente(25)*/
      /*new ObterQRCODE_DefinirGrupos(mapa),
      new LerBlocos_ArmazenarPosicoes(mapa),
      //new MovimentoAstar(mapa, "E", "A")
      new ColetarCaixas(bancoGrupos, bancoBlocos, mapa)*/
      //new MovimentoAstar(mapa, "E", "A")
      /*new MovimentoAstar(mapa, "A", "J"),
      new MovimentoAstar(mapa, "J", "A")*/
      //new LerQRCODE()
      //new AlinharRodas(PosicaoRodas.EIXO_Y)
    );
  }

  public Command getTeleopCommand() {
    return new Teleop(); 
  }
}

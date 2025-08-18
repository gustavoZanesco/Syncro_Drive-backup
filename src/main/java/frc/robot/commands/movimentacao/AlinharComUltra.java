package frc.robot.commands.movimentacao;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;

public class AlinharComUltra extends CommandBase {

    private final DriveSubsystem subBase = RobotContainer.subDrive;
    
    private boolean alinhado = false;
    private double tempoAlvoAtingido = 0;
    private double TEMPO_ESTAVEL_MS = 500;

    public AlinharComUltra() { 
        addRequirements(subBase); /*addRequirements(subManipulador);*/
    }

    @Override
    public void initialize() { subBase.chegouZ = false; }


    double sensor2Passado = 0;

    @Override
    public void execute() { 
        subBase.chegouZ = false;
        //subManipulador.movimentarElevador(15);

        
        /*if ( etapa == 0 && subBase.ultraEsquerdo.getDistancia() > 30 /*&& subManipulador.noSetPoint()*///) { 
            /*subBase.omnidiretional_Global( 0, 13.35, 0 ); 
            if ( subBase.ultraEsquerdo.getDist  ancia() < 32 ) { etapa = 1; }
            
        }*/
        
        double erro = subBase.ultraEsquerdo.getDistancia() - subBase.ultraDireito.getDistancia();
        //boolean endCommand = false;
        double vZr;

        //boolean sensorParado = variacaoSensor2 == 0;

        /*if (estavel(sensorParado)) {
            vZr = 10;
        } else {*/
            vZr = erro * 5;
        //}

        boolean alinhado = Math.abs(erro) <= 0.3;

        if ( alinhado ) { subBase.pararMotoresTracao(); }
        else { subBase.synchroDrive_local(0, 0, vZr); }
    
        this.alinhado = alinhado;
        /*if (estavel(alinhado)) { subBase.resetZ(); }
        this.alinhado = estavel(alinhado);
        SmartDashboard.putString("VZR", vZr + "");*/
    }

    @Override
    public boolean isFinished() { return estavel(alinhado); }

    @Override
    public void end(boolean interrupted) {
        subBase.resetZ();
        subBase.pararMotoresTracao();
        subBase.chegouZ = true;
    }

    public boolean estavel ( boolean alinhado ) {
        if ( alinhado ) {
            if (tempoAlvoAtingido == 0) tempoAlvoAtingido = System.currentTimeMillis();
            return (System.currentTimeMillis() - tempoAlvoAtingido) > TEMPO_ESTAVEL_MS;
        } else {
            tempoAlvoAtingido = 0;
            return false;
        }
    }
}
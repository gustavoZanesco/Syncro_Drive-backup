package frc.robot.commands.movimentacao.teleoperado;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.DriveSubsystem;
//import frc.robot.subsystems.ManipuladorSubsystem;
import frc.robot.subsystems.ManipuladorSubsystem;

public class Teleop extends CommandBase
{

    //PIDControl controleAngulo = new PIDControl();
    /**
     * Bring in Subsystem and Gamepad instances
     */
    private static DriveSubsystem subDrive = RobotContainer.subDrive;
    private static ManipuladorSubsystem subManipulador = RobotContainer.subManipulador;
 
    private static final OI oi = RobotContainer.oi;

    /**
     * Joystick inputs
     */
    double inputLeftY = 0;
    double inputLeftX = 0;
    double inputRightX = 0;
    double inputRightY = 0;


    public Teleop()
    {
        //addRequirements(subDrive);
        //SmartDashboard.putNumber("X desejado", 0);
        //SmartDashboard.putNumber("Y desejado", 0);
        //SmartDashboard.putNumber("Z desejado", 0);
    }

    /**
     * Code here will run once when the command is called for the first time
     */
    @Override
    public void initialize() 
    {
        subDrive.giroscopio.reset();
        subDrive.posicaoDesejadaZ = 0;
        //subDrive.chegouY = false;
        //subDrive.chegouX = false;
        //subDrive.chegouZ = false;
        //subDrive.giroscopio.reset();
        /* SmartDashboard.putNumber("pino controle", 0);
        SmartDashboard.putNumber("angulo Servo Z", 300); 
        SmartDashboard.putNumber("angulo Servo X", 0);
        SmartDashboard.putNumber("angulo Garra", 0);*/
        //SmartDashboard.putNumber("angulacao Theta", 0);
        //SmartDashboard.putNumber("ki Angulo", 0);
        //driveTrain.resetYaw();
        //subDrive.chegouZ = true;
        //subDrive.chegouXYZ = false;
    }
    int contador = 0;

    /**
     * Code here will run continously every robot loop until the command is stopped
     */
    @Override
    public void execute() 
    {
        /**
         * Get Joystick data
         */
        inputLeftX = -oi.getLeftDriveX();
        inputLeftY = oi.getLeftDriveY();
        inputRightX = -oi.getRightDriveX();
        inputRightY = -oi.getRightDriveY();

        /*if (oi.getXis()) { subManipulador.acionarElevador(-5); } 
        else if (oi.getTriangle()) { subManipulador.acionarElevador(50); }
        else { subManipulador.acionarElevador(0); }

        if (!oi.getBotaoResetGiroscopio()) {
            if (oi.getCircle()) { subManipulador.setBase("guardar"); }
            else if (oi.getSquare()) { subManipulador.setBase("pegar"); }
        }
        else {
            if (oi.getCircle()) { subManipulador.setBase("45_graus"); }
            else if (oi.getSquare()) { subManipulador.setBase("-45_graus"); }
        }
        

        if (oi.getDriveLeftBumper()) { subManipulador.setAnguloGarra("0_graus"); }
        else if (oi.getDriveRightBumper()) { subManipulador.setAnguloGarra("90_graus"); }
        
        if (!oi.getBotaoResetGiroscopio()) {
            if (oi.getDriveLeftTrigger()) { subManipulador.moverMesa("bloco1"); }
            else if (oi.getDriveRightTrigger()) { subManipulador.moverMesa("bloco3"); }
            else { subManipulador.moverMesa("central"); }
        }
        else {
            if (oi.getDriveLeftTrigger()) { subManipulador.moverMesa("bloco2"); }
            else if (oi.getDriveRightTrigger()) { subManipulador.moverMesa("bloco4"); }
        }
        
        if (!oi.getBotaoResetGiroscopio()) {
            if (oi.getDriveXButton() ) {
                subManipulador.setEstadoGarra("pegarCubo", true);
            }
            else if (oi.modoSeguroPS4() ) {
                subManipulador.setEstadoGarra("soltarCubo", true); 
            }
        }
        else { 
            if (oi.getDriveXButton() ) {
                subManipulador.setEstadoGarra("pegarPalete", true);
            }
            else if (oi.modoSeguroPS4() ) {
                subManipulador.setEstadoGarra("prepararPalete", true); 
            }
         }

         
         /*else if (oi.getBotaoResetGiroscopio() && oi.getDriveXButton()) {
             subManipulador.setEstadoGarra("soltarPalete");
            }
            
            else if (oi.getBotaoResetGiroscopio() && oi.modoSeguroPS4()) {
                subManipulador.setEstadoGarra("pegarPalete");
            };
            
            else if ( oi.modoSeguroPS4() && (oi.getDriveLeftTrigger() || oi.getDriveRightTrigger())) {
                subManipulador.setEstadoGarra("soltarCubo");
            }*/
            
            
            
            // ativacao dos atuadores com as entradas do controle
            
            /*double variavel = subDrive.giroscopio.getAngle();
            double setpoint = 0;
            
            controleAngulo.setP(0.075);
            controleAngulo.setI(0.05);
            
            double vZ = controleAngulo.calcular(variavel, setpoint);
            
            if (Math.abs(vZ) < 0.05) { vZ = 0; }
            */
        if ( oi.getDriveLeftTrigger() ) { inputLeftX = 0; };
        if ( oi.getDriveRightTrigger() ) { inputLeftY = 0; }; 
        
        subDrive.movimentar(-inputLeftY * 50, -inputRightX * 40);
        
        if (oi.getTriangle()) { subManipulador.acionarElevadorY(40); }
        else if(oi.getXis()) { subManipulador.acionarElevadorY(-40); }
        else { subManipulador.motorEixoY.stop(); }
        
        if (oi.getDriveRightBumper()) { subManipulador.setBase("pegar"); }
        else if (oi.getDriveLeftBumper()) { subManipulador.setBase("guardar"); }
        else {
            subManipulador.motorEixoZ.stop();
        }
        
        if (!oi.getBotaoResetGiroscopio()) {
            if (oi.getDriveXButton() ) {
                subManipulador.setEstadoGarra("pegarCubo", true);
            }
            else if (oi.modoSeguroPS4() ) {
                subManipulador.setEstadoGarra("soltarCubo", true); 
            }
        }
        else { 
            if (oi.getDriveXButton() ) {
                subManipulador.setEstadoGarra("pegarPalete", true);
            }
            else if (oi.modoSeguroPS4() ) {
                subManipulador.setEstadoGarra("prepararPalete", true); 
            }
        }

        if (oi.getSquare()) {
            subManipulador.setEixoX("esquerda");
        }
        else if (oi.getCircle()) {
            subManipulador.setEixoX("direita");
        }

        if (oi.getDriveRightTrigger()) {
            subManipulador.setAnguloGarra("0_graus");
        }
        else if (oi.getDriveLeftTrigger()) {
            subManipulador.setAnguloGarra("90_graus");
        }
        
        if (oi.getPS4Button()) {
            subManipulador.motorEixoY.resetEncoder();
        }
        //if (oi.getBotaoResetGiroscopio()) { new AlinharComUlt ra().schedule(); }
        //subDrive.omnidiretional_Global  (-inputLeftX * 60, -inputLeftY * 60, -inputRightX * 60 );

        

        //subDrive.omnidiretional_XYZ(SmartDashboard.getNumber("X desejado", 0), SmartDashboard.getNumber("Y desejado", 0), SmartDashboard.getNumber("Z desejado", 0));
        ///SmartDashboard.putNumber("vertical esq", -inputLeftX);
        //SmartDashboard.putNumber("horizontal esq", -inputLeftY);
        //SmartDashboard.putNumber("horizontal dir", -inputRightX);
        //if ( oi.getBotaoResetGiroscopio() ) { subBase.giroscopio.reset(); subBase.resetEncoder(); };
    }

    /**     
     * When the comamnd is stopped or interrupted this code is run
     * <p>
     * Good place to stop motors in case of an error
     */
    @Override
    public void end(boolean interrupted)
    {
        subDrive.pararMotoresTracao();
    }

    /**
     * Check to see if command is finished
     */
    @Override
    public boolean isFinished()
    {
        return false;
    }

}
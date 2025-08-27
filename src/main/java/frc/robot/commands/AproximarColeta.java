package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.movimentacao.AproximarDireita;
import frc.robot.commands.movimentacao.AproximarEsquerda;
import frc.robot.commands.movimentacao.AproximarFrente;

public class AproximarColeta extends SequentialCommandGroup {
    boolean ajustarEsquerda = false;

    public AproximarColeta(String cubo) {
        double valorEsquerda = 0;
        double valorDireita = 0;
        switch (cubo) {
            case "C-1":
                valorEsquerda = 125;
                valorDireita = 0;
                ajustarEsquerda = true;
                break;
               
            case "C-2":
                valorEsquerda = 285;
                valorDireita = 0;
                ajustarEsquerda = true;
                break;
        
            case "C-3":
                valorEsquerda = 440;
                valorDireita = 0;
                ajustarEsquerda = true;
                break;

            case "C-4":
                valorEsquerda = 600;
                valorDireita = 0;
                ajustarEsquerda = true;
                break;

            case "C-5":
                valorEsquerda = 760;
                valorDireita = 0;
                ajustarEsquerda = true;
                break;

            case "C-6":
                valorEsquerda = 920;
                valorDireita = 0;
                ajustarEsquerda = true;
                break;

            case "D-1":
                valorEsquerda = 0;
                valorDireita = 925;
                ajustarEsquerda = false;
                break;

            case "D-2":
                valorEsquerda = 0;
                valorDireita = 765;
                ajustarEsquerda = false;
                break;

            case "D-3":
                valorEsquerda = 0;
                valorDireita = 601;
                ajustarEsquerda = false;
                break;

            case "D-4":
                valorEsquerda = 0;
                valorDireita = 450;
                ajustarEsquerda = false;
                break;

            case "D-5":
                valorEsquerda = 0;
                valorDireita = 290;
                ajustarEsquerda = false;
                break;

            case "D-6":
                valorEsquerda = 0;
                valorDireita = 130;
                ajustarEsquerda = false;
                break;
        }

        addCommands(
            new AproximarFrente(25),
            ajustarEsquerda ? new AproximarEsquerda(valorEsquerda) : new AproximarDireita(valorDireita)     
        );
    }
}
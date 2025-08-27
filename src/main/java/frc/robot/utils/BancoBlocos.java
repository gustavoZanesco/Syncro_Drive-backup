package frc.robot.utils;

import java.util.*;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.utils.Mapa;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.utils.Posicao;

public class BancoBlocos {
    public List<Bloco> blocos = new ArrayList<>(12);

    private Mapa mapa;

    ShuffleboardTab tab = Shuffleboard.getTab("Elementos");

    public BancoBlocos(Mapa mapa) {
        this.mapa = mapa;
    }

    public void adicionarBloco(String cor, String pontoNavegavel) {
        if (blocos.contains(new Bloco(cor, pontoNavegavel))) {
            return;
        }
        else { 
            blocos.add(new Bloco(cor, pontoNavegavel));
        }
    }

    public Bloco buscarMaisProximo(String cor, String posRobo) {
        return blocos.stream()
            .filter(b -> b.cor.equals(cor))
            .min(Comparator.comparingDouble(b -> distancia(mapa.getXYPonto(posRobo), mapa.getXYPonto(b.pontoNavegavel))))
            .orElse(null);
    }

    private double distancia(Posicao posrobo, Posicao a) {
        return Math.hypot(posrobo.getX() - a.getX(), posrobo.getY() - a.getY());
    }

    public void removerBloco(Bloco b) {
        blocos.remove(b);
            
    }

    public boolean mudarCores( int indice, String corEsquerda, String corCentro, String corDireita ) {
        blocos.get( indice - 1 ).setCor( corEsquerda );
        blocos.get( indice ).setCor( corCentro );
        blocos.get( indice + 1 ).setCor( corDireita );
        Timer.delay(0.1);
        return true;
    }
}
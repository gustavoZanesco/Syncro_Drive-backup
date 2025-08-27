package frc.robot.utils;

import java.util.Objects;

public class Bloco {
    
    public String cor;
    public String pontoNavegavel;

    public Bloco(String cor, String pontoNavegavel) {
        this.cor = cor;
        this.pontoNavegavel = pontoNavegavel;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    @Override
    public String toString() {
        return "Bloco " + cor + " em (" + pontoNavegavel + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Bloco)) return false;
        Bloco other = (Bloco) obj;
        return cor.equals(other.cor) && pontoNavegavel.equals(other.pontoNavegavel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cor, pontoNavegavel);
    }
}
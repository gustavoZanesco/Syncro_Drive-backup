package frc.robot.utils;

public class Grupo {
    private String[] cores;
    private String palete;

    public Grupo(String[] cores, String palete) {
        if (cores.length < 1 || cores.length > 4) {
            throw new IllegalArgumentException("Grupo deve ter de 1 a 4 cores.");
        }
        this.cores = cores.clone();
        this.palete = palete;
    }

    public String[] getCores() {
        return cores.clone();
    }

    public String getPalete() {
        return palete;
    }

    public void setCores(String[] novasCores) {
        if (novasCores.length < 1 || novasCores.length > 4) {
            throw new IllegalArgumentException("Grupo deve ter de 1 a 4 cores.");
        }
        this.cores = novasCores.clone();
    }

    public void setPalete(String novoPalete) {
        this.palete = novoPalete;
    }

    @Override
    public String toString() {
        return "Grupo [cores=" + String.join(",", cores) + ", palete=" + palete + "]";
    }
}
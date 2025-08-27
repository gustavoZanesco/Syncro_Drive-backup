package frc.robot.utils;

import java.util.ArrayList;
import java.util.List;

public class BancoGrupos {

    public List<Grupo> grupos = new ArrayList<>(3);

    public BancoGrupos() {

    }

    public void adicionarGrupo(Grupo grupo) {
        if (grupos.contains(grupo)) { return; }
        else { grupos.add(grupo); }
    }

    public void modificarGrupo(int indice, Grupo grupo) {
        grupos.get(indice).setCores(grupo.getCores());
        grupos.get(indice).setPalete(grupo.getPalete());
    }
}
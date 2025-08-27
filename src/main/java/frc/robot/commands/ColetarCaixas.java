package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.utils.*;
import frc.robot.commands.manipulador.comandosBasicos.*;
import frc.robot.commands.movimentacao.*;

public class ColetarCaixas extends SequentialCommandGroup {

    private final Mapa mapa;
    private final BancoBlocos banco;
    private final BancoGrupos grupos;

    public ColetarCaixas(BancoGrupos grupos, BancoBlocos banco, Mapa mapa) {
        this.banco = banco;     
        this.mapa = mapa;
        this.grupos = grupos;

        // Monta a sequência completa já no construtor
        addCommands(construirSequencia());
    }

    private SequentialCommandGroup construirSequencia() {
        SequentialCommandGroup sequencia = new SequentialCommandGroup();

        // 3 grupos
        for (int g = 0; g < 3; g++) {
            Grupo grupo = grupos.grupos.get(g);
            String[] cores = grupo.getCores();
            String palete = grupo.getPalete();

            // ---- Blocos ----
            for (int i = 0; i < 4; i++) {
                if (i >= cores.length) break;
                String cor = cores[i];

                Bloco alvo = banco.buscarMaisProximo(cor, palete);
                Posicao destino;
                if (alvo != null) {
                    destino = mapa.getXYPonto(alvo.pontoNavegavel);
                } else {
                    destino = mapa.getXYPonto("fallback");
                }

                sequencia.addCommands(new SequentialCommandGroup(
                    // Buscar bloco mais próximo da cor
                    new DeslocamentoXYZ(destino),
                    new GirarBase("pegar"),
                    new FecharGarra("Cubo", true),
                    new GirarBase("guardar"),
                    new AbrirGarra("Cubo", true),

                    // Remover bloco da lista
                    new InstantCommand(() -> {
                        if (alvo != null) banco.removerBloco(alvo);
                        new ParallelDeadlineGroup(
                            new MovimentoAstar(mapa, alvo.pontoNavegavel, "A"), 
                            new MovimentarElevador(25));
                    })
                ));
            }

            /*Posicao destinoPalete = mapa.getXYPonto(palete);
            // ---- Palete ----
            sequencia.addCommands(new SequentialCommandGroup(
                new DeslocamentoXYZ(destinoPalete),
                new GirarBase("palete"),
                new FecharGarra("Cubo", false),   // Simula entrega
                new AbrirGarra("Cubo", false)
            ));*/
        }

        return sequencia;
    }
}

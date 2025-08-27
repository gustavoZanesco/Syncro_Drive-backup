package frc.robot.utils;

public class SensorFiltro {

    double ultimoTurnoUltra = 0;
    int sensorIndexUltra = 0;
    public final void ultrasonicos( Ultrasonico sensor1, Ultrasonico sensor2 ) {

        final int tempoDeTurno = 24; // [ms]
        double turnoAtual = System.currentTimeMillis();

        if ( turnoAtual - ultimoTurnoUltra >= tempoDeTurno ) {

            // le um sensor e pinga outro e vice-versa
            switch ( sensorIndexUltra ) {
                case 0 : { sensor1.atualizarLeitura(); sensor2.pingTrigger(); break; }
                case 1 : { sensor1.pingTrigger(); sensor2.atualizarLeitura(); break; }
            }

            // troca o estado da variavel
            sensorIndexUltra = 1 - sensorIndexUltra;

            // atualiza o tempo do ultimo turno
            ultimoTurnoUltra = turnoAtual;
        }
    }

    double ultimoTurnoSharp = 0;
    int sensorIndexSharp = 0;
    public final void sharps( Sharp sensor1, Sharp sensor2 ) {

        final int tempoDeTurno = 0; // [ms]
        double turnoAtual = System.currentTimeMillis();

        if ( turnoAtual - ultimoTurnoSharp >= tempoDeTurno ) {

            // le um sensor e pinga outro e vice-versa
            switch ( sensorIndexSharp ) {
                case 0 : { sensor1.atualizarLeitura(); break; }
                case 1 : { sensor2.atualizarLeitura(); break; }
            }

            // troca o estado da variavel
            sensorIndexSharp = 1 - sensorIndexSharp;

            // atualiza o tempo do ultimo turno
            ultimoTurnoSharp = turnoAtual;
        }
    }
}
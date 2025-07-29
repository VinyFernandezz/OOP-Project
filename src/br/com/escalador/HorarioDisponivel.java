//==================================================
// HorarioDisponivel.java
// br.com.escalador
//==================================================
package br.com.escalador;

public class HorarioDisponivel {
    String diaDaSemana;
    String horario;

    public HorarioDisponivel(String diaDaSemana, String horario) {
        this.diaDaSemana = diaDaSemana;
        this.horario = horario;
    }

    @Override
    public String toString() {
        return diaDaSemana + " às " + horario;
    }
}

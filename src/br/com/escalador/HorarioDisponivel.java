//==================================================
// HorarioDisponivel.java
// br.com.escalador
//==================================================
package br.com.escalador;

/**
 * Classe que representa um horário disponível.
 */
public class HorarioDisponivel {
    // PACKAGE-PRIVATE para acesso direto
    String diaDaSemana;
    String horario;

    // Construtor vazio (diretrizes)
    public HorarioDisponivel() {}

    // Construtor com argumentos
    public HorarioDisponivel(String diaDaSemana, String horario) {
        this.diaDaSemana = diaDaSemana;
        this.horario = horario;
    }

    // Getters e setters
    public String getDiaDaSemana() { return diaDaSemana; }
    public void setDiaDaSemana(String diaDaSemana) { this.diaDaSemana = diaDaSemana; }
    
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    @Override
    public String toString() {
        return diaDaSemana + " às " + horario;
    }
}
//==================================================
// Evento.java
// br.com.escalador
//==================================================
package br.com.escalador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evento {
    String descricao;
    String diaDaSemana;
    String horario;
    Map<String, Integer> necessidades;
    Map<String, List<Pessoa>> escalaPronta;

    public Evento(String descricao, String diaDaSemana, String horario, Map<String, Integer> necessidades) {
        this.descricao = descricao;
        this.diaDaSemana = diaDaSemana;
        this.horario = horario;
        this.necessidades = necessidades;
        this.escalaPronta = new HashMap<>();
    }
}

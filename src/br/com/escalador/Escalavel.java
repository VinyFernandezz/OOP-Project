//==================================================
// Escalavel.java
// br.com.escalador
//==================================================
package br.com.escalador;

import java.util.List;

/**
 * Interface que define o contrato para entidades escaláveis.
 */
public interface Escalavel {
    boolean isDisponivel(String dia, String horario);
    List<String> getFuncoesPossiveis();
    boolean podeAcumularFuncoes();
    boolean temFuncao(String funcao);
}

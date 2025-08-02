//==================================================
// ConfiguracaoEscala.java
// br.com.escalador
//==================================================
package br.com.escalador;

import java.util.Arrays;
import java.util.List;

/**
 * Classe de configuração com elementos static e final.
 */
public final class ConfiguracaoEscala {
    
    // Constantes static final
    public static final int MAX_FUNCOES_SIMULTANEAS = 2;
    public static final List<String> INSTRUMENTOS = Arrays.asList(
        "Violão", "Teclado", "Baixo", "Guitarra", "Bateria"
    );
    public static final String FUNCAO_CANTOR = "Cantor";
    
    // Singleton com static
    private static ConfiguracaoEscala instancia;
    
    private ConfiguracaoEscala() {} // Construtor privado
    
    public static ConfiguracaoEscala getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracaoEscala();
        }
        return instancia;
    }
    
    public static boolean ehInstrumento(String funcao) {
        return INSTRUMENTOS.contains(funcao);
    }
}

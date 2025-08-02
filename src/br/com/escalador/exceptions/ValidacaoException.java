//==================================================
// ValidacaoException.java
// br.com.escalador.exceptions
//==================================================
package br.com.escalador.exceptions;

/**
 * Exceção para erros de validação de regras de escalação.
 */
public class ValidacaoException extends EscalacaoException {
    private static final long serialVersionUID = 1L;
    
    public ValidacaoException(String message) {
        super("Erro de validação: " + message);
    }
}

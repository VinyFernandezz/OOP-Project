//==================================================
// EscalacaoException.java
// br.com.escalador.exceptions
//==================================================
package br.com.escalador.exceptions;

/**
 * Exceção base para problemas de escalação.
 */
public class EscalacaoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public EscalacaoException() {
        super();
    }
    
    public EscalacaoException(String message) {
        super(message);
    }
    
    public EscalacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}

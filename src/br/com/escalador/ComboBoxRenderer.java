//==================================================
// ComboBoxRenderer.java
// br.com.escalador
//==================================================
package br.com.escalador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ComboBoxRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
    private static final int ITEM_HEIGHT = 28; // Aumentei um pouco para melhor toque/visual

    public ComboBoxRenderer() {
        setBorder(new EmptyBorder(4, 5, 4, 5));
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Chama o método da superclasse para obter o comportamento padrão
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        // Se o valor for uma Pessoa, exibe o nome dela
        if (value instanceof Pessoa) {
            Pessoa pessoa = (Pessoa) value;
            setText(pessoa.nome);
        } else if (value == null) {
            setText(" -- Selecione -- ");
            setForeground(Color.GRAY);
        }
        
        // Define uma altura preferencial para os itens na lista suspensa
        c.setPreferredSize(new Dimension(c.getPreferredSize().width, ITEM_HEIGHT));
        
        return c;
    }
}

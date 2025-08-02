package br.com.escalador;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;

public class GerenciadorMembrosUI extends JDialog {
	private static final long serialVersionUID = 1L;

    private List<Pessoa> listaDePessoas;
    private DefaultListModel<Pessoa> listModelPessoas;
    private JList<Pessoa> jListMembros;

    private JTextField campoNome;
    private JList<String> jListFuncoes;
    private DefaultListModel<String> listModelFuncoes;
    private JTextArea areaDisponibilidade;
    private JCheckBox checkCantaEToca;
    
    public GerenciadorMembrosUI(Frame owner, List<Pessoa> membros) {
        super(owner, "Gerenciador de Membros", true);
        this.listaDePessoas = membros;

        setSize(850, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Lista de Membros (Esquerda) ---
        listModelPessoas = new DefaultListModel<>();
        listaDePessoas.forEach(listModelPessoas::addElement);
        jListMembros = new JList<>(listModelPessoas);
        jListMembros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(jListMembros), BorderLayout.WEST);

        // --- Formulário de Edição (Direita) ---
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Membro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0; painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        campoNome = new JTextField(20);
        painelFormulario.add(campoNome, gbc);

        // Funções (NOVO LAYOUT)
        gbc.gridx = 0; gbc.gridy = 1; painelFormulario.add(new JLabel("Funções:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weighty = 0.4; gbc.fill = GridBagConstraints.BOTH;
        listModelFuncoes = new DefaultListModel<>();
        jListFuncoes = new JList<>(listModelFuncoes);
        painelFormulario.add(new JScrollPane(jListFuncoes), gbc);

        // Botões para Funções
        JPanel painelBotoesFuncao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddFuncao = new JButton("Adicionar...");
        JButton btnRemFuncao = new JButton("Remover");
        painelBotoesFuncao.add(btnAddFuncao);
        painelBotoesFuncao.add(btnRemFuncao);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(painelBotoesFuncao, gbc);

        // Disponibilidade
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTH;
        painelFormulario.add(new JLabel("Disponibilidade (Dia-HH:mm por linha):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weighty = 0.6; gbc.fill = GridBagConstraints.BOTH;
        areaDisponibilidade = new JTextArea(10, 20);
        painelFormulario.add(new JScrollPane(areaDisponibilidade), gbc);
        
        // Checkbox Canta e Toca
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        checkCantaEToca = new JCheckBox("Consegue cantar e tocar ao mesmo tempo");
        painelFormulario.add(checkCantaEToca, gbc);
        
        add(painelFormulario, BorderLayout.CENTER);
        
        // --- Botões de Ação (Inferior) ---
        JPanel painelBotoesAcao = new JPanel();
        JButton btnNovo = new JButton("Novo Membro");
        JButton btnSalvar = new JButton("Salvar Alterações");
        JButton btnExcluir = new JButton("Excluir Selecionado");
        painelBotoesAcao.add(btnNovo);
        painelBotoesAcao.add(btnSalvar);
        painelBotoesAcao.add(btnExcluir);
        add(painelBotoesAcao, BorderLayout.SOUTH);
        
        // --- Listeners ---
        jListMembros.addListSelectionListener(e -> carregarDadosDoMembroSelecionado());
        btnNovo.addActionListener(e -> limparFormularioParaNovoMembro());
        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnExcluir.addActionListener(e -> excluirMembro());
        btnAddFuncao.addActionListener(e -> adicionarFuncoes());
        btnRemFuncao.addActionListener(e -> removerFuncaoSelecionada());
    }
    
    private void carregarDadosDoMembroSelecionado() {
        Pessoa p = jListMembros.getSelectedValue();
        if (p != null) {
            campoNome.setText(p.getNome());
            listModelFuncoes.clear();
            p.getFuncoes().stream().sorted().forEach(listModelFuncoes::addElement);
        
            String dispTexto = p.getDisponibilidade().stream()
                            .map(d -> d.diaDaSemana + "-" + d.horario)
                            .collect(Collectors.joining("\n"));
            areaDisponibilidade.setText(dispTexto);
            checkCantaEToca.setSelected(p.isCantaEToca());
        }
    }
    
    private void limparFormularioParaNovoMembro() {
        jListMembros.clearSelection();
        campoNome.setText("");
        listModelFuncoes.clear();
        areaDisponibilidade.setText("");
        checkCantaEToca.setSelected(false);
        campoNome.requestFocus();
    }
    
    private void salvarAlteracoes() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pessoa p = jListMembros.getSelectedValue();
        if (p == null) { // Criando um novo membro
            p = new Pessoa(nome);
            atualizarDadosPessoaPeloFormulario(p);
            listaDePessoas.add(p);
            listModelPessoas.addElement(p);
            jListMembros.setSelectedValue(p, true);
        } else { // Atualizando um membro existente
            p.nome = nome;
            atualizarDadosPessoaPeloFormulario(p);
            // Forçar a atualização da lista se o nome mudar
            listModelPessoas.setElementAt(p, jListMembros.getSelectedIndex());
        }
    }

    private void atualizarDadosPessoaPeloFormulario(Pessoa p) {
        p.getFuncoes().clear();
        for (int i = 0; i < listModelFuncoes.size(); i++) {
            p.getFuncoes().add(listModelFuncoes.getElementAt(i));
        }
    
        p.getDisponibilidade().clear();
        String[] linhasDisp = areaDisponibilidade.getText().split("\n");
        for (String linha : linhasDisp) {
            String dispTrimmed = linha.trim();
            if (dispTrimmed.contains("-")) {
                String[] partes = dispTrimmed.split("-", 2);
                if (partes.length == 2 && !partes[0].isEmpty() && !partes[1].isEmpty()) {
                    p.getDisponibilidade().add(new HorarioDisponivel(partes[0].trim(), partes[1].trim()));
                }
            }
        }
        p.setCantaEToca(checkCantaEToca.isSelected());
    }

    private void excluirMembro() {
        Pessoa p = jListMembros.getSelectedValue();
        if (p != null) {
            if (JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir " + p.nome + "?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                listaDePessoas.remove(p);
                listModelPessoas.removeElement(p);
                limparFormularioParaNovoMembro();
            }
        }
    }

    private void adicionarFuncoes() {
        Pessoa pSelecionada = jListMembros.getSelectedValue();
        if(pSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Salve o novo membro antes de adicionar funções.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Coleta todas as funções únicas existentes no sistema
        Set<String> todasAsFuncoes = new HashSet<>();
        for(Pessoa membro : listaDePessoas) {
            todasAsFuncoes.addAll(membro.funcoes);
        }
        
        // Cria a caixa de diálogo de seleção
        List<String> funcoesOrdenadas = new ArrayList<>(todasAsFuncoes);
        Collections.sort(funcoesOrdenadas);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Selecione as funções ou adicione uma nova:"), BorderLayout.NORTH);
        
        DefaultListModel<String> model = new DefaultListModel<>();
        funcoesOrdenadas.forEach(model::addElement);
        JList<String> list = new JList<>(model);
        list.setVisibleRowCount(10);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JTextField novaFuncaoField = new JTextField();
        panel.add(novaFuncaoField, BorderLayout.SOUTH);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Funções", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Adiciona as funções selecionadas na lista
            for(String funcaoSelecionada : list.getSelectedValuesList()) {
                if(!listModelFuncoes.contains(funcaoSelecionada)) {
                    listModelFuncoes.addElement(funcaoSelecionada);
                }
            }
            // Adiciona a nova função do campo de texto, se houver
            String novaFuncao = novaFuncaoField.getText().trim();
            if (!novaFuncao.isEmpty() && !listModelFuncoes.contains(novaFuncao)) {
                listModelFuncoes.addElement(novaFuncao);
            }
        }
    }

    private void removerFuncaoSelecionada() {
        List<String> funcoesARemover = jListFuncoes.getSelectedValuesList();
        if (!funcoesARemover.isEmpty()) {
            for(String f : funcoesARemover) {
                listModelFuncoes.removeElement(f);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma ou mais funções para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}

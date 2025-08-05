package br.com.escalador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    
    // NOVOS COMPONENTES PARA DISPONIBILIDADE
    private JComboBox<String> comboDiaSemana;
    private JComboBox<String> comboHorario;
    private JList<HorarioDisponivel> jListDisponibilidade;
    private DefaultListModel<HorarioDisponivel> listModelDisponibilidade;
    
    private JCheckBox checkCantaEToca;
    
    // Arrays com as opções pré-definidas
    private final String[] DIAS_SEMANA = {
        "Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"
    };
    
    private final String[] HORARIOS = {
        "06:30", "07:30", "17:00", "17:30", "19:00", "19:30", "20:00"
    };
    
    public GerenciadorMembrosUI(Frame owner, List<Pessoa> membros) {
        super(owner, "Gerenciador de Membros", true);
        this.listaDePessoas = membros;

        setSize(900, 650); // Aumentei um pouco para acomodar os novos componentes
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Lista de Membros (Esquerda) ---
        listModelPessoas = new DefaultListModel<>();
        listaDePessoas.forEach(listModelPessoas::addElement);
        jListMembros = new JList<>(listModelPessoas);
        jListMembros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollMembros = new JScrollPane(jListMembros);
        scrollMembros.setPreferredSize(new Dimension(200, 0));
        add(scrollMembros, BorderLayout.WEST);

        // --- Formulário de Edição (Centro) ---
        JPanel painelFormulario = criarPainelFormulario();
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
    }
    
    /**
     * Cria o painel principal do formulário com layout melhorado
     */
    private JPanel criarPainelFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Membro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        campoNome = new JTextField(25);
        painelFormulario.add(campoNome, gbc);

        // Funções
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        painelFormulario.add(new JLabel("Funções:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weighty = 0.3; gbc.fill = GridBagConstraints.BOTH;
        
        JPanel painelFuncoes = criarPainelFuncoes();
        painelFormulario.add(painelFuncoes, gbc);

        // Disponibilidade - NOVA SEÇÃO MELHORADA
        gbc.gridx = 0; gbc.gridy = 2; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        painelFormulario.add(new JLabel("Disponibilidade:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weighty = 0.4; gbc.fill = GridBagConstraints.BOTH;
        
        JPanel painelDisponibilidade = criarPainelDisponibilidade();
        painelFormulario.add(painelDisponibilidade, gbc);
        
        // Checkbox Canta e Toca
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        checkCantaEToca = new JCheckBox("Consegue cantar e tocar ao mesmo tempo");
        painelFormulario.add(checkCantaEToca, gbc);
        
        return painelFormulario;
    }
    
    /**
     * Cria painel para gerenciamento de funções
     */
    private JPanel criarPainelFuncoes() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        
        // Lista de funções
        listModelFuncoes = new DefaultListModel<>();
        jListFuncoes = new JList<>(listModelFuncoes);
        jListFuncoes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollFuncoes = new JScrollPane(jListFuncoes);
        painel.add(scrollFuncoes, BorderLayout.CENTER);
        
        // Botões para funções
        JPanel painelBotoesFuncao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddFuncao = new JButton("Adicionar...");
        JButton btnRemFuncao = new JButton("Remover");
        btnAddFuncao.addActionListener(e -> adicionarFuncoes());
        btnRemFuncao.addActionListener(e -> removerFuncaoSelecionada());
        painelBotoesFuncao.add(btnAddFuncao);
        painelBotoesFuncao.add(btnRemFuncao);
        painel.add(painelBotoesFuncao, BorderLayout.SOUTH);
        
        return painel;
    }
    
    /**
     * NOVO: Cria painel melhorado para gerenciamento de disponibilidade
     */
    private JPanel criarPainelDisponibilidade() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        
        // Painel superior com dropdowns para adicionar disponibilidade
        JPanel painelAdicionar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAdicionar.setBorder(BorderFactory.createTitledBorder("Adicionar Disponibilidade"));
        
        painelAdicionar.add(new JLabel("Dia:"));
        comboDiaSemana = new JComboBox<>(DIAS_SEMANA);
        painelAdicionar.add(comboDiaSemana);
        
        painelAdicionar.add(Box.createHorizontalStrut(10));
        
        painelAdicionar.add(new JLabel("Horário:"));
        comboHorario = new JComboBox<>(HORARIOS);
        comboHorario.setEditable(true); // Permite entrada customizada
        painelAdicionar.add(comboHorario);
        
        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> adicionarDisponibilidade());
        painelAdicionar.add(btnAdicionar);
        
        painel.add(painelAdicionar, BorderLayout.NORTH);
        
        // Lista de disponibilidades atuais
        listModelDisponibilidade = new DefaultListModel<>();
        jListDisponibilidade = new JList<>(listModelDisponibilidade);
        jListDisponibilidade.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Renderizador customizado para exibir disponibilidade de forma legível
        jListDisponibilidade.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof HorarioDisponivel) {
                    HorarioDisponivel h = (HorarioDisponivel) value;
                    setText(h.getDiaDaSemana() + " às " + h.getHorario());
                }
                return c;
            }
        });
        
        JScrollPane scrollDisp = new JScrollPane(jListDisponibilidade);
        scrollDisp.setBorder(BorderFactory.createTitledBorder("Horários Disponíveis"));
        painel.add(scrollDisp, BorderLayout.CENTER);
        
        // Botão para remover disponibilidades
        JPanel painelBotoesDisp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRemoverDisp = new JButton("Remover Selecionados");
        btnRemoverDisp.addActionListener(e -> removerDisponibilidadesSelecionadas());
        painelBotoesDisp.add(btnRemoverDisp);
        painel.add(painelBotoesDisp, BorderLayout.SOUTH);
        
        return painel;
    }
    
    /**
     * NOVO: Adiciona nova disponibilidade baseada nos dropdowns
     */
    private void adicionarDisponibilidade() {
        String dia = (String) comboDiaSemana.getSelectedItem();
        String horario = (String) comboHorario.getSelectedItem();
        
        if (dia != null && horario != null && !horario.trim().isEmpty()) {
            horario = horario.trim();
            
            // Verifica se já existe
            HorarioDisponivel novo = new HorarioDisponivel(dia, horario);
            boolean jaExiste = false;
            
            for (int i = 0; i < listModelDisponibilidade.size(); i++) {
                HorarioDisponivel existente = listModelDisponibilidade.getElementAt(i);
                if (existente.getDiaDaSemana().equals(dia) && existente.getHorario().equals(horario)) {
                    jaExiste = true;
                    break;
                }
            }
            
            if (!jaExiste) {
                listModelDisponibilidade.addElement(novo);
                // Reseta os combos para facilitar próxima entrada
                comboDiaSemana.setSelectedIndex(0);
                comboHorario.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Esta disponibilidade já foi adicionada.", 
                    "Duplicata", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecione dia e horário.", 
                "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * NOVO: Remove disponibilidades selecionadas
     */
    private void removerDisponibilidadesSelecionadas() {
        List<HorarioDisponivel> selecionados = jListDisponibilidade.getSelectedValuesList();
        if (!selecionados.isEmpty()) {
            for (HorarioDisponivel h : selecionados) {
                listModelDisponibilidade.removeElement(h);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione uma ou mais disponibilidades para remover.", 
                "Seleção Necessária", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Carrega dados do membro selecionado - ATUALIZADO para novos componentes
     */
    private void carregarDadosDoMembroSelecionado() {
        Pessoa p = jListMembros.getSelectedValue();
        if (p != null) {
            // Nome
            campoNome.setText(p.getNome());
            
            // Funções
            listModelFuncoes.clear();
            p.getFuncoes().stream().sorted().forEach(listModelFuncoes::addElement);
            
            // Disponibilidade - NOVA LÓGICA
            listModelDisponibilidade.clear();
            for (HorarioDisponivel h : p.getDisponibilidade()) {
                listModelDisponibilidade.addElement(h);
            }
            
            // Canta e toca
            checkCantaEToca.setSelected(p.isCantaEToca());
        }
    }
    
    /**
     * Limpa formulário - ATUALIZADO para novos componentes
     */
    private void limparFormularioParaNovoMembro() {
        jListMembros.clearSelection();
        campoNome.setText("");
        listModelFuncoes.clear();
        listModelDisponibilidade.clear(); // NOVO
        comboDiaSemana.setSelectedIndex(0); // NOVO
        comboHorario.setSelectedIndex(0); // NOVO
        checkCantaEToca.setSelected(false);
        campoNome.requestFocus();
    }
    
    /**
     * Salva alterações - ATUALIZADO para novos componentes
     */
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
        
        JOptionPane.showMessageDialog(this, "Membro salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Atualiza dados da pessoa - ATUALIZADO para novos componentes
     */
    private void atualizarDadosPessoaPeloFormulario(Pessoa p) {
        // Funções
        p.getFuncoes().clear();
        for (int i = 0; i < listModelFuncoes.size(); i++) {
            p.getFuncoes().add(listModelFuncoes.getElementAt(i));
        }
    
        // Disponibilidade - NOVA LÓGICA
        p.getDisponibilidade().clear();
        for (int i = 0; i < listModelDisponibilidade.size(); i++) {
            HorarioDisponivel h = listModelDisponibilidade.getElementAt(i);
            p.getDisponibilidade().add(new HorarioDisponivel(h.getDiaDaSemana(), h.getHorario()));
        }
        
        // Canta e toca
        p.setCantaEToca(checkCantaEToca.isSelected());
    }

    /**
     * Exclui membro selecionado
     */
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

    /**
     * Adiciona funções (mantido do código original)
     */
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

    /**
     * Remove função selecionada (mantido do código original)
     */
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
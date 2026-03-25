package rsa_didattico;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import javax.swing.border.*;

public class RSAGUI extends JFrame implements ActionListener {

    private RSACore rsa;

    private JTextField txtP, txtQ, txtN, txtM, txtE, txtK;
    private JTextField txtNumeroDaCifrare, txtNumeroCifrato, txtRisultato;
    private JButton btnGenera, btnCifra, btnDecifra;

    public RSAGUI() {
        rsa = new RSACore();
        initUI();
    }

    private void initUI() {
        setTitle("Crittografia RSA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 247));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createKeyPanel(), BorderLayout.NORTH);
        centerPanel.add(createOperationPanel(), BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(88, 86, 214));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Sistema di Crittografia RSA");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Sviluppato da Nesticò Giuseppe, Sacco Gianluca, Cristofaro Giacomo");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(220, 220, 255));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(new Color(88, 86, 214));
        textPanel.add(title);
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }

    private JPanel createKeyPanel() {
        JPanel keyPanel = new JPanel(new BorderLayout(10, 10));
        keyPanel.setBackground(Color.WHITE);
        keyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel keysTitle = new JLabel("Generazione Chiavi RSA");
        keysTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        keysTitle.setForeground(new Color(55, 65, 81));

        JPanel keysGrid = new JPanel(new GridLayout(6, 2, 10, 15));
        keysGrid.setBackground(Color.WHITE);
        keysGrid.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        keysGrid.add(createStyledLabel("Numero Primo p:"));
        txtP = createStyledTextField(false);
        keysGrid.add(txtP);

        keysGrid.add(createStyledLabel("Numero Primo q:"));
        txtQ = createStyledTextField(false);
        keysGrid.add(txtQ);

        keysGrid.add(createStyledLabel("Modulo n = p x q:"));
        txtN = createStyledTextField(false);
        keysGrid.add(txtN);

        keysGrid.add(createStyledLabel("φ(n) = (p-1) x (q-1):"));
        txtM = createStyledTextField(false);
        keysGrid.add(txtM);

        keysGrid.add(createStyledLabel("Chiave Pubblica e:"));
        txtE = createStyledTextField(false);
        keysGrid.add(txtE);

        keysGrid.add(createStyledLabel("Chiave Privata d:"));
        txtK = createStyledTextField(false);
        keysGrid.add(txtK);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGenera = createStyledButton("GENERA NUOVE CHIAVI", new Color(88, 86, 214));
        btnGenera.addActionListener(this);
        buttonPanel.add(btnGenera);

        keyPanel.add(keysTitle, BorderLayout.NORTH);
        keyPanel.add(keysGrid, BorderLayout.CENTER);
        keyPanel.add(buttonPanel, BorderLayout.SOUTH);

        return keyPanel;
    }

    private JPanel createOperationPanel() {
        JPanel opPanel = new JPanel(new BorderLayout(10, 10));
        opPanel.setBackground(Color.WHITE);
        opPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel opsTitle = new JLabel("Operazioni di Crittografia");
        opsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        opsTitle.setForeground(new Color(55, 65, 81));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblInput = createStyledLabel("Numero da Cifrare:");
        lblInput.setHorizontalAlignment(SwingConstants.RIGHT);
        inputPanel.add(lblInput, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtNumeroDaCifrare = createStyledTextField(true);
        inputPanel.add(txtNumeroDaCifrare, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel(""), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        btnCifra = createStyledButton("CIFRA", new Color(34, 197, 94));
        btnCifra.addActionListener(this);
        inputPanel.add(btnCifra, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel lblCifrato = createStyledLabel("Numero Cifrato:");
        lblCifrato.setHorizontalAlignment(SwingConstants.RIGHT);
        inputPanel.add(lblCifrato, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtNumeroCifrato = createStyledTextField(false);
        inputPanel.add(txtNumeroCifrato, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel(""), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        btnDecifra = createStyledButton("DECIFRA", new Color(239, 68, 68));
        btnDecifra.addActionListener(this);
        inputPanel.add(btnDecifra, gbc);


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel lblRisultato = createStyledLabel("Risultato Decifrato:");
        lblRisultato.setHorizontalAlignment(SwingConstants.RIGHT);
        inputPanel.add(lblRisultato, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtRisultato = createStyledTextField(false);
        inputPanel.add(txtRisultato, gbc);

        opPanel.add(opsTitle, BorderLayout.NORTH);
        opPanel.add(inputPanel, BorderLayout.CENTER);

        return opPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(75, 85, 99));
        return label;
    }

    private JTextField createStyledTextField(boolean editable) {
        JTextField field = new JTextField();
        field.setEditable(editable);
        field.setFont(new Font("Monospaced", Font.PLAIN, 12));
        field.setBackground(new Color(250, 250, 252));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 215), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGenera) {
            generaChiavi();
        } else if (e.getSource() == btnCifra) {
            cifra();
        } else if (e.getSource() == btnDecifra) {
            decifra();
        }
    }

    private void generaChiavi() {
        if (rsa.generaChiavi()) {
            BigInteger[] valori = rsa.getTuttiValori();
            txtP.setText(valori[0].toString());
            txtQ.setText(valori[1].toString());
            txtN.setText(valori[2].toString());
            txtM.setText(valori[3].toString());
            txtE.setText(valori[4].toString());
            txtK.setText(valori[5].toString());
            setTitle("RSA - Chiavi Generate");
            showTemporaryMessage("Chiavi generate con successo!", new Color(34, 197, 94));
        } else {
            showTemporaryMessage("Errore durante la generazione delle chiavi!", new Color(239, 68, 68));
        }
    }

    private void cifra() {
        try {
            BigInteger M = new BigInteger(txtNumeroDaCifrare.getText());
            BigInteger[] pub = rsa.getChiavePubblica();
            BigInteger X = rsa.cifra(M, pub[0], pub[1]);

            if (X != null) {
                txtNumeroCifrato.setText(X.toString());
                showTemporaryMessage("Cifratura completata con successo!", new Color(34, 197, 94));
            } else {
                txtNumeroCifrato.setText("");
                showTemporaryMessage("Errore: Numero troppo grande per il modulo!", new Color(239, 68, 68));
            }
        } catch (Exception ex) {
            txtNumeroCifrato.setText("");
            showTemporaryMessage("Errore: Input non valido!", new Color(239, 68, 68));
        }
    }

    private void decifra() {
        try {
            BigInteger X = new BigInteger(txtNumeroCifrato.getText());
            BigInteger[] priv = rsa.getChiavePrivata();
            BigInteger M = rsa.decifra(X, priv[0], priv[1]);

            if (M != null) {
                txtRisultato.setText(M.toString());
                showTemporaryMessage("Decifratura completata con successo!", new Color(34, 197, 94));
            } else {
                txtRisultato.setText("");
                showTemporaryMessage("Decifratura fallita!", new Color(239, 68, 68));
            }
        } catch (Exception ex) {
            txtRisultato.setText("");
            showTemporaryMessage("Errore: Numero cifrato non valido!", new Color(239, 68, 68));
        }
    }

    private void showTemporaryMessage(String message, Color color) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(this, "");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Timer timer = new Timer(1500, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new RSAGUI());
    }
}
package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DuckPanel extends JPanel {

    private JTextArea chatArea;
    private JTextField inputField;
    private DuckService duckService; // Servis za komunikaciju sa AI

    public DuckPanel() {
        this.duckService = new DuckService();
        setLayout(new BorderLayout());

        // 1. Slika patkice (mora biti u src/main/resources/duck.png)
        // Ako nemas sliku jos, ovaj red ce baciti gresku, zakomentarisi ga privremeno
        // JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/duck.png")));
        JLabel iconLabel = new JLabel("🦆", SwingConstants.CENTER); // Privremeni emoji dok ne nadjes sliku
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 50));
        add(iconLabel, BorderLayout.NORTH);

        // 2. Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // 3. Input polje
        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);

        // 4. Akcija na ENTER
        inputField.addActionListener(e -> {
            String userText = inputField.getText();
            if (userText.trim().isEmpty()) return;

            // Prikazi korisnikov tekst odmah
            appendChat("Ti: " + userText);
            inputField.setText("");

            // POZIV SERVERA (Ovo mora u novi thread da ne blokira UI!)
            new Thread(() -> {
                String response = duckService.askTheDuck(userText);

                // Vracanje rezultata na UI thread
                SwingUtilities.invokeLater(() -> {
                    appendChat("Patkica: " + response);
                });
            }).start();
        });
    }

    private void appendChat(String text) {
        chatArea.append(text + "\n\n");
        // Auto-scroll do dna
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
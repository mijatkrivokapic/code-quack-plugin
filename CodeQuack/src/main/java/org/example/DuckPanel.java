package org.example;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DuckPanel extends JPanel {

    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private DuckService duckService;

    public DuckPanel() {
        this.duckService = new DuckService();
        setLayout(new BorderLayout());
        setBackground(UIUtil.getPanelBackground());

        // Header with duck icon
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(UIUtil.getPanelBackground());
        headerPanel.setBorder(JBUI.Borders.empty(8));

        Icon duckIcon = IconLoader.getIcon("/META-INF/duck.svg", DuckPanel.class);
        JLabel iconLabel = new JLabel(duckIcon);
        JLabel titleLabel = new JLabel("Rubber Duck Debugging");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createHorizontalStrut(8));
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Chat area with messages
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(UIUtil.getPanelBackground());
        chatPanel.setBorder(JBUI.Borders.empty(10));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(UIUtil.getPanelBackground());
        inputPanel.setBorder(JBUI.Borders.empty(8));

        inputField = new JTextField();
        inputField.putClientProperty("JTextField.placeholderText", "Ask the duck...");
        inputField.setBorder(JBUI.Borders.empty(8));

        inputPanel.add(inputField, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Action on ENTER
        inputField.addActionListener(e -> {
            String userText = inputField.getText();
            if (userText.trim().isEmpty()) return;

            addMessage(userText, true);
            inputField.setText("");

            // Show typing indicator
            JPanel typingIndicator = createTypingIndicator();
            chatPanel.add(typingIndicator);
            chatPanel.revalidate();
            scrollToBottom();

            // Call service in background thread
            new Thread(() -> {
                String response = duckService.askTheDuck(userText);

                SwingUtilities.invokeLater(() -> {
                    chatPanel.remove(typingIndicator);
                    addMessage(response, false);
                });
            }).start();
        });
    }

    private void addMessage(String text, boolean isUser) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        messagePanel.setBackground(UIUtil.getPanelBackground());
        messagePanel.setBorder(JBUI.Borders.empty(4, 0));

        if (isUser) {
            messagePanel.add(Box.createHorizontalGlue());
        }

        JPanel bubble = createMessageBubble(text, isUser);
        messagePanel.add(bubble);

        if (!isUser) {
            messagePanel.add(Box.createHorizontalGlue());
        }

        chatPanel.add(messagePanel);
        chatPanel.revalidate();
        scrollToBottom();
    }

    private JPanel createMessageBubble(String text, boolean isUser) {
        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        Color bgColor = isUser
                ? new JBColor(new Color(0x2B7DE1), new Color(0x3574C9))
                : new JBColor(new Color(0xEBECF0), new Color(0x3C3F41));

        Color textColor = isUser
                ? new JBColor(Color.WHITE, Color.WHITE)
                : UIUtil.getLabelForeground();

        bubble.setBackground(bgColor);
        bubble.setBorder(JBUI.Borders.empty(8, 12));

        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setForeground(textColor);
        textArea.setFont(UIUtil.getLabelFont());
        textArea.setBorder(null);

        bubble.add(textArea, BorderLayout.CENTER);
        return bubble;
    }

    private JPanel createTypingIndicator() {
        JPanel indicator = new JPanel(new FlowLayout(FlowLayout.LEFT));
        indicator.setBackground(UIUtil.getPanelBackground());
        indicator.setBorder(JBUI.Borders.empty(4, 0));

        JPanel bubble = new JPanel();
        bubble.setBackground(new JBColor(new Color(0xEBECF0), new Color(0x3C3F41)));
        bubble.setBorder(JBUI.Borders.empty(8, 12));

        JLabel label = new JLabel("Duck is thinking...");
        label.setForeground(UIUtil.getLabelForeground());
        bubble.add(label);

        indicator.add(bubble);
        return indicator;
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
}
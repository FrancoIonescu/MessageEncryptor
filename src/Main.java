import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main extends JFrame {
    private JTextField messageField;
    private JPasswordField passwordEncryptField;
    private JPasswordField passwordDecryptField;
    private File selectedFile;
    private JLabel fileLabel;
    private JTextArea decryptedTextArea;

    public Main() {
        setTitle("🔒 Message Encryptor");
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color textGreen = new Color(0x00FF66);

        ImageIcon bgImage = new ImageIcon("background.jpg");
        JLabel backgroundLabel = new JLabel(bgImage);
        backgroundLabel.setLayout(new BoxLayout(backgroundLabel, BoxLayout.Y_AXIS));
        backgroundLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel encryptPanel = new JPanel(new GridBagLayout());
        TitledBorder encryptBorder = BorderFactory.createTitledBorder("🔐 Encrypt Message");
        encryptBorder.setTitleColor(textGreen);
        encryptBorder.setTitleFont(new Font(null, Font.PLAIN, 18));
        encryptPanel.setBorder(encryptBorder);
        encryptPanel.setBackground(new Color(0x212121, true));
        encryptPanel.setOpaque(true);
        encryptPanel.setPreferredSize(new Dimension(500, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setForeground(textGreen);
        gbc.gridx = 0; gbc.gridy = 0;
        encryptPanel.add(messageLabel, gbc);

        gbc.gridx = 1;
        messageField = new JTextField(20);
        messageField.setFont(defaultFont);
        encryptPanel.add(messageField, gbc);

        JLabel passwordLabelEncrypt = new JLabel("Password:");
        passwordLabelEncrypt.setForeground(textGreen);
        gbc.gridx = 0; gbc.gridy = 1;
        encryptPanel.add(passwordLabelEncrypt, gbc);

        gbc.gridx = 1;
        passwordEncryptField = new JPasswordField(20);
        passwordEncryptField.setFont(defaultFont);
        encryptPanel.add(passwordEncryptField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton encryptButton = new JButton("Encrypt & Save");
        encryptButton.setFont(defaultFont);
        encryptButton.setBackground(new Color(0x149414));
        encryptButton.setForeground(Color.WHITE);
        encryptButton.setFocusPainted(false);
        encryptButton.addActionListener(e -> encryptAndSave());
        encryptPanel.add(encryptButton, gbc);

        JPanel decryptPanel = new JPanel(new GridBagLayout());
        TitledBorder decryptBorder = BorderFactory.createTitledBorder("🔓 Decrypt Message");
        decryptBorder.setTitleColor(textGreen);
        decryptBorder.setTitleFont(new Font(null, Font.PLAIN, 18));
        decryptPanel.setBorder(decryptBorder);
        decryptPanel.setBackground(new Color(0x212121, true));
        decryptPanel.setOpaque(true);
        decryptPanel.setPreferredSize(new Dimension(500, 200));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        fileLabel = new JLabel("Drag & Drop a File Here or Click", SwingConstants.CENTER);
        fileLabel.setPreferredSize(new Dimension(300, 60));
        fileLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        fileLabel.setOpaque(true);
        fileLabel.setBackground(Color.LIGHT_GRAY);
        fileLabel.setFont(defaultFont);
        fileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fileLabel.setVerticalAlignment(SwingConstants.CENTER);
        decryptPanel.add(fileLabel, gbc);

        fileLabel.setDropTarget(new DropTarget() {
            public void drop(DropTargetDropEvent event) {
                try {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        List<File> files = (List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if (!files.isEmpty()) {
                            selectedFile = files.get(0);
                            fileLabel.setText("Selected: " + selectedFile.getName());
                            fileLabel.setToolTipText(selectedFile.getAbsolutePath());
                        }
                    }
                } catch (Exception ex) {
                    fileLabel.setText("Error: Could not read file.");
                }
            }
        });

        fileLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(Main.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    fileLabel.setText("Selected: " + selectedFile.getName());
                    fileLabel.setToolTipText(selectedFile.getAbsolutePath());
                }
            }
        });


        JLabel passwordLabelDecrypt = new JLabel("Password:");
        passwordLabelDecrypt.setForeground(textGreen);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        decryptPanel.add(passwordLabelDecrypt, gbc);

        gbc.gridx = 1;
        passwordDecryptField = new JPasswordField(20);
        passwordDecryptField.setFont(defaultFont);
        decryptPanel.add(passwordDecryptField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setFont(defaultFont);
        decryptButton.setBackground(new Color(0x149414));
        decryptButton.setForeground(Color.WHITE);
        decryptButton.setFocusPainted(false);
        decryptButton.addActionListener(e -> decryptFile());
        decryptPanel.add(decryptButton, gbc);

        decryptedTextArea = new JTextArea("Decrypted message will appear here");
        decryptedTextArea.setFont(defaultFont);
        decryptedTextArea.setForeground(textGreen);
        decryptedTextArea.setBackground(new Color(0x1E1E1E));
        decryptedTextArea.setLineWrap(true);
        decryptedTextArea.setWrapStyleWord(true);
        decryptedTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(decryptedTextArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        backgroundLabel.add(encryptPanel);
        backgroundLabel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundLabel.add(decryptPanel);
        backgroundLabel.add(Box.createRigidArea(new Dimension(0, 10)));
        backgroundLabel.add(scrollPane);

        add(backgroundLabel, BorderLayout.CENTER);
    }

    private void encryptAndSave() {
        String message = messageField.getText();
        String password = new String(passwordEncryptField.getPassword());

        if (message.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Message and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Encrypted File");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String encryptedPassword = EncryptionAlgorithm.encryptMessage(password);
            String encryptedMessage = EncryptionAlgorithm.encryptMessage(message);

            boolean success = EncryptionAlgorithm.createFile(fileToSave.getAbsolutePath(), encryptedPassword, encryptedMessage);

            if (success) {
                JOptionPane.showMessageDialog(this, "Encrypted message saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void decryptFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "No file selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = new String(passwordDecryptField.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String decryptedMessage = requestDecryptMessage(password, selectedFile.getPath());
        if (decryptedMessage == null) {
            decryptedTextArea.setText("Incorrect password or invalid file!");
        } else {
            decryptedTextArea.setText("Decrypted: " + decryptedMessage);
        }
    }

    private String requestDecryptMessage(String password, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(" ", 2);
                if (parts.length == 2) {
                    String storedPassword = EncryptionAlgorithm.decryptMessage(parts[0]);
                    String encryptedMessage = parts[1];

                    if (storedPassword.equals(password)) {
                        return EncryptionAlgorithm.decryptMessage(encryptedMessage);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the file!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {
    private JTextField searchField;
    private JTextArea originalArea, filteredArea;
    private Path selectedFile;

    public DataStreamsGUI() {
        setTitle("Data Streams Search");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout());
        JButton loadButton = new JButton("Load File");
        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");
        searchField = new JTextField(15);

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(quitButton);
        add(topPanel, BorderLayout.NORTH);

        originalArea = new JTextArea();
        filteredArea = new JTextArea();
        originalArea.setEditable(false);
        filteredArea.setEditable(false);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(originalArea),
                new JScrollPane(filteredArea)
        );
        add(splitPane, BorderLayout.CENTER);

        loadButton.addActionListener(this::loadFile);
        searchButton.addActionListener(this::searchFile);
        quitButton.addActionListener(e -> System.exit(0));
    }

    private void loadFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile().toPath();
            originalArea.setText("");
            filteredArea.setText("");
            try (Stream<String> lines = Files.lines(selectedFile)) {
                lines.forEach(line -> originalArea.append(line + "\n"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading file.");
            }
        }
    }

    private void searchFile(ActionEvent e) {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Load a file first!");
            return;
        }
        filteredArea.setText("");
        String search = searchField.getText().trim().toLowerCase();
        try (Stream<String> lines = Files.lines(selectedFile)) {
            lines.filter(line -> line.toLowerCase().contains(search))
                    .forEach(line -> filteredArea.append(line + "\n"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error searching file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataStreamsGUI().setVisible(true));
    }
}

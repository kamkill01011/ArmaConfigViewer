package ch.armaconfigviewer.main.GUI;

import ch.armaconfigviewer.main.CfgClass;
import ch.armaconfigviewer.main.Parser;
import ch.armaconfigviewer.main.Util.Tree;
import ch.armaconfigviewer.main.web.Web;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Optional;

public class MainFrame extends JFrame {
    private JLabel statusLabel = new JLabel();
    private Parser parser = new Parser();

    public MainFrame() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        JPanel inputFilePanel = new JPanel();
        JPanel outputFolderPanel = new JPanel();
        JPanel generatePanel = new JPanel();

        //input file
        inputFilePanel.add(new JLabel("Input file: "));
        JTextField inputFileText = new JTextField(30);
        inputFileText.setText(System.getProperty("user.home") + "\\AppData\\Local\\Arma 3");
        inputFilePanel.add(inputFileText);
        JButton inputFileButton = new JButton("Browse");
        inputFileButton.addActionListener(e -> {
            getInputFile(inputFileText.getText()).ifPresent(inputFileText::setText);
            panel.revalidate();
            panel.repaint();
        });
        inputFilePanel.add(inputFileButton);

        //output file
        outputFolderPanel.add(new JLabel("Output folder: "));
        JTextField outputFolderText = new JTextField(30);
        outputFolderText.setText(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        outputFolderPanel.add(outputFolderText);
        JButton outputFolderButton = new JButton("Browse");
        outputFolderButton.addActionListener(e -> {
            getOutputFolder(outputFolderText.getText()).ifPresent(outputFolderText::setText);
            panel.revalidate();
            panel.repaint();
        });
        outputFolderPanel.add(outputFolderButton);

        //generate
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> {
            new Thread(() -> {
                generateButton.setEnabled(false);

                StatusThread statusThread = new StatusThread(statusLabel);
                new Thread(statusThread).start();
                parser.addObserver(statusThread);

                List<Tree> trees = parser.parseFile(inputFileText.getText());
                long totalLeafs = 0L;
                for(Tree tree : trees) {
                    tree.addObserver(statusThread);
                    totalLeafs += tree.getNumberOfLeafs();
                }
                statusThread.setTotalLeafs(totalLeafs);

                Web web = new Web();
                String temp = outputFolderText.getText();
                if(!temp.endsWith("\\")) temp += '\\';
                String outputFolder = temp;
                web.copyCss(outputFolder);

                for(Tree tree : trees) tree.browse(node -> {
                    if(node.getItem() instanceof CfgClass) web.makeClassHtml((CfgClass) node.getItem(), tree, outputFolder);
                });

                statusThread.setRunning(false);
                statusLabel.setText(statusLabel.getText() + " - DONE !");

                generateButton.setEnabled(true);
            }).start();
        });
        generatePanel.add(generateButton);
        generatePanel.add(statusLabel);

        panel.add(inputFilePanel);
        panel.add(outputFolderPanel);
        panel.add(generatePanel);

        add(panel);
        setTitle("Arma Config Viewer");
        setSize(600, 150);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Optional<String> getInputFile(String path) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose output file");
        chooser.setCurrentDirectory(new File(path));
        FileFilter rtpFilter = new FileNameExtensionFilter("RPT File", "rpt");
        chooser.addChoosableFileFilter(rtpFilter);
        chooser.setFileFilter(rtpFilter);
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) return Optional.of(chooser.getSelectedFile().getPath());
        else return Optional.empty();
    }

    private Optional<String> getOutputFolder(String path) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose input file");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setFileHidingEnabled(false);
        chooser.setCurrentDirectory(new File(path));
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) return Optional.of(chooser.getSelectedFile().getPath());
        else return Optional.empty();
    }

    public void setStatusLabelText(String s) {
        statusLabel.setText(s);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}

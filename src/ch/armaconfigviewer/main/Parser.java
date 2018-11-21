package ch.armaconfigviewer.main;

import ch.armaconfigviewer.main.Util.Tree;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Parser extends Observable {
    private final String MARKER_CLASS = "[\"KAM_ACV_Class\",";
    private final String MARKER_PROPERTY = "[\"KAM_ACV_Property\",";
    private final String LOG_LINE_START = "hh:mm:ss ";
    private long numberOfLineProcessed = 0L;

    public String cleanLine(String line, String marker) {
        return line.substring(LOG_LINE_START.length() + marker.length() + 1, line.length() - 2);
    }

    public CfgClass cleanEntry(String line) {
        return new CfgClass(line.substring(line.indexOf(',') + 1), line.substring(0, line.indexOf(',')));
    }

    public CfgProperty cleanProperty(String line) {
        return new CfgProperty<>(line.substring(0, line.indexOf(',')), line.substring(line.indexOf(',') + 1));
    }

    public List<Tree> parseFile(String inputFile) {
        List<Tree> trees = new LinkedList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))) {
            String s;//line read
            List<CfgItem> notAdded = new LinkedList<>();
            while ((s = in.readLine()) != null) {
                if(s.contains(MARKER_CLASS)) {//if line is marked as a class by the script
                    setChanged();
                    notifyObservers();
                    CfgClass cfgClass = cleanEntry(cleanLine(s, MARKER_CLASS));
                    if(!tryToAddLeaf(trees, cfgClass)) {
                        notAdded.add(cfgClass);
                        if(cfgClass.getPath().length == 0) {
                            trees.add(new Tree(cfgClass));
                        }
                    }

                } else if(s.contains(MARKER_PROPERTY)) {//if line is marked as a property by the script
                    setChanged();
                    notifyObservers();
                    CfgProperty property = cleanProperty(cleanLine(s, MARKER_PROPERTY));
                    if(!tryToAddLeaf(trees, property)) {
                        notAdded.add(property);
                    }

                }
                numberOfLineProcessed++;
            }
            for(CfgItem item : notAdded) tryToAddLeaf(trees, item);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return trees;
    }

    private boolean tryToAddLeaf(List<Tree> trees,CfgItem cfgItem) {
        if(cfgItem.getPath().length == 0) return false;
        for(Tree tree : trees) {
            if(cfgItem.getPath()[0].equals(tree.getRootName())) {
                tree.addLeaf(cfgItem);
                return true;
            }
        }
        return false;
    }

    public long getNumberOfLineProcessed() {
        return numberOfLineProcessed;
    }
}

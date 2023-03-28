package ch.armaconfigviewer.main;

import ch.armaconfigviewer.main.Util.Tree;

import java.util.Optional;

public abstract class CfgItem<T> {
    private String[] entry;//path + name on the item
    private T value;//value of the property or parent on the class
    private Tree.Node node = null;//node of the tree for faster access (optional)

    public CfgItem(String entry, T value) {
        this.entry = parseEntry(entry);
        this.value = value;
    }

    public CfgItem(String[] entry, T value) {
        this.entry = entry;
        this.value = value;
    }

    /**
     *
     * @param entry string to parse in a array
     * @return array of the entry
     */
    public static String[] parseEntry(String entry) {
        String[] temp = entry.split("/");
        String[] result = new String[temp.length - 1];
        System.arraycopy(temp, 1, result, 0, result.length);
        return result;
    }

    public String getName() {
        return entry[entry.length - 1];
    }

    public String[] getPath() {
        String[] path = new String[entry.length - 1];
        System.arraycopy(entry, 0, path, 0, entry.length - 1);
        return path;
    }

    public String[] getEntry() {
        return entry;
    }

    public String getEntryAsFile() {
        return String.join("\\", entry);
    }

    public T getValue() {
        return value;
    }

    @Override
    public abstract String toString();

    public Optional<Tree.Node> getNode() {
        return node == null ? Optional.empty() : Optional.of(node);
    }

    public void setNode(Tree.Node node) {
        this.node = node;
    }
}

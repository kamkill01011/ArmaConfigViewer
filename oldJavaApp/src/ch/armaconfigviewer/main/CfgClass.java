package ch.armaconfigviewer.main;

import ch.armaconfigviewer.main.Util.Tree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CfgClass extends CfgItem<String[]> {

    public CfgClass(String parent, String entry) {
        super(entry, parent.isEmpty() ? null : parseEntry(parent));
    }

    public CfgClass(String parent, String[] entry) {
        super(entry, parent.isEmpty() ? null : parseEntry(parent));
    }

    public CfgClass(String[] parent, String[] entry) {
        super(entry, parent);
    }

    public String[] getParentPath() {
        String[] path = new String[getValue().length - 1];
        System.arraycopy(getValue(), 0, path, 0, getValue().length - 1);
        return path;
    }

    /**
     *
     * @param properties list of properties
     * @param property property to check if exist
     * @return if property is already if properties
     */
    public boolean hasProperty(List<CfgItem> properties, String property) {
        return properties.stream().filter(item -> item.getName().equals(property)).findFirst().orElse(null) != null;
    }

    public String getParent() {
        return getValue() == null ? null : getValue()[getValue().length - 1];
    }

    /**
     *
     * @param tree tree where class is stored
     * @return all class specific properties (created or modified by the class), but not inherited properties
     */
    public List<CfgItem> getProperties(Tree tree) {
        List<CfgItem> properties = new LinkedList<>();
        for (Tree.Node node : getNode().orElse(tree.getLeaf(getEntry())).getLeafs()) {
            properties.add(node.getItem());
        }

        return properties;
    }

    /**
     *
     * @param tree tree where class is stored
     * @return all properties including inherited properties
     */
    public List<CfgItem> getAllProperties(Tree tree) {
        List<CfgItem> allProperties = new LinkedList<>(getProperties(tree));

        //if no parent return what you have
        if(getParent() == null) return allProperties;

        //in cfgClass value is the entry of the parent class
        Tree.Node nodeOfParent = tree.getLeaf(getValue());//not the parent node! the node of the parent class
        while (nodeOfParent != null) {
            for (Tree.Node node : nodeOfParent.getLeafs()) {
                if(!hasProperty(allProperties, node.getItem().getName())) allProperties.add(node.getItem());//add parent property if it is not already override
            }
            nodeOfParent = tree.getLeaf(((CfgClass) nodeOfParent.getItem()).getValue());//get the parent of the parent
        }

        return allProperties;
    }

    public List<CfgItem> getAllSortedProperties(Tree tree) {
        List<CfgItem> allProperties = getAllProperties(tree);
        allProperties.sort(Comparator.comparing(item -> item.getName().toLowerCase()));
        return allProperties;
    }

    @Override
    public String toString() {
        return getEntry()[getEntry().length - 1] + ":" + (getValue() == null ? "" : getParent());
    }

    public String displayStruct(Tree tree) {
        StringBuilder result = new StringBuilder(Arrays.toString(getPath()) + "/" + toString() + '\n');
        for(CfgItem item : getAllSortedProperties(tree)) result.append('\t').append(item.toString()).append('\n');
        return result.toString();
    }
}

package ch.armaconfigviewer.main.Util;

import ch.armaconfigviewer.main.CfgClass;
import ch.armaconfigviewer.main.CfgItem;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.function.Consumer;

public class Tree extends Observable {
    public class Node {
        private CfgItem item;
        private List<Node> leafs;
        private int level;
        private Node parentNode;

        public Node(CfgItem item, int level, Node parentNode) {
            this.item = item;
            leafs = new LinkedList<>();
            this.level = level;
            this.parentNode = parentNode;
        }

        public void addLeaf(CfgItem item) {
            item.setNode(new Node(item, level + 1, this));
            leafs.add((Node) item.getNode().get());
        }

        public Node getLeaf(String name) {
            for(Node node : leafs) {
                if(node.item.getName().equals(name)) {
                    return node;
                }
            }
            return null;
        }

        public CfgItem getItem() {
            return item;
        }

        public List<Node> getLeafs() {
            return leafs;
        }

        public StringBuilder getIndentation() {
            StringBuilder result = new StringBuilder();
            for(int i = 0; i < level; i++) result.append('\t');
            return result;
        }

        @Override
        public String toString() {
            return getIndentation().append(item.toString()).toString();
        }

        public String display() {
            return getIndentation().append(item.getName()).toString();
        }
    }


    private Node root;
    private long numberOfLeafs = 1L;

    public Tree(String cfg) {
        root = new Node(new CfgClass("", cfg), 0, null);
    }

    public Tree(CfgClass cfg) {
        root = new Node(cfg, 0, null);
    }

    public String getRootName() {
        return root.getItem().getName();
    }

    public Node getLeaf(String[] path) {
        Node current = root;//todo test if path[0].equals(root.item.getName());
        if(path == null) return null;//todo clean

        for(int i = 1; i < path.length; i++) {
            if(current == null) return null;
            current = current.getLeaf(path[i]);
        }
        return current;
    }

    public void addLeaf(CfgItem item) {
        if(item.getPath().length > 0) {
            getLeaf(item.getPath()).addLeaf(item);
            numberOfLeafs++;
        } else {
            System.err.println(item);
        }
    }

    public void browse(Consumer<Node> fnc) {
        browse(root, fnc);
    }

    private void browse(Node localRoot, Consumer<Node> fnc) {
        fnc.accept(localRoot);
        setChanged();
        notifyObservers();
        for(Node node : localRoot.leafs) {
            browse(node, fnc);
        }
    }

    public int getLeafLevel(String[] path) {
        return getLeaf(path).level;
    }

    public Node getParentNode(Node child) {
        return child.parentNode;
    }

    public long getNumberOfLeafs() {
        return numberOfLeafs;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        browse((node) -> result.append(node.toString()).append('\n'));
        return result.toString();
    }

    public String display() {
        StringBuilder result = new StringBuilder();
        browse((node) -> result.append(node.display()).append('\n'));
        return result.toString();
    }

    public String displayAllStruct() {
        StringBuilder result = new StringBuilder();
        browse((node) -> {
            if(node.item instanceof CfgClass) result.append(((CfgClass)node.item).displayStruct(this)).append('\n');
        });
        return result.toString();
    }

    public void print(PrintWriter out) {
        browse(out::println);
    }

    public void printAllStruct(PrintWriter out) {
        browse((node) -> {
            if(node.item instanceof CfgClass) out.println(((CfgClass)node.item).displayStruct(this));
        });
    }
}

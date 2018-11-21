package ch.armaconfigviewer.test;

import ch.armaconfigviewer.main.CfgClass;
import ch.armaconfigviewer.main.CfgProperty;
import ch.armaconfigviewer.main.Util.Tree;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTree {
    private static List<CfgClass> entries;

    @BeforeAll
    public static void setUpBeforeAll() {
        entries = new LinkedList<>();

        entries.add(new CfgClass("", "bin/cfg/a"));
        entries.add(new CfgClass("", "bin/cfg/b"));
        entries.add(new CfgClass("", "bin/cfg/a/c"));
        entries.add(new CfgClass("", "bin/cfg/d"));
        entries.add(new CfgClass("bin/cfg/a", "bin/cfg/e"));
        entries.add(new CfgClass("", "bin/cfg/e/f"));
        entries.add(new CfgClass("", "bin/cfg/e/g"));
        entries.add(new CfgClass("", "bin/cfg/h"));
        entries.add(new CfgClass("", "bin/cfgCar"));
        entries.add(new CfgClass("", "bin/cfgCar/i"));
        entries.add(new CfgClass("", "bin/cfgCar/j"));
        entries.add(new CfgClass("", "bin/cfgCar/j/k"));
        entries.add(new CfgClass("", "bin/cfg/e/z"));
    }


    @Test
    public void testTree() {
        Tree tree = new Tree("root/cfg");
        for(CfgClass entry : entries) tree.addLeaf(entry);
        tree.addLeaf(new CfgProperty<>("bin/cfg/a/prop", "0"));
        tree.addLeaf(new CfgProperty<>("bin/cfg/e/test", "2"));
        System.out.println(tree);

        assertEquals(2, tree.getLeafLevel(entries.get(2).getEntry()));

        System.out.println(entries.get(4).displayStruct(tree));
        System.out.println(tree.getRootName());
    }
}

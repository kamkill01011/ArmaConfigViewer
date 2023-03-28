package ch.armaconfigviewer.main;

public class CfgProperty<T> extends CfgItem<T> {

    public CfgProperty(String entry, T value) {
        super(entry, value);
    }

    public CfgProperty(String[] entry, T value) {
        super(entry, value);
    }

    @Override
    public String toString() {
        return getName() + "=" + getValue();
    }
}

package ch.armaconfigviewer.main.GUI;

import ch.armaconfigviewer.main.Parser;
import ch.armaconfigviewer.main.Util.Tree;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class StatusThread implements Runnable, Observer {
    private boolean running = false;
    private JLabel label;
    private long processedLeafs;
    private long totalLeafs;

    public StatusThread(JLabel label) {
        this.label = label;
    }

    @Override
    public void run() {
        running = true;
        processedLeafs = 0L;
        while(running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Parser) {
            label.setText(String.format("%,d lines processed", ((Parser)o).getNumberOfLineProcessed()));
        }
        else if (o instanceof Tree) {
            processedLeafs++;
            label.setText(String.format("%,d/%,d leafs processed", processedLeafs, totalLeafs));
        }

        label.revalidate();
        label.repaint();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setTotalLeafs(long totalLeafs) {
        this.totalLeafs = totalLeafs;
    }
}

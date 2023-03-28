package ch.armaconfigviewer.main.web;

import ch.armaconfigviewer.main.CfgClass;
import ch.armaconfigviewer.main.CfgItem;
import ch.armaconfigviewer.main.Util.Tree;

import java.io.*;
import java.nio.file.Files;

public class Web {

    public void makeClassHtml(CfgClass cfgClass, Tree tree) {
        makeClassHtml(cfgClass, tree, "");
    }

    public void makeClassHtml(CfgClass cfgClass, Tree tree, String folder) {
        makeHtml("template.html", folder + cfgClass.getEntryAsFile() + ".html", "$tab", getHtml(cfgClass, tree, "\t\t"), "$css", cfgClass.getPath().length);
    }

    public void copyCss(String destFolder) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream css = classloader.getResourceAsStream("style.css");
        File dest = new File(destFolder + "\\css\\style.css");
        if(dest.getParentFile() != null) dest.getParentFile().mkdirs();
        try {
            Files.copy(css, dest.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHtml(CfgClass cfgClass, Tree tree, String tabs) {
        StringBuilder html = new StringBuilder("<table>\n");
        //head
        html
                .append(tabs)
                .append("\t")
                .append("<tr><th>")
                .append(cfgClass.getName());
        if(cfgClass.getParent() != null) {
            html
                    .append(": ")
                    .append("<a href=\"")
                    .append(cfgClass.getParent())
                    .append(".html")
                    .append("\">")
                    .append(cfgClass.getParent())
                    .append("</a>")
                    .append("</th></tr>")
                    .append("\n");
        }
        //rows
        for(CfgItem item : cfgClass.getAllSortedProperties(tree)) {
            html.append(tabs).append("\t").append("<tr><td>");
            if(item instanceof CfgClass) {
                String itemParent = item.getPath().length == 0 ? "" : item.getPath()[item.getPath().length - 1];
                html
                        .append("<a href=\"")
                        .append(itemParent)
                        .append("\\")
                        .append(item.getName())
                        .append(".html")
                        .append("\">")
                        .append(item.getName())
                        .append("</a>");
                if(((CfgClass)item).getParent() != null) {
                    String parentParent = ((CfgClass)item).getParentPath().length == 0 ? "" : ((CfgClass)item).getParentPath()[((CfgClass)item).getParentPath().length - 1];
                    html
                            .append(": ")
                            .append("<a href=\"")
                            .append(parentParent)
                            .append("\\")
                            .append(((CfgClass) item).getParent())
                            .append(".html")
                            .append("\">")
                            .append(((CfgClass) item).getParent())
                            .append("</a>");
                }
            } else {
                html
                        .append(item.getName())
                        .append("=")
                        .append(item.getValue());
            }
            html
                    .append("</td></tr>")
                    .append("\n");
        }

        return html.append(tabs).append("</table>").toString();
    }

    public void makeHtml(String templateFile, String outputFile, String tabTag, String content, String cssTag, int level) {
        String s = readResourceFile(templateFile);
        s = s.replace(tabTag, content);
        s = s.replace(cssTag, getCss(level));
        writeFile(outputFile, s);
    }

    public String getCss(int n) {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < n; i++) s.append("../");
        return s.toString();
    }

    public String readResourceFile(String inputFile) {
        StringBuilder file = new StringBuilder();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(inputFile)))) {
            String s;
            while ((s = in.readLine()) != null) {
                file.append(s).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.toString();
    }

    /*public String readFile(String inputFile) {
        StringBuilder file = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))) {
            String s;
            while ((s = in.readLine()) != null) {
                file.append(s).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.toString();
    }*/

    public void writeFile(String outputFile, String input) {
        try {
            File file = new File(outputFile);
            if(file.getParentFile() != null) file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write(input);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

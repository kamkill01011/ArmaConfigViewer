package kam;

import java.util.LinkedList;
import java.util.List;

public class CfgClass extends CfgItem {
    private CfgClass parent;
    private String parentPath;
    private List<CfgItem> properties = new LinkedList<>();

    public CfgClass(String path) {
        super(path);
    }

}

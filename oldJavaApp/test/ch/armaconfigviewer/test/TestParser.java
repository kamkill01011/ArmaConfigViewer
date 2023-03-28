package ch.armaconfigviewer.test;

import ch.armaconfigviewer.main.CfgClass;
import ch.armaconfigviewer.main.CfgProperty;
import ch.armaconfigviewer.main.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestParser {
    private static Parser parser;

    @BeforeAll
    public static void setUpBeforeAll() {
        parser = new Parser();
    }

    @Test
    public void testCleanLine() {
        assertEquals("bin\\config.bin/CfgVehicles/LaserTarget,bin\\config.bin/CfgVehicles/All", parser.cleanLine("19:43:35 [\"KAM_ACV_Parent\",[bin\\config.bin/CfgVehicles/LaserTarget,bin\\config.bin/CfgVehicles/All]]", "[\"KAM_ACV_Parent\","));
        assertEquals("bin\\config.bin/CfgVehicles/LaserTarget/reversed,0", parser.cleanLine("19:43:35 [\"KAM_ACV_Property\",[bin\\config.bin/CfgVehicles/LaserTarget/reversed,0]]", "[\"KAM_ACV_Property\","));
    }

    @Test
    public void testParseEntry() {
        CfgClass entry = parser.cleanEntry("bin\\config.bin/CfgVehicles/LaserTarget,bin\\config.bin/CfgVehicles/All");
        assertEquals("All", entry.getParent());
        assertEquals("LaserTarget", entry.getName());
    }

    @Test
    public void testParseProperty() {
        CfgProperty property = parser.cleanProperty("bin\\config.bin/CfgVehicles/LaserTarget/reversed,0");
        assertEquals("LaserTarget", property.getPath()[property.getPath().length - 1]);
        assertEquals("reversed", property.getName());
        assertEquals("0", property.getValue());
    }

    @Test
    public void testParseLongEntry() {
        CfgClass entry = parser.cleanEntry("bin\\config.bin/CfgVehicles/CAManBase/ACE_Actions/ACE_Dogtag,");
        assertNull(entry.getValue());
        assertEquals("ACE_Dogtag", entry.getName());
    }
}

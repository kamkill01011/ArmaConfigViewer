package kam;

import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void testParseLine() {
        Parser.parseLine("bin\\config.bin/CfgMagazines/Default/scope=\"0\"");
        Parser.parseLine("bin\\config.bin/CfgMagazines/FakeMagazine=\"\"bin\\config.bin/CfgMagazines/Default\"\"");
    }
}

package restcommon.databind;

import restcommon.runner.RestTestGroupJsonPath;
import org.junit.Test;

import java.net.URL;

/**
 * Created by maggie on 4/19/16.
 */
public class RestTestGroupParsingTest {
    @Test
    public void parseJson() throws Exception {
        RestTestGroup group = RestTestGroup.build(getClass().getResource("testGroup.json"));
        int i = 0;
    }
}

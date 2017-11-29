package restcommon.databind;

import junitparams.JUnitParamsRunner;
import org.junit.runner.RunWith;
import restcommon.runner.DefaultRestTestClass;
import restcommon.runner.RestTestGroupJsonPath;

/**
 * Created by maggie on 4/19/16.
 */
@RunWith(JUnitParamsRunner.class)
@RestTestGroupJsonPath("sample-rest.json")
public class SampleJsonRestTest extends DefaultRestTestClass {}

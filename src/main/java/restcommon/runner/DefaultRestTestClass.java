package restcommon.runner;

import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import restcommon.databind.RestTestGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maggie on 4/19/16.
 */
public abstract class DefaultRestTestClass {
    private static Map<String, TestContext> testCtxMap = new HashMap<>();
    private TestContext ctx;

    @Before
    public void testSetup() {
        ctx = testCtxMap.get(getClass().getSimpleName());
        if (!ctx.start) {
            ctx.testGroup.configureBeforeGroup();
            ctx.start = true;
        }
        ctx.testGroup.configureBeforeTest();
    }

    @Test
    @Parameters
    @TestCaseName("{0}")
    public void test(String testName) {
        ctx.testGroup.getTest(testName).configure(ctx.testGroup.getConfigureForTest()).asserting();
    }

    protected Object[] parametersForTest() {
        String testClassName = getClass().getSimpleName();
        RestTestGroupJsonPath annotation = getClass().getAnnotation(RestTestGroupJsonPath.class);
        String jsonPath = annotation == null ? testClassName + ".json" : annotation.value();
        TestContext ctx = new TestContext(RestTestGroup.build(getClass().getResource(jsonPath)));
        testCtxMap.put(testClassName, ctx);
        return ctx.testGroup.getAllTestNames().toArray(new Object[]{});
    }

    @After
    public void testCleanup() {
        ctx.testGroup.configureAfterTest();
        if (ctx.checkLastTest()) ctx.testGroup.configureAfterGroup();
    }

    private class TestContext {
        RestTestGroup testGroup;
        int remainTests;
        boolean start;

        TestContext(RestTestGroup testGroup) {
            this.testGroup = testGroup;
            this.remainTests = testGroup.getTests().size();
            this.start = false;
        }

        boolean checkLastTest() {
            return --remainTests == 0;
        }
    }
}

package restcommon.databind;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jayway.restassured.response.Response;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by maggie on 4/19/16.
 */
public class RestTestGroup {
    private String baseUrl;
    @JsonDeserialize(keyAs = String.class, contentAs = String.class)
    private Map<String, String> groupConfig;
    @JsonIgnore
    private Map<String, String> testConfig;
    @JsonDeserialize(contentAs = RestTestConfig.class)
    private List<RestTestConfig> beforeGroupConfig;
    @JsonDeserialize(contentAs = RestTestConfig.class)
    private List<RestTestConfig> beforeTestConfig;
    @JsonDeserialize(contentAs = RestTestConfig.class)
    private List<RestTestConfig> afterTestConfig;
    @JsonDeserialize(contentAs = RestTestConfig.class)
    private List<RestTestConfig> afterGroupConfig;
    @JsonDeserialize(contentAs = RestTest.class)
    private List<RestTest> tests;
    @JsonIgnore
    private Map<String, RestTest> testMap;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Map<String, String> getGroupConfig() {
        return groupConfig;
    }

    public void setGroupConfig(Map<String, String> groupConfig) {
        this.groupConfig = groupConfig;
    }

    public List<RestTestConfig> getBeforeGroupConfig() {
        return beforeGroupConfig;
    }

    public void setBeforeGroupConfig(
            List<RestTestConfig> beforeGroupConfig) {
        this.beforeGroupConfig = beforeGroupConfig;
    }

    public List<RestTestConfig> getBeforeTestConfig() {
        return beforeTestConfig;
    }

    public void setBeforeTestConfig(
            List<RestTestConfig> beforeTestConfig) {
        this.beforeTestConfig = beforeTestConfig;
    }

    public List<RestTestConfig> getAfterTestConfig() {
        return afterTestConfig;
    }

    public void setAfterTestConfig(
            List<RestTestConfig> afterTestConfig) {
        this.afterTestConfig = afterTestConfig;
    }

    public List<RestTestConfig> getAfterGroupConfig() {
        return afterGroupConfig;
    }

    public void setAfterGroupConfig(
            List<RestTestConfig> afterGroupConfig) {
        this.afterGroupConfig = afterGroupConfig;
    }

    public List<RestTest> getTests() {
        return tests;
    }

    public void setTests(List<RestTest> tests) {
        this.tests = tests;
        initTestMap();
    }

    private void initTestMap() {
        if (testMap == null) testMap = new LinkedHashMap<>();
        else testMap.clear();
        for (RestTest test : tests) testMap.put(test.getName(), test);
    }

    /* concrete logic */

    public static RestTestGroup build(URL jsonUrl) {
        try {
            RestTestGroup testGroup = new ObjectMapper().readValue(jsonUrl, RestTestGroup.class);
            setBaseUrl(testGroup.beforeGroupConfig, testGroup.baseUrl);
            setBaseUrl(testGroup.beforeTestConfig, testGroup.baseUrl);
            setBaseUrl(testGroup.afterTestConfig, testGroup.baseUrl);
            setBaseUrl(testGroup.afterGroupConfig, testGroup.baseUrl);
            if (testGroup.getTests() != null) {
                for (RestTest test : testGroup.getTests()) test.getCommand().setBaseUrl(testGroup.baseUrl);
            }
            return testGroup;
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static void setBaseUrl(List<RestTestConfig> configs, String baseUrl) {
        if (configs == null) return;
        for (RestTestConfig config : configs) config.getCommand().setBaseUrl(baseUrl);
    }

    public void configureBeforeGroup() {
        if (groupConfig == null) groupConfig = new HashMap<>();
        configure(groupConfig, beforeGroupConfig);
    }

    public void configureBeforeTest() {
        testConfig = new HashMap<>(groupConfig);
        configure(testConfig, beforeTestConfig);
    }

    public void configureAfterTest() {
        if (testConfig == null) testConfig = new HashMap<>();
        configure(testConfig, afterTestConfig);
    }

    public void configureAfterGroup() {
        if (groupConfig == null) groupConfig = new HashMap<>();
        configure(groupConfig, afterGroupConfig);
    }

    private void configure(Map<String, String> target, List<RestTestConfig> input) {
        if (target == null || input == null) return;
        for (RestTestConfig config : input) {
            Response resp = config.getCommand().configure(target).execute();
//            assertThat(config.getCommand().getUrl() + " failed", resp.statusCode(),
//                    both(greaterThanOrEqualTo(200)).and(lessThan(300)));
            if (config.getName() != null && config.getRefPath() != null) {
                target.put(config.getName(), from(resp.body().asString()).getString(config.getRefPath()));
            }
        }
    }

    public Map<String, String> getConfigureForTest() {
        Map<String, String> res = new HashMap<>();
        if (groupConfig != null) res.putAll(groupConfig);
        if (testConfig != null) res.putAll(testConfig);
        return res;
    }

    public RestTest getTest(String testName) {
        return testMap == null ? null : testMap.get(testName);
    }

    public List<String> getAllTestNames() {
        return testMap == null ? null : new ArrayList<>(testMap.keySet());
    }

}

package restcommon.databind;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by maggie on 4/19/16.
 */
public class RestTest {
    private String name;
    private RestCommand command;
    private RestExpectResult expect;
    @JsonIgnore
    private Map<String, String> config;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RestCommand getCommand() {
        return command;
    }

    public void setCommand(RestCommand command) {
        this.command = command;
    }

    public RestExpectResult getExpect() {
        return expect;
    }

    public void setExpect(RestExpectResult expect) {
        this.expect = expect;
    }

    public RestTest configure(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public void asserting() {
        Response resp = command.configure(config).execute();
        if (expect.getStatusCode() != null) {
            assertThat("statusCode assert failure", resp.getStatusCode(), is(expect.getStatusCode()));
        }
        if (resp.contentType().startsWith(ContentType.TEXT.toString()) && expect.getRawBodyValue() != null) {
            assertThat("raw body assert failure", resp.getBody().asString(), is(expect.getRawBodyValue()));
        } else if (resp.contentType().startsWith(ContentType.JSON.toString()) && expect.getBodyValues() != null) {
            JsonPath jp = JsonPath.from(resp.getBody().asString());
            for (Map.Entry<String, Object> bodyExpect : expect.getBodyValues().entrySet()) {
                Object actualVal = jp.get(bodyExpect.getKey());
                Object expectVal = bodyExpect.getValue();

                //float assure handling
                if (actualVal instanceof Float && (expectVal instanceof Float || expectVal instanceof Double)) {
                    expectVal = Float.valueOf(expectVal + "");
                }


                if (expectVal.equals("*") && actualVal != null) expectVal = actualVal;
                assertThat(String.format("body path <%s> assert failure", bodyExpect.getKey()),
                        //jp.get(bodyExpect.getKey()), is(bodyExpect.getValue()));
                        actualVal, is(expectVal));
            }
        }
    }
}

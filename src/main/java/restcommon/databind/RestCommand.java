package restcommon.databind;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by maggie on 4/19/16.
 */
public class RestCommand {
    private String url;
    private Method method;
    private String contentType;
    @JsonDeserialize(keyAs = String.class, contentAs = String.class)
    private Map<String, String> headers;
    @JsonDeserialize(using = RestCommandBodyDeserializer.class)
    private String body;
    @JsonIgnore
    private String baseUrl;
    @JsonIgnore
    private Map<String, String> config;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public RestCommand configure(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public enum Method {
        GET, POST, PUT, DELETE, HEAD
    }

    /* concrete logic */

    public Response execute() {
        RequestSpecification spec = given();
        if (contentType != null) spec.contentType(contentType);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                spec.header(header.getKey(), handleRef(header.getValue()));
            }
        }
        if (body != null) spec.body(handleRef(body));
        String actualUrl = String.format("%s/%s", baseUrl, url.startsWith("/") ? url.substring(1) : url);
        switch (method) {
            case POST:
                return spec.post(actualUrl);
            case PUT:
                return spec.put(actualUrl);
            case DELETE:
                return spec.delete(actualUrl);
            case HEAD:
                return spec.head(actualUrl);
            case GET:
            default:
                return spec.get(actualUrl);
        }
    }

    private String handleRef(String input) {
        if (input == null || config == null || config.isEmpty()) return input;
        String pattern = "\\$\\{(\\w+)\\}";
        Matcher m = Pattern.compile(pattern).matcher(input);
        while (m.find()) {
            String refVal = config.get(m.group(1));
            if (refVal != null) input = input.replaceFirst(pattern, refVal);
        }
        return input;
    }
}

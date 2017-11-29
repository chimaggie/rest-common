package restcommon.databind;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

/**
 * Created by maggie on 4/19/16.
 */
public class RestExpectResult {
    private Integer statusCode;
    @JsonDeserialize(keyAs = String.class, contentAs = Object.class)
    private Map<String, Object> bodyValues;
    private String rawBodyValue;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, Object> getBodyValues() {
        return bodyValues;
    }

    public void setBodyValues(Map<String, Object> bodyValues) {
        this.bodyValues = bodyValues;
    }

    public String getRawBodyValue() {
        return rawBodyValue;
    }

    public void setRawBodyValue(String rawBodyValue) {
        this.rawBodyValue = rawBodyValue;
    }
}

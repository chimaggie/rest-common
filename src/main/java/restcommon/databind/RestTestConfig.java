package restcommon.databind;

/**
 * Created by maggie on 4/19/16.
 */
public class RestTestConfig {
    private String name;
    private RestCommand command;
    private String refPath;

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

    public String getRefPath() {
        return refPath;
    }

    public void setRefPath(String refPath) {
        this.refPath = refPath;
    }
}

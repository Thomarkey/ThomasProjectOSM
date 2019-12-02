package country.customer.project.selenium.driver.setup.enums;

import java.util.*;

public enum EnvironmentType {
    BROWSERSTACK("browserstack"),
    DOCKER("docker", "docker-grid", "grid"),
    LOCAL("local"),
    SAUCELABS("saucelabs");

    private final List<String> environments;

    EnvironmentType(String... environments) {
        this.environments = Arrays.asList(environments);
    }

    public String toString() {
        return this.environments.get(0);
    }

    static final private Map<String, EnvironmentType> ALIAS_MAP = new HashMap<>();

    static {
        for (EnvironmentType environmentType : EnvironmentType.values()) {
            // ignoring the case by normalizing to uppercase
            ALIAS_MAP.put(environmentType.name().toUpperCase(), environmentType);
            for (String alias : environmentType.environments)
                ALIAS_MAP.put(alias.toUpperCase(), environmentType);
        }
    }

    static public EnvironmentType fromString(String value) {
        if (value == null) throw new NullPointerException("alias null");
        EnvironmentType environmentType = ALIAS_MAP.get(value.toUpperCase());
        if (environmentType == null)
            throw new IllegalArgumentException("Not an alias: " + value);
        return environmentType;
    }
}

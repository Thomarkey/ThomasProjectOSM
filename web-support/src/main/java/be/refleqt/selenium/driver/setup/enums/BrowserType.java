package be.refleqt.selenium.driver.setup.enums;

import java.util.*;

public enum BrowserType {
    CHROME("chrome"),
    EDGE("edge", "MicrosoftEdge"),
    FIREFOX("firefox", "ff"),
    IEXPLORER("ie", "internet explorer", "iexplorer"),
    SAFARI("safari");

    private final List<String> browsers;

    BrowserType(String... browsers) {
        this.browsers = Arrays.asList(browsers);
    }

    public String toString() {
        return this.browsers.get(0);
    }

    static final private Map<String, BrowserType> ALIAS_MAP = new HashMap<>();

    static {
        for (BrowserType browserType : BrowserType.values()) {
            // ignoring the case by normalizing to uppercase
            ALIAS_MAP.put(browserType.name().toUpperCase(), browserType);
            for (String alias : browserType.browsers)
                ALIAS_MAP.put(alias.toUpperCase(), browserType);
        }
    }


    static public BrowserType fromString(String value) {
        if (value == null) throw new NullPointerException("alias null");
        BrowserType browserType = ALIAS_MAP.get(value.toUpperCase());
        if (browserType == null)
            throw new IllegalArgumentException("Not an alias: " + value);
        return browserType;
    }
}

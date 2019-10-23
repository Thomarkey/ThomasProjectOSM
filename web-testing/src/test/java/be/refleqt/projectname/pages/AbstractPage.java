package be.refleqt.projectname.pages;

import be.refleqt.projectname.support.*;

/**
 * In this class you can override the already defined functions locally
 */
public abstract class AbstractPage extends be.refleqt.library.selenium.AbstractPage {

    public static ThreadLocal<World> world = new ThreadLocal<>();

    public static void setWorld(World world) {
        AbstractPage.world.set(world);
    }

    public AbstractPage() {
        super();
        setTimeOut(Long.parseLong(PropertiesReader.getKey("timeout.explicitWait")));
        setShortTimeOut(Long.parseLong(PropertiesReader.getKey("timeout.implicitWait.short")));
        setLongTimeOut(Long.parseLong(PropertiesReader.getKey("timeout.longWait")));
    }
}

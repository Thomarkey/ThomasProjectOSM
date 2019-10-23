package be.refleqt.projectname.support;

import be.refleqt.projectname.pages.*;

public class World {
    public World() {
        AbstractPage.setWorld(this);
    }

    public String search;
    public String result;
}

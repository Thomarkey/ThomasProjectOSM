package country.customer.project.support;

import country.customer.project.pages.AbstractPage;

public class World {
    public World() {
        AbstractPage.setWorld(this);
    }

    public String search;
    public String result;
}

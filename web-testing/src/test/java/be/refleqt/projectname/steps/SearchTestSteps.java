package be.refleqt.projectname.steps;

import be.refleqt.projectname.support.World;
import io.cucumber.java.en.And;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SearchTestSteps {

    private World world;

    public SearchTestSteps(World world) {
        this.world = world;
    }

    @And("I searched for {}")
    public void iSearchedForSearch(String term) {
        assertThat(world.search).isEqualToIgnoringCase(term);
    }

    @And("I found {}")
    public void iFoundResult(String term) {
        assertThat(world.result).isEqualToIgnoringCase(term);
    }
}

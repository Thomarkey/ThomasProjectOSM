package be.refleqt.logger;

import cucumber.api.Scenario;

public class ScenarioManager {

    private static ThreadLocal<ScenarioManager> INSTANCE = new ThreadLocal<>();
    private ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    private ScenarioManager() {
    }

    public static ScenarioManager getInstance() {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new ScenarioManager());
        }
        return INSTANCE.get();
    }

    public void setScenario(Scenario scenario) {
        this.scenario.set(scenario);
    }

    public Scenario getScenario() {
        return this.scenario.get();
    }
}

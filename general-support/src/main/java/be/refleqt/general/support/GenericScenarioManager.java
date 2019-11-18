package be.refleqt.general.support;

import io.cucumber.core.api.*;

public class GenericScenarioManager {

    protected static ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    public static void setScenario(Scenario currentScenario) {
        scenario.set(currentScenario);
        System.out.println("Running: " + currentScenario.getName());
    }

    public static void writeLine(String info) {
        try {
            scenario.get().write(info);
        } catch (Exception e) {
        }
    }

    public static Scenario getScenario() {
        return scenario.get();
    }
}

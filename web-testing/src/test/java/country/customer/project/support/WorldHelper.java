package country.customer.project.support;

public class WorldHelper {
    private static ThreadLocal<World> world = new ThreadLocal<>();

    public static synchronized void setWorld(World world) {
        WorldHelper.world.set(world);
    }

    public synchronized static World getWorld() {
        return world.get();
    }
}

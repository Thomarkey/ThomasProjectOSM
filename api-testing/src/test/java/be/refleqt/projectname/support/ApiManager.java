package be.refleqt.projectname.support;

import be.refleqt.base.test.dto.ApiClient;
import be.refleqt.base.test.dto.api.PetApi;
import be.refleqt.logger.CustomLogFilter;
import be.refleqt.projectname.utils.World;
import io.restassured.RestAssured;

import java.util.concurrent.TimeUnit;

public class ApiManager {

    private static ThreadLocal<World> world = new ThreadLocal<>();
    private static String basePath = System.getProperty("basePath", "https://petstore.swagger.io/v2");
    private static int timeOut = 60;

    public static void setupRestAssured() {
        RestAssured.baseURI = basePath;
        RestAssured.filters(new CustomLogFilter());
    }

    public static void setWorld(World world) {
        ApiManager.world.set(world);
    }

    public static PetApi getPetApi() {
        PetApi petApi = new PetApi();
        ApiClient apiClient = petApi.getApiClient();

        apiClient.setBasePath(basePath);
        apiClient.getHttpClient().setConnectTimeout(timeOut, TimeUnit.SECONDS);

//        TODO: Add default headers if needed
//        apiClient.addDefaultHeader("Authorization", "Bearer" + world.get().token);
        petApi.setApiClient(apiClient);
        return petApi;
    }
}

package be.refleqt.projectname.support;

import be.refleqt.base.test.dto.*;
import be.refleqt.base.test.dto.api.*;
import be.refleqt.logger.*;
import be.refleqt.projectname.utils.*;
import io.restassured.*;

import java.util.concurrent.*;

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
        petApi.setApiClient(getDefaultApiClient(petApi.getApiClient()));
        return petApi;
    }

    //Sets the base path and other default generic parameters.
    private static ApiClient getDefaultApiClient(ApiClient apiClient) {
        apiClient.setBasePath(basePath);
        apiClient.getHttpClient().setConnectTimeout(timeOut, TimeUnit.SECONDS);
        return apiClient;
    }

    //Extension of the default api client with required headers like a access token
    private static ApiClient getAuthorizedApiClient(ApiClient apiClient) {
        return getDefaultApiClient(apiClient)
                .addDefaultHeader("Authorization", "Bearer " + world.get().accessToken)
                .addDefaultHeader("OtherRequiredHeader", "value");
    }
}

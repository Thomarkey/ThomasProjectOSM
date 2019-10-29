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
        return new PetApi(getDefaultApiClient());
    }

    public static PetApi getPetApiAuthenticated() {
        return new PetApi(getAuthorizedApiClient());
    }

    //Sets the base path and other default generic parameters.
    private static ApiClient getDefaultApiClient() {
        ApiClient apiClient = new ApiClient();

        apiClient.setBasePath(basePath);
        apiClient.getHttpClient().setConnectTimeout(timeOut, TimeUnit.SECONDS);
        return apiClient;
    }

    //Extension of the default api client with required headers like a access token
    private static ApiClient getAuthorizedApiClient() {
        return getDefaultApiClient()
                .addDefaultHeader("Authorization", "Bearer " + world.get().accessToken)
                .addDefaultHeader("OtherRequiredHeader", "value");
    }
}

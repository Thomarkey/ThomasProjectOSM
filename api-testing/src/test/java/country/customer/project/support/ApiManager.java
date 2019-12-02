package country.customer.project.support;

import country.customer.project.logger.CustomLogFilter;
import country.customer.project.test.dto.ApiClient;
import country.customer.project.test.dto.api.PetApi;
import country.customer.project.utils.World;
import io.restassured.RestAssured;

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

    public static PetApi getPetsApi() {
        return new PetApi(getDefaultApiClient());
    }

    //Sets the base path and other default generic parameters.
    private static ApiClient getDefaultApiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(basePath);
        return apiClient;
    }

    //Extension of the default api client with required headers like a access token
    private static ApiClient getAuthorizedApiClient() {
        return getDefaultApiClient()
                .addDefaultHeader("Authorization", "Bearer " + world.get().accessToken)
                .addDefaultHeader("OtherRequiredHeader", "value");
    }
}

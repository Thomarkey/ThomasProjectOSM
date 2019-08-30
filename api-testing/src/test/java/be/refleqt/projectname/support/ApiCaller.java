package be.refleqt.projectname.support;

import be.refleqt.base.test.dto.ApiException;
import be.refleqt.base.test.dto.ApiResponse;
import be.refleqt.projectname.utils.World;

import java.util.concurrent.Callable;

public final class ApiCaller {

    public static ThreadLocal<World> world = new ThreadLocal<>();

    private ApiCaller() {
    }

    public static <T> ApiResponseOrException<T> call(Callable<ApiResponse<T>> apiCall) {
        return new ApiCallable<>(apiCall).callApi();
    }

    public static void setWorld(World world) {
        ApiCaller.world.set(world);
    }

    private static class ApiCallable<T> {
        private final Callable<ApiResponse<T>> apiCall;

        private ApiCallable(Callable<ApiResponse<T>> apiCall) {
            this.apiCall = apiCall;
        }

        private ApiResponseOrException<T> callApi() {
            ApiResponse<T> apiResponse = null;
            ApiException apiException = null;

            try {
                apiResponse = apiCall.call();
            } catch (ApiException e) {
                apiException = e;
                world.get().lastApiException = apiException;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return new ApiResponseOrException<>(apiResponse, apiException);
        }
    }
}


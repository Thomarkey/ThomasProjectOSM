package be.refleqt.projectname.support;

import be.refleqt.base.test.dto.ApiException;
import be.refleqt.base.test.dto.ApiResponse;

public class ApiResponseOrException<T> {
    private final ApiResponse<T> apiResponse;
    private final ApiException apiException;

    public ApiResponseOrException(ApiResponse<T> apiResponse, ApiException apiException) {
        this.apiResponse = apiResponse;
        this.apiException = apiException;
    }


    public boolean responseReturned() {
        return apiResponse != null;
    }

    public int getStatus() {
        if (responseReturned()) {
            return apiResponse.getStatusCode();
        } else {
            return apiException.getCode();
        }
    }

    public ApiResponse<T> getApiResponse() {
        return apiResponse;
    }

    public ApiException getApiException() {
        return apiException;
    }
}

package country.customer.project.utils;

import country.customer.project.support.*;
import country.customer.project.test.dto.ApiException;
import country.customer.project.test.dto.model.Pet;

public class World {
    public World() {
        ApiCaller.setWorld(this);
        ApiManager.setWorld(this);
    }

    public long petId;
    public ApiResponseOrException<Pet> petResponse;
    public ApiException lastApiException;
    public String accessToken;
}

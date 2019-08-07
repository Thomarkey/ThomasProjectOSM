package be.refleqt.projectname.utils;

import be.refleqt.base.test.dto.ApiException;
import be.refleqt.base.test.dto.model.Pet;
import be.refleqt.projectname.support.ApiCaller;
import be.refleqt.projectname.support.ApiManager;
import be.refleqt.projectname.support.ApiResponseOrException;

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

package be.refleqt.projectname.utils;

import be.refleqt.base.test.dto.*;
import be.refleqt.base.test.dto.model.*;
import be.refleqt.projectname.support.*;

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

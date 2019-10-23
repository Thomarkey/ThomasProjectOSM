package be.refleqt.projectname.transformers;

import be.refleqt.base.test.dto.model.*;
import be.refleqt.projectname.utils.*;
import io.cucumber.datatable.*;

import java.util.*;

public class PetTransformer implements TableEntryTransformer<Pet> {
    @Override
    public Pet transform(Map<String, String> data) {
        return new Pet()
                .name(DataHelper.getGeneric(data.getOrDefault("name", "<randomString>")))
                .id(Long.valueOf(DataHelper.getGeneric(data.getOrDefault("id", "300"))));
    }
}

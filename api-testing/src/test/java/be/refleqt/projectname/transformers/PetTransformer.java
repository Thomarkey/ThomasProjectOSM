package be.refleqt.projectname.transformers;

import be.refleqt.base.test.dto.model.Pet;
import be.refleqt.projectname.utils.DataHelper;
import io.cucumber.datatable.TableEntryTransformer;
import java.util.Map;

public class PetTransformer implements TableEntryTransformer<Pet> {
    @Override
    public Pet transform(Map<String, String> data) {
        return new Pet()
                .name(DataHelper.getGeneric(data.getOrDefault("name", "<randomString>")))
                .id(Long.valueOf(DataHelper.getGeneric(data.getOrDefault("id", "300"))));
    }
}

package country.customer.project.transformers;

import country.customer.project.test.dto.model.Pet;
import country.customer.project.utils.DataHelper;
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

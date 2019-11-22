package be.refleqt.projectname.steps;

import be.refleqt.base.test.dto.model.Pet;
import be.refleqt.projectname.transformers.Color;
import be.refleqt.projectname.transformers.PetTransformer;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.datatable.DataTableType;
import java.util.Locale;
import static java.util.Locale.ENGLISH;

/**
 * This file is located in the steps package because cucumber needs to find it in the glue.
 */
public class TypeRegistryConfiguration implements TypeRegistryConfigurer {
    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(Pet.class, new PetTransformer()));
        typeRegistry.defineParameterType(new ParameterType<>("color", "red|yellow|green", Color.class, Color::new));
    }
}

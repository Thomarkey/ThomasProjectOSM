package be.refleqt.projectname.steps;

import be.refleqt.base.test.dto.model.Pet;
import be.refleqt.projectname.converters.PetConverter;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
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
        typeRegistry.defineDataTableType(new DataTableType(Pet.class, new PetConverter()));
    }
}

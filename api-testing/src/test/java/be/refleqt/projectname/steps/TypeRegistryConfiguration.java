package be.refleqt.projectname.steps;

import be.refleqt.base.test.dto.model.*;
import be.refleqt.projectname.transformers.*;
import io.cucumber.core.api.*;
import io.cucumber.cucumberexpressions.*;
import io.cucumber.datatable.*;

import java.util.*;

import static java.util.Locale.*;

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

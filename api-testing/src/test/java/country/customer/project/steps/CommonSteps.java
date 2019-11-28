package country.customer.project.steps;

import com.fasterxml.jackson.databind.JsonNode;
import country.customer.project.general.support.GenericScenarioManager;
import country.customer.project.logger.JsonNodeHelper;
import country.customer.project.support.*;
import country.customer.project.test.dto.model.Pet;
import country.customer.project.transformers.Color;
import country.customer.project.utils.World;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.Transpose;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import static io.restassured.RestAssured.given;
import io.restassured.http.Header;
import io.restassured.response.Response;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    private World world;

    /**
     * @param world Don't forgot to generate a constructor with a World in each step class you create!
     */
    public CommonSteps(World world) {
        this.world = world;
    }

    @Before
    public void before(Scenario scenario) {
        GenericScenarioManager.setScenario(scenario);
        ApiManager.setupRestAssured();
    }

    @Then("The system returns error message with:")
    public void theSystemReturnsErrorMessageWith(Map<String, String> data) {
        assertThat(world.lastApiException.getCode()).isEqualTo(Integer.parseInt(data.get("status")));

        JsonNode responseBody = JsonNodeHelper.readJsonFromString(world.lastApiException.getResponseBody());
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.get("code").asText()).isEqualToIgnoringCase(data.get("code"));
        assertThat(responseBody.get("message").asText()).isEqualToIgnoringCase(data.get("message"));
    }

    @Then("The system returns field errors with:")
    public void theSystemReturnsFieldErrorsWith(List<Map<String, String>> data) {
        JsonNode responseBody = JsonNodeHelper.readJsonFromString(world.lastApiException.getResponseBody());
        assertThat(responseBody).isNotNull();
        JsonNode fieldErrors = responseBody.get("fieldErrors");
        assertThat(fieldErrors).isNotNull();

        List<Map<String, String>> toCompare = new ArrayList<>();

        //Mapping all thrown errors.
        for (JsonNode fieldError : fieldErrors) {
            Map<String, String> error = new HashMap<>();
            error.put("field", fieldError.get("field").asText());
            error.put("code", fieldError.get("code").asText());
            error.put("message", fieldError.get("message").asText());
            toCompare.add(error);
        }

        //Validating the fieldError triggered is displayed.
        assertThat(toCompare).isNotNull();
        data.forEach(
                d -> assertThat(toCompare).contains(d)
        );
    }

    /**
     * Everything under here are examples of how to use the base.
     * We expect you to delete these functions.
     */
    @Given("I create a pet with:")
    public void iCreateAPet(@Transpose Pet pet) {
        world.petId = pet.getId();

        ApiResponseOrException<Void> response = ApiCaller.call(
                () -> ApiManager.getPetsApi().addPetWithHttpInfo(pet)
        );

        assertThat(response.getStatus())
                .isEqualTo(200);
    }

    @Then("The created pet is named {word}")
    public void theCreatedPetIsNamedName(String name) {
        world.petResponse = ApiCaller.call(
                () -> ApiManager.getPetsApi().getPetByIdWithHttpInfo(world.petId)
        );

        assertThat(world.petResponse.getApiResponse().getData().getName())
                .isEqualToIgnoringCase(name);
    }

    @Given("I perform a multiData form request")
    public void iPerformAMultiDataFormRequest() {
        //rest-assured example
        Response response = given()
                .contentType("multipart/form-data")
                .header(new Header("Authorization", "Basic basic"))
                .header(new Header("Accept-Language", "nl"))
//                        .multiPart("paramName", new File("PATH")) //Example of file upload
                .multiPart("username", "user") //Example of oAuth call
                .multiPart("password", "pass")
                .multiPart("grant_type", "password")
                .request("POST", "/oauth/token");

        JsonNode responseBody = JsonNodeHelper.readJsonFromString(response.getBody().print());

        assertThat(responseBody).isNotNull();
        world.accessToken = responseBody.get("access_token").asText();
    }

    @Given("New color {color} is printed")
    public void newColor(Color color) {
        System.out.println(color.getColor());
    }
}

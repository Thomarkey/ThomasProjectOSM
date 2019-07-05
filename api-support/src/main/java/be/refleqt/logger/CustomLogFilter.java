package be.refleqt.logger;

import io.cucumber.datatable.dependency.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CustomLogFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        StringBuilder requestBuilder = new StringBuilder();

        requestBuilder.append("<<< REQUEST >>>");
        requestBuilder.append("\n");
        requestBuilder.append("STATUS:");
        requestBuilder.append("\n\t");
        requestBuilder.append(requestSpec.getMethod());
        requestBuilder.append(": ");
        requestBuilder.append(requestSpec.getURI());
        requestBuilder.append("\n");
        requestBuilder.append("\n");

        requestBuilder.append("HEADERS:");
        requestBuilder.append("\n");
        requestSpec.getHeaders().forEach(h -> {
            requestBuilder.append("\t");
            requestBuilder.append(h.getName());
            requestBuilder.append(": ");
            requestBuilder.append(h.getValue());
            requestBuilder.append("\n");
        });

        if (requestSpec.getBody() != null) {
            try {
                requestBuilder.append(
                        new ObjectMapper()
                                .writerWithDefaultPrettyPrinter()
                                .writeValueAsString(JsonNodeHelper.readJsonFromString(requestSpec.getBody()))
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (requestSpec.getMultiPartParams().size() > 0) {
            requestBuilder.append("\n");
            requestBuilder.append("MULTIFORM PART:");
            requestBuilder.append("\n");

            requestSpec.getMultiPartParams().forEach(m -> {
                        requestBuilder.append("\t");
                        requestBuilder.append(m.getControlName());
                        requestBuilder.append(": ");
                        requestBuilder.append(m.getContent().toString());
                        requestBuilder.append("\n");
                    }
            );
        }
        requestBuilder.append("\n");

        ScenarioManager.getInstance().getScenario().write(requestBuilder.toString());

        StringBuilder responseBuilder = new StringBuilder();

        responseBuilder.append("<<< RESPONSE >>>");
        responseBuilder.append("\n");
        responseBuilder.append("STATUS:");
        responseBuilder.append("\n\t");
        responseBuilder.append(response.getStatusLine());
        responseBuilder.append("\n");
        responseBuilder.append("HEADERS:");
        responseBuilder.append("\n");
        response.getHeaders().forEach(h -> {
            responseBuilder.append("\t");
            responseBuilder.append(h.getName());
            responseBuilder.append(": ");
            responseBuilder.append(h.getValue());
            responseBuilder.append("\n");
        });

        responseBuilder.append("BODY:");
        responseBuilder.append("\n");
        responseBuilder.append(response.getBody().prettyPrint());
        ScenarioManager.getInstance().getScenario().write(responseBuilder.toString());
        return response;
    }
}


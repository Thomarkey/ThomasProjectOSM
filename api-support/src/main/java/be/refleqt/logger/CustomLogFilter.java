package be.refleqt.logger;

import io.cucumber.datatable.dependency.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Headers;
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
        getHeaders(requestBuilder, requestSpec.getHeaders());

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
        getHeaders(responseBuilder, response.getHeaders());

        responseBuilder.append("BODY:");
        responseBuilder.append("\n");
        responseBuilder.append(response.getBody().prettyPrint());
        ScenarioManager.getInstance().getScenario().write(responseBuilder.toString());
        return response;
    }

    private void getHeaders(StringBuilder requestBuilder, Headers headers) {
        requestBuilder.append("\n");

        requestBuilder.append("HEADERS:");
        requestBuilder.append("\n");
        headers.forEach(h -> {
            requestBuilder.append("\t");
            requestBuilder.append(h.getName());
            requestBuilder.append(": ");
            requestBuilder.append(h.getValue());
            requestBuilder.append("\n");
        });
    }
}


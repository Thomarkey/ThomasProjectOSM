import be.refleqt.base.test.dto.ApiException;
import be.refleqt.base.test.dto.Pair;
import be.refleqt.logger.ScenarioManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Response;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;

public aspect Logger {

    private static org.apache.logging.log4j.Logger LOGGER;
    private static Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private static int responseCode;
    private static String responseHeaders;

    pointcut buildCall() : execution(* be.refleqt.base.test.dto.ApiClient.buildCall(..));

    static {
       LOGGER = LogManager.getLogger(Logger.class);
    }

    before() : buildCall() {
        Object[] args = thisJoinPoint.getArgs();
        String path = (String) args[0];
        String method = (String) args[1];
        List<Pair> queryParams = (List<Pair>) args[2];
        List<Pair> collectionQueryParams = (List<Pair>) args[3];
        Object body = args[4];
        Map<String, String> headerParams = (Map<String, String>) args[5];

        String queryParamString = "";
        String collectionQueryParamString = "";
        String headerString = "";

        Set<String> headers = (headerParams == null)? emptySet() : headerParams.keySet();
        for(String header : headers) {
            headerString = headerString + "\n\t" + header + " : " + headerParams.get(header);
        }
        for(Pair queryParam : queryParams) {
            queryParamString = queryParamString + "\n\t" + queryParam.getName() + " : " + queryParam.getValue();
        }
        for(Pair collectionQueryParam : collectionQueryParams) {
            collectionQueryParamString = collectionQueryParamString + "\n\t" + collectionQueryParam.getName() + " : " + collectionQueryParam.getValue();
        }

        if (ScenarioManager.getInstance().getScenario() != null) {
            ScenarioManager.getInstance().getScenario().write("<<< REQUEST >>>" +
                    "\nMETHOD: " + method +
                    "\nPATH: " + path +
                    "\nHEADERS: " + headerString +
                    "\nQUERY PARAMS: " + queryParamString +
                    "\nCOLLECTION QUERY PARAMS: " + collectionQueryParamString +
                    "\nBODY: \n" + GSON.toJson(body));
        }
    }

    pointcut handleResponse() : execution(* be.refleqt.base.test.dto.ApiClient.handleResponse(..));

    before() : handleResponse() {
        Object[] args = thisJoinPoint.getArgs();
        Response response = (Response) args[0];

        responseCode = response.code();
        responseHeaders = "";

        for(String header : response.headers().names()) {
            responseHeaders = responseHeaders + "\n\t" + header + " : " + response.headers().values(header);
        }
    }

    after() returning(Object o) : handleResponse() {
        if(o instanceof byte[]) {
        } else if(o instanceof File) {
        } else {
            if (ScenarioManager.getInstance().getScenario() != null) {
                ScenarioManager.getInstance().getScenario().write("<<< RESPONSE >>>" +
                        "\nCODE: " + responseCode +
                        "\nHEADERS: " + responseHeaders +
                        "\nRESPONSE BODY: \n" + GSON.toJson(o));
            }
        }
    }

    after() throwing(Exception e) : handleResponse() {
        if(e instanceof ApiException) {
            ApiException apiException = (ApiException) e;

            if (ScenarioManager.getInstance().getScenario() != null) {
                ScenarioManager.getInstance().getScenario().write("<<< RESPONSE >>>" +
                        "\nRESPONSE CODE: " + responseCode +
                        "\nHEADERS: " + responseHeaders +
                        "\nRESPONSE BODY: \n" + GSON.toJson(apiException.getResponseBody()));
            }
        }
    }
}
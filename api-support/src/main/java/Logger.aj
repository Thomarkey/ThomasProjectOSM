import be.refleqt.base.test.dto.*;
import be.refleqt.general.support.*;
import com.google.gson.*;
import java.io.*;
import static java.util.Collections.*;
import java.util.*;
import okhttp3.*;
import org.apache.logging.log4j.*;

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

        if (GenericScenarioManager.getScenario() != null) {
            GenericScenarioManager.writeLine("\n<<< REQUEST >>>" +
                    "\nMETHOD: " + method +
                    "\nPATH: " + path +
                    "\nHEADERS: " + headerString +
                    "\nQUERY PARAMS: " + queryParamString +
                    "\nCOLLECTION QUERY PARAMS: " + collectionQueryParamString +
                    "\nBODY: \n" + GSON.toJson(body) + "\n");
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
            if (GenericScenarioManager.getScenario() != null) {
                GenericScenarioManager.writeLine("\n<<< RESPONSE >>>" +
                        "\nCODE: " + responseCode +
                        "\nHEADERS: " + responseHeaders +
                        "\nRESPONSE BODY: \n" + GSON.toJson(o) + "\n");
            }
        }
    }

    after() throwing(Exception e) : handleResponse() {
        if(e instanceof ApiException) {
            ApiException apiException = (ApiException) e;

            if (GenericScenarioManager.getScenario() != null) {
                GenericScenarioManager.writeLine("\n<<< RESPONSE >>>" +
                        "\nRESPONSE CODE: " + responseCode +
                        "\nHEADERS: " + responseHeaders +
                        "\nRESPONSE BODY: \n" + GSON.toJson(apiException.getResponseBody()) + "\n");
            }
        }
    }
}
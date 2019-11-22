import be.refleqt.base.test.dto.*;
import be.refleqt.general.support.GenericScenarioManager;
import be.refleqt.logger.JsonNodeHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;

public aspect Logger {

    private static org.apache.logging.log4j.Logger LOGGER;
    private static Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private static ThreadLocal<StringBuilder> responsePrint = new ThreadLocal<>();
    private static final boolean isWip = StringUtils.containsIgnoreCase(System.getProperty("cucumberTag", "wip"), "wip");

    //Color
    private static final String RESET = "\033[0m";  // Text Reset
    private static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    private static final String CYAN_BOLD = "\033[1;36m";   // CYAN

    pointcut buildCall() : execution(* be..dto.ApiClient.buildCall(..)) || execution(* com..dto.ApiClient.buildCall(..))
            || execution(* eu..dto.ApiClient.buildCall(..));

    static {
        LOGGER = LogManager.getLogger(Logger.class);
    }

    before() : buildCall() {
        Object[] args = thisJoinPoint.getArgs();
        ApiClient apiClient = (ApiClient) thisJoinPoint.getThis();
        List<Pair> queryParams = (List<Pair>) args[2];
        List<Pair> collectionQueryParams = (List<Pair>) args[3];
        Map<String, String> headerParams = (Map<String, String>) args[5];

        try {
            headerParams.putAll((Map<String, String>) FieldUtils.readField(apiClient, "defaultHeaderMap", true));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        StringBuilder request = new StringBuilder();

        if (isWip) {
            request.append(YELLOW_BOLD).append("\n<<< REQUEST >>>").append(RESET);
        } else {
            request.append("\n<<< REQUEST >>>");
        }

        request.append("\nMETHOD: ").append((String) args[1])
                .append("\nPATH: ").append(apiClient.getBasePath()).append((String) args[0]);

        if (headerParams.size() > 0) {
            request.append("\nHEADERS: ");

            headerParams.keySet().forEach(
                    h -> request.append("\n\t").append(h).append(" : ").append(headerParams.get(h))
            );
        }

        if (queryParams.size() > 0) {
            request.append("\nQUERY PARAMS: ");
            for (Pair queryParam : queryParams) {
                request.append("\n\t").append(queryParam.getName()).append(" : ").append(queryParam.getValue());
            }
        }

        if (collectionQueryParams.size() > 0) {
            request.append("\nCOLLECTION QUERY PARAMS: ");
            for (Pair collectionQueryParam : collectionQueryParams) {
                request.append("\n\t").append(collectionQueryParam.getName()).append(" : ").append(
                        collectionQueryParam.getValue());
            }
        }
        request.append("\nBODY: \n").append(GSON.toJson(args[4])).append("\n");

        if (GenericScenarioManager.getScenario() != null) {
            GenericScenarioManager.writeLine(request.toString());
        }
    }

    pointcut handleResponse() : execution(* be..dto.ApiClient.handleResponse(..))
            || execution(* com..dto.ApiClient.handleResponse(..)) || execution(* eu..dto.ApiClient.handleResponse(..));

    before(): handleResponse() {
        Object[] args = thisJoinPoint.getArgs();
        responsePrint.set(new StringBuilder());
        Response response = (Response) args[0];

        if (isWip) {
            responsePrint.get().append(CYAN_BOLD).append("\n<<< RESPONSE >>>").append(RESET);
        } else {
            responsePrint.get().append("\n<<< RESPONSE >>>");
        }

        responsePrint.get().append("\nCODE: ").append(response.code());

        if (response.headers().size() > 0) {
            responsePrint.get().append("\nHEADERS: ");
            for (String header : response.headers().names()) {
                responsePrint.get().append("\n\t").append(header).append(" : ").append(response.headers().values(header));
            }
        }
    }

    after() returning(Object o) : handleResponse() {
        if (o instanceof byte[]) {
        } else if (o instanceof File) {
        } else {
            responsePrint.get().append("\nRESPONSE BODY: \n").append(GSON.toJson(o)).append("\n");

            if (GenericScenarioManager.getScenario() != null) {
                GenericScenarioManager.writeLine(responsePrint.get().toString());
            }
        }
    }

    after() throwing(Exception e) : handleResponse() {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;

            responsePrint.get().append("\nRESPONSE BODY: \n")
                    .append((JsonNodeHelper.readJsonToPrettyString(apiException.getResponseBody())))
                    .append("\n");

            if (GenericScenarioManager.getScenario() != null) {
                GenericScenarioManager.writeLine(responsePrint.get().toString());
            }
        }
    }
}
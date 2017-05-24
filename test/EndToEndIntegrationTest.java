import org.junit.Assert;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithServer;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.test.Helpers.*;
import static play.libs.Json.toJson;

public class EndToEndIntegrationTest extends WithServer {

    @Test
    public void shouldSuccessfullyTransferMoney() throws Exception {
        long fromUserId = createUser();
        long toUserId = createUser();

        final String body = transferMoney(fromUserId, toUserId, 10d);
        assertThat(body, containsString("Money transfer was successful."));

        validateUserHasCorrectAmount(fromUserId, 0);
        validateUserHasCorrectAmount(toUserId, 20);
    }

    @Test
    public void shouldNotSuccessfullyTransferMoneyDueToInsufficientAmount() throws Exception {
        long fromUserId = createUser();
        long toUserId = createUser();

        final String body = transferMoney(fromUserId, toUserId, 20d);
        assertThat(body, containsString("User with id: {"+ fromUserId +"} doesn't have enough amount: {20.000000} of currency: {GBP}"));

        validateUserHasCorrectAmount(fromUserId, 10);
        validateUserHasCorrectAmount(toUserId, 10);
    }

    private String transferMoney(long fromUserId, long toUserId, double amount) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("fromUser", fromUserId);
        bodyMap.put("toUser", toUserId);
        bodyMap.put("currency", "GBP");
        bodyMap.put("amount", amount);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/users/" + fromUserId +"/transfer")
                .bodyJson(toJson(bodyMap));

        Result result = route(app, request);
        return contentAsString(result);
    }

    private void validateUserHasCorrectAmount(long userId, double amount) {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/users/" + userId);

        Result result = route(app, request);
        final String body = contentAsString(result);

        double amountToValidate = Json.parse(body).get("user").get("wallet").get("amount").asDouble();
        Assert.assertTrue(amount == amountToValidate);
    }

    private long createUser() {
        Map<String, Object> bodyMap = new HashMap<>();
        Map<String, String> walletMap = new HashMap<>();

        walletMap.put("currency", "GBP");
        walletMap.put("amount", "10");

        bodyMap.put("name", "userName");
        bodyMap.put("wallet", walletMap);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/users/create")
                .bodyJson(toJson(bodyMap));

        Result result = route(app, request);
        final String body = contentAsString(result);

        return Json.parse(body).get("user").get("id").asLong();
    }

}
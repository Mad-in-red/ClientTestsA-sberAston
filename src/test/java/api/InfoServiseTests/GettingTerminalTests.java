package api.InfoServiseTests;

import BaseTests.BaseTerminalTest;
import Utilite.TerminalTestsUtilite;
import io.restassured.path.json.JsonPath;
import api.models.InfoService.TerminalSearchRequest;
import api.models.InfoService.TerminalsData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GettingTerminalTests extends BaseTerminalTest {

    @Test
    public void gettingTerminalByCityParameter() {
        String city = "Москва";

        JsonPath response = BaseTerminalTest.searchTerminals(city)
                .then()
                .log().all()
                .extract()
                .jsonPath();

        TerminalAssertance.verifyBasicResponse(response, 1, 6);
        TerminalAssertance.verifyTerminalsStructureList(response.getList("atm", TerminalsData.class));
    }

    @ParameterizedTest
    @MethodSource("paginationSizeAndNumber")
    void testAtmSearchWithPagination(int pageNumber, int pageSize, String city) {

        JsonPath response = BaseTerminalTest.searchTerminalWithPagination(city, pageNumber, pageSize)
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .jsonPath();

        TerminalAssertance.verifyPaginationResponse(response, pageNumber, pageSize);

        List<TerminalsData> terminals = response.getList("atm", TerminalsData.class);
        terminals.forEach(TerminalAssertance::verifyTerminalStructure);
    }


    @Test
    public void fullValidTerminalRequest() {

        TerminalSearchRequest request = new TerminalSearchRequest()
                .setCity("Москва")
                .setCashWithdraw(true)
                .setCashDeposit(true)
                .setAcceptPayments(true)
                .setMoneyTransfer(true)
                .setNfc(true)
                .setIs24Hour(false)
                .setSearchQuery("а")
                .setPageNumber(1)
                .setPageSize(2)
                .setSortBy("name");

        JsonPath response = given()
                .queryParam("city", request.getCity())
                .queryParam("cashWithdraw", request.isCashWithdraw())
                .queryParam("cashDeposit", request.isCashDeposit())
                .queryParam("acceptPayments", request.isAcceptPayments())
                .queryParam("moneyTransfer", request.isMoneyTransfer())
                .queryParam("nfc", request.isNfc())
                .queryParam("is24Hour", request.isIs24Hour())
                .queryParam("searchQuery", request.getSearchQuery())
                .queryParam("pageNumber", request.getPageNumber())
                .queryParam("pageSize", request.getPageSize())
                .queryParam("sortBy", request.getSortBy())
                .when()
                .get(ENDPOINT)
                .then()
                .log().all()
                .extract()
                .jsonPath();

        List<TerminalsData> terminals = response.getList("atm", TerminalsData.class);

        assertThat(terminals, not(empty()));
        terminals.forEach(this::verifyTerminalStructure);

        TerminalTestsUtilite.verifyPagination(response, request.getPageNumber(), request.getPageSize());
        TerminalTestsUtilite.verifySorting(terminals, request.getSortBy());
    }
}

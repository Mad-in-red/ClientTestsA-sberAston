package api;

import io.restassured.path.json.JsonPath;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class TerminalAssertance {

    public static void verifyPaginationResponse(JsonPath response, int expectedPageNumber, int expectedPageSize) {
        assertThat(response.getInt("count"), notNullValue());
        assertThat(response.getInt("pageNumber"), equalTo(expectedPageNumber));
        assertThat(response.getInt("pageSize"), equalTo(expectedPageSize));
        assertThat(response.getList("atm"), hasSize(lessThanOrEqualTo(expectedPageSize)));
    }

    public static void verifyTerminalStructure(TerminalsData terminal) {
        assertThat(terminal.getName(), not(emptyOrNullString()));
        assertThat(terminal.getStreet(), not(emptyOrNullString()));
        assertThat(terminal.getHouse(), not(emptyOrNullString()));
        assertThat(terminal.getAtmLatitude(), notNullValue());
        assertThat(terminal.getAtmLongitude(), notNullValue());
    }

    public static void verifyBasicResponse(JsonPath response, int expectedPageNumber, int expectedPageSize) {
        assertThat(response.getInt("count"), notNullValue());
        assertThat(response.getInt("pageNumber"), equalTo(expectedPageNumber));
        assertThat(response.getInt("pageSize"), equalTo(expectedPageSize));
        assertThat(response.getList("atm"), hasSize(greaterThan(0)));
    }

    public static void verifyTerminalsStructureList(List<TerminalsData> terminals) {
        terminals.forEach(terminal -> {
            assertThat(terminal.getName(), not(emptyOrNullString()));
            assertThat(terminal.getStreet(), not(emptyOrNullString()));
            assertThat(terminal.getHouse(), not(emptyOrNullString()));
            assertThat(terminal.getAtmLatitude(), notNullValue());
            assertThat(terminal.getAtmLongitude(), notNullValue());
        });
    }
}


package Utilite;

import io.restassured.path.json.JsonPath;
import api.models.InfoService.TerminalsData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class TerminalTestsUtilite {
    public static void verifyPagination(JsonPath response, int expectedPageNumber, int expectedPageSize) {
        assertThat(response.getInt("count"), greaterThanOrEqualTo(0));
        assertThat(response.getInt("pageNumber"), equalTo(expectedPageNumber));
        assertThat(response.getInt("pageSize"), equalTo(expectedPageSize));
    }

    public static void verifySorting(List<TerminalsData> terminals, String sortField) {
        if (terminals.size() > 1) {
            List<String> actualValues = terminals.stream()
                    .map(t -> getFieldValue(t, sortField))
                    .collect(Collectors.toList());

            List<String> sortedValues = new ArrayList<>(actualValues);
            sortedValues.sort(String.CASE_INSENSITIVE_ORDER);

            assertThat("ATM should be sorted by " + sortField, actualValues, equalTo(sortedValues));
        }
    }

    private static String getFieldValue(TerminalsData terminal, String field) {
        switch (field) {
            case "name": return terminal.getName();
            default: throw new IllegalArgumentException("Unknown sort field: " + field);
        }
    }
}

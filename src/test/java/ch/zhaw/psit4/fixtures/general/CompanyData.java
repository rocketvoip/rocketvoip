package ch.zhaw.psit4.fixtures.general;

/**
 * General methods for company related data.
 *
 * @author Rafael Ostertag
 */
public final class CompanyData {
    private CompanyData() {
        // intentionally empty
    }

    public static String getCompanyName(int number) {
        return "ACME " + number;
    }
}

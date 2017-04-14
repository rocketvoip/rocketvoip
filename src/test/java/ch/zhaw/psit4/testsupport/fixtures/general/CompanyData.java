package ch.zhaw.psit4.testsupport.fixtures.general;

/**
 * General methods for company related data.
 *
 * @author Rafael Ostertag
 */
public final class CompanyData {

    public static final String COMPANY_PREFIX = "ACME";

    private CompanyData() {
        // intentionally empty
    }

    public static String getCompanyName(int number) {
        return COMPANY_PREFIX + number;
    }
}

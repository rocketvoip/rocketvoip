package ch.zhaw.psit4.fixtures.database;

import ch.zhaw.psit4.data.jpa.entities.Company;
import ch.zhaw.psit4.fixtures.general.CompanyData;

/**
 * Create Company entities.
 *
 * @author Rafael Ostertag
 */
public final class CompanyEntity {
    private CompanyEntity() {
        // intentionally empty
    }

    public static Company createCompany(int number) {
        return new Company(CompanyData.getCompanyName(number));
    }

}

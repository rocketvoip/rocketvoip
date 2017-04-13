package ch.zhaw.psit4.testsupport.fixtures.domain;

import ch.zhaw.psit4.testsupport.fixtures.general.CompanyData;
import ch.zhaw.psit4.testsupport.fixtures.general.SipClientData;

/**
 * @author Rafael Ostertag
 */
public final class DialAppCallGenerator {
    public static final String TIMEOUT = "30";

    private DialAppCallGenerator() {
        // intentionally empty
    }

    public static String generateMultipleDialAppCalls(int numberOfClients, int companyNumber, String technology) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("Dial(");
        for (int i = 1; i <= numberOfClients; i++) {
            stringBuffer.append(technology);
            stringBuffer.append('/');
            stringBuffer.append(SipClientData.getSipClientLabel(i));
            stringBuffer.append('-');
            stringBuffer.append(CompanyData.getCompanyName(companyNumber));
            stringBuffer.append('&');
        }
        int stringLength = stringBuffer.length();
        int lastCharacterPosition = stringLength - 1;
        stringBuffer.delete(lastCharacterPosition, stringLength);
        stringBuffer.append(", ");
        stringBuffer.append(TIMEOUT);
        stringBuffer.append(')');

        return stringBuffer.toString();
    }
}

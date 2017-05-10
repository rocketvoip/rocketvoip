package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.AsteriskApplicationInterface;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class DialPlanExtensionTest {
    private DialPlanExtension dialPlanExtension;
    private AsteriskApplicationInterface dialPlanAppMock;

    @Before
    public void setUp() throws Exception {
        dialPlanExtension = new DialPlanExtension();

        dialPlanAppMock = mock(AsteriskApplicationInterface.class);
        when(dialPlanAppMock.toApplicationCall()).thenReturn("mockedApp");

        dialPlanExtension.setDialPlanApplication(dialPlanAppMock);
    }

    @Test
    public void getPhoneNumber() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        assertThat(dialPlanExtension.getPhoneNumber(), equalTo("123"));
    }

    @Test
    public void getPriority() throws Exception {
        dialPlanExtension.setPriority("1");
        assertThat(dialPlanExtension.getPriority(), equalTo("1"));
    }

    @Test
    public void getDialPlanApplication() throws Exception {
        assertThat(dialPlanExtension.getDialPlanApplication(), is(not(nullValue())));
    }

    @Test(expected = ValidationException.class)
    public void nullPhoneNumber() throws Exception {
        dialPlanExtension.setPhoneNumber(null);
        dialPlanExtension.setPriority("1");

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyPhoneNumber() throws Exception {
        dialPlanExtension.setPhoneNumber("");
        dialPlanExtension.setPriority("1");

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullPriority() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        dialPlanExtension.setPriority(null);

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyPriority() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        dialPlanExtension.setPriority("");

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullDialPlanApplication() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        dialPlanExtension.setPriority("1");
        dialPlanExtension.setDialPlanApplication(null);

        dialPlanExtension.validate();
    }

    @Test(expected = ValidationException.class)
    public void invalidDialPlanApplication() throws Exception {
        dialPlanExtension.setPhoneNumber("123");
        dialPlanExtension.setPriority("1");

        doThrow(new ValidationException("mocked validation failure")).when(dialPlanAppMock).validate();

        dialPlanExtension.validate();
    }

    @Test
    public void validate() throws Exception {
        dialPlanExtension.setPriority("1");
        dialPlanExtension.setPhoneNumber("123");

        dialPlanExtension.validate();

        verify(dialPlanAppMock, times(1)).validate();
    }


    @Test
    public void toDialPlanExtensionConfiguration() throws Exception {
        dialPlanExtension.setPriority("1");
        dialPlanExtension.setPhoneNumber("123");

        String expected = "exten=> 123, 1, mockedApp\n";
        assertThat(dialPlanExtension.toDialPlanExtensionConfiguration(), equalTo(expected));
    }


}
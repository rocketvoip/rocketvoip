package ch.zhaw.psit4.domain.beans;

import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanExtensionConfigurationInterface;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class DialPlanContextTest {
    private DialPlanContext dialPlanContext;
    private DialPlanExtension dialPlanExtensionMock;
    private List<DialPlanExtensionConfigurationInterface> extensionList;

    @Before
    public void setUp() throws Exception {
        dialPlanExtensionMock = mock(DialPlanExtension.class);
        when(dialPlanExtensionMock.toDialPlanExtensionConfiguration()).thenReturn("mockedExtension\n");

        extensionList = new ArrayList<>();
        extensionList.add(dialPlanExtensionMock);

        dialPlanContext = new DialPlanContext();
        dialPlanContext.setDialPlanExtensionList(extensionList);

    }
    @Test
    public void getContextName() throws Exception {
        dialPlanContext.setContextName("name");
        assertThat(dialPlanContext.getContextName(), equalTo("name"));
    }

    @Test
    public void getDialPlanExtensionList() throws Exception {
        // has been set by setUp().
        assertThat(dialPlanContext.getDialPlanExtensionList(), is(not(nullValue())));
    }

    @Test(expected = ValidationException.class)
    public void emptyContextName() throws Exception {
        dialPlanContext.setContextName("");

        dialPlanContext.validate();
    }

    @Test(expected = ValidationException.class)
    public void nullExtensionList() throws Exception {
        dialPlanContext.setContextName("name");
        dialPlanContext.setDialPlanExtensionList(null);

        dialPlanContext.validate();
    }

    @Test(expected = ValidationException.class)
    public void emptyExtensionList() throws Exception {
        dialPlanContext.setContextName("name");
        dialPlanContext.setDialPlanExtensionList(new ArrayList<>());

        dialPlanContext.validate();
    }

    @Test(expected = ValidationException.class)
    public void withInvalidExtension() throws Exception {
        dialPlanContext.setContextName("name");
        doThrow(new ValidationException("mocked validation exception")).when(dialPlanExtensionMock).validate();

        dialPlanContext.validate();
    }

    @Test
    public void toDialPlanContextConfiguration() throws Exception {
        dialPlanContext.setContextName("name");

        String expected = "[name]\nmockedExtension\n\n";
        assertThat(dialPlanContext.toDialPlanContextConfiguration(), equalTo(expected));
    }

    @Test
    public void toDialPlanContextConfigurationWithToExtensions() throws Exception {
        DialPlanExtension dialPlanExtensionMock2 = mock(DialPlanExtension.class);
        when(dialPlanExtensionMock2.toDialPlanExtensionConfiguration()).thenReturn("mockedExtension2\n");

        extensionList.add(dialPlanExtensionMock2);
        dialPlanContext.setContextName("name");

        String expected = "[name]\nmockedExtension\nmockedExtension2\n\n";
        assertThat(dialPlanContext.toDialPlanContextConfiguration(), equalTo(expected));
    }

    @Test
    public void toDialPlanContextConfigurationNullInterspersed() throws Exception {
        DialPlanExtension dialPlanExtensionMock2 = mock(DialPlanExtension.class);
        when(dialPlanExtensionMock2.toDialPlanExtensionConfiguration()).thenReturn("mockedExtension2\n");

        extensionList.add(null);
        extensionList.add(dialPlanExtensionMock2);
        dialPlanContext.setContextName("name");
        extensionList.add(null);

        String expected = "[name]\nmockedExtension\nmockedExtension2\n\n";
        assertThat(dialPlanContext.toDialPlanContextConfiguration(), equalTo(expected));
    }



    @Test
    public void validate() throws Exception {
        dialPlanContext.setContextName("name");
        dialPlanContext.validate();
    }

}
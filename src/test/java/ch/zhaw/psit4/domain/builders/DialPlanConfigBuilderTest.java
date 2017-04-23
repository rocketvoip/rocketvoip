package ch.zhaw.psit4.domain.builders;

import ch.zhaw.psit4.domain.beans.DialPlanContext;
import ch.zhaw.psit4.domain.beans.DialPlanExtension;
import ch.zhaw.psit4.domain.exceptions.InvalidConfigurationException;
import ch.zhaw.psit4.domain.exceptions.ValidationException;
import ch.zhaw.psit4.domain.interfaces.DialPlanAppInterface;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rafael Ostertag
 */
public class DialPlanConfigBuilderTest {
    private DialPlanConfigBuilder dialPlanConfigBuilder;

    @Before
    public void setUp() throws Exception {
        dialPlanConfigBuilder = new DialPlanConfigBuilder();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildingWithNoContexts() throws Exception {
        dialPlanConfigBuilder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewContextAddNewContext() throws Exception {
        dialPlanConfigBuilder
                .addNewContext(new DialPlanContext())
                .addNewContext(new DialPlanContext());
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewContextSetApplication() throws Exception {
        dialPlanConfigBuilder
                .addNewContext(new DialPlanContext())
                .setApplication(mock(DialPlanAppInterface.class));
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewExtension() throws Exception {
        dialPlanConfigBuilder.addNewExtension(new DialPlanExtension());
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewContextThenBuild() throws Exception {
        dialPlanConfigBuilder.addNewContext(new DialPlanContext()).build();
    }

    @Test(expected = IllegalStateException.class)
    public void wrongCallingOrderAddNewApp() throws Exception {
        dialPlanConfigBuilder.setApplication(mock(DialPlanAppInterface.class));
    }

    @Test(expected = InvalidConfigurationException.class)
    public void addNullContext() throws Exception {
        dialPlanConfigBuilder.addNewContext(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void addNullExtension() throws Exception {
        dialPlanConfigBuilder.addNewExtension(null);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void addNullApp() throws Exception {
        dialPlanConfigBuilder.setApplication(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void activateNonExistingContext() throws Exception {
        DialPlanContext dialPlanContext = spy(DialPlanContext.class);
        dialPlanContext.setContextName("name");

        DialPlanExtension dialPlanExtension = spy(DialPlanExtension.class);
        dialPlanExtension.setPhoneNumber("1234");
        dialPlanExtension.setPriority("1");

        DialPlanAppInterface dialPlanAppInterface = mock(DialPlanAppInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension)
                .setApplication(dialPlanAppInterface)
                .activateExistingContext("should raise exception")
                .build();
    }

    @Test(expected = ValidationException.class)
    public void activateExistingContextWhileUnfinishedContextIsActive1() throws Exception {
        // First context
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        DialPlanAppInterface dialPlanAppInterface1 = mock(DialPlanAppInterface.class);

        // Second Context
        DialPlanContext dialPlanContext2 = spy(DialPlanContext.class);
        dialPlanContext2.setContextName("name2");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setPriority("1");

        DialPlanAppInterface dialPlanAppInterface2 = mock(DialPlanAppInterface.class);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanAppInterface1)
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension2)
                // This here must fail
                .activateExistingContext("name");
    }

    @Test(expected = IllegalStateException.class)
    public void activateExistingContextWhileUnfinishedContextIsActive2() throws Exception {
        // First context
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        DialPlanAppInterface dialPlanAppInterface1 = mock(DialPlanAppInterface.class);

        // Second Context
        DialPlanContext dialPlanContext2 = spy(DialPlanContext.class);
        dialPlanContext2.setContextName("name2");

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanAppInterface1)
                .addNewContext(dialPlanContext2)
                // We don't add a new extension, thus it's an empty context and it must fail
                // This here must fail
                .activateExistingContext("name");
    }

    @Test
    public void activateExistingContext() throws Exception {
        // First context
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        DialPlanAppInterface dialPlanAppInterface1 = mock(DialPlanAppInterface.class);

        // Second Context
        DialPlanContext dialPlanContext2 = spy(DialPlanContext.class);
        dialPlanContext2.setContextName("name2");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setPriority("1");

        DialPlanAppInterface dialPlanAppInterface2 = mock(DialPlanAppInterface.class);

        // Must be added to first context
        DialPlanExtension dialPlanExtensionLateAdditions = spy(DialPlanExtension.class);
        dialPlanExtensionLateAdditions.setPhoneNumber("9101");
        dialPlanExtensionLateAdditions.setPriority("2");

        DialPlanAppInterface dialPlanAppInterfaceLateAddition = mock(DialPlanAppInterface.class);

        List<DialPlanContext> dialPlanContextList = dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanAppInterface1)
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanAppInterface2)
                // This here will be tested
                .activateExistingContext("name")
                .addNewExtension(dialPlanExtensionLateAdditions)
                .setApplication(dialPlanAppInterfaceLateAddition)
                .build();

        verify(dialPlanContext1, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanAppInterface1, atLeastOnce()).validate();
        verify(dialPlanContext2, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanAppInterface2, atLeastOnce()).validate();
        verify(dialPlanExtensionLateAdditions, atLeastOnce()).validate();
        verify(dialPlanAppInterfaceLateAddition, atLeastOnce()).validate();

        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(2));
        assertThat(((DialPlanExtension) dialPlanContext1.getDialPlanExtensionList().get(0)).getPhoneNumber(),
                equalTo("1234"));
        assertThat(((DialPlanExtension) dialPlanContext1.getDialPlanExtensionList().get(1)).getPhoneNumber(),
                equalTo("9101"));

        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(1));
        assertThat(((DialPlanExtension) dialPlanContext2.getDialPlanExtensionList().get(0)).getPhoneNumber(),
                equalTo("5678"));

    }

    @Test
    public void testSimpleValidConfiguration() throws Exception {
        DialPlanContext dialPlanContext = spy(DialPlanContext.class);
        dialPlanContext.setContextName("name");

        DialPlanExtension dialPlanExtension = spy(DialPlanExtension.class);
        dialPlanExtension.setPhoneNumber("1234");
        dialPlanExtension.setPriority("1");

        DialPlanAppInterface dialPlanAppInterface = mock(DialPlanAppInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext)
                .addNewExtension(dialPlanExtension)
                .setApplication(dialPlanAppInterface)
                .build();

        verify(dialPlanContext, atLeastOnce()).validate();
        verify(dialPlanExtension, atLeastOnce()).validate();
        verify(dialPlanAppInterface, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(1));

        assertThat(configuration.get(0), equalTo(dialPlanContext));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext.getDialPlanExtensionList(), hasSize(1));
        assertThat(dialPlanContext.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension));

        assertThat(dialPlanExtension.getDialPlanApplication(), equalTo(dialPlanAppInterface));
    }

    @Test
    public void testExhaustiveConfiguration() throws Exception {
        /*
         The configuration would look like this:

        [name1]
        exten=> 1234, 1, ...
        exten=> 5678, 2, ...

        [name2]
        exten=> 1111, 1, ...
        exten=> 2222, 2, ...
         */
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name1");

        DialPlanContext dialPlanContext2 = spy(DialPlanContext.class);
        dialPlanContext2.setContextName("name2");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setPriority("2");

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setPhoneNumber("1111");
        dialPlanExtension3.setPriority("1");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setPhoneNumber("2222");
        dialPlanExtension4.setPriority("2");

        DialPlanAppInterface dialPlanAppInterface1 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface2 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface3 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface4 = mock(DialPlanAppInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanAppInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanAppInterface2)

                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension3)
                .setApplication(dialPlanAppInterface3)
                .addNewExtension(dialPlanExtension4)
                .setApplication(dialPlanAppInterface4)
                .build();

        verify(dialPlanContext1, atLeastOnce()).validate();
        verify(dialPlanContext2, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension3, atLeastOnce()).validate();
        verify(dialPlanExtension4, atLeastOnce()).validate();
        verify(dialPlanAppInterface1, atLeastOnce()).validate();
        verify(dialPlanAppInterface2, atLeastOnce()).validate();
        verify(dialPlanAppInterface3, atLeastOnce()).validate();
        verify(dialPlanAppInterface4, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(2));

        assertThat(configuration.get(0), equalTo(dialPlanContext1));
        assertThat(configuration.get(1), equalTo(dialPlanContext2));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext1.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));

        assertThat(dialPlanExtension1.getDialPlanApplication(), equalTo(dialPlanAppInterface1));
        assertThat(dialPlanExtension2.getDialPlanApplication(), equalTo(dialPlanAppInterface2));


        assertThat(dialPlanContext2.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension3));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension4));

        assertThat(dialPlanExtension3.getDialPlanApplication(), equalTo(dialPlanAppInterface3));
        assertThat(dialPlanExtension4.getDialPlanApplication(), equalTo(dialPlanAppInterface4));
    }

    @Test
    public void testPriorityOrdering() throws Exception {
        /*
         The configuration would look like this:

        [name1]
        exten=> 1234, 1, ...
        exten=> 5678, 2, ...
        exten=> 9101, 3, ...
         */
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name1");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setPriority("2");

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setPhoneNumber("9101");
        dialPlanExtension3.setPriority("3");

        DialPlanAppInterface dialPlanAppInterface1 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface2 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface3 = mock(DialPlanAppInterface.class);

        List<DialPlanContext> configuration = dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension3)
                .setApplication(dialPlanAppInterface3)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanAppInterface2)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanAppInterface1)
                .build();


        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(3));
        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0).getPriority(), equalTo("1"));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1).getPriority(), equalTo("2"));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(2).getPriority(), equalTo("3"));
    }

    // This ain't working

//    @Test
//    public void testPriorityOrderingWithNonNumerPriority() throws Exception {
//        /*
//         The configuration would look like this:
//
//        [name1]
//        exten=> 1234, 1, ...
//        exten=> 1234, n, ...
//        exten=> 5678, 2, ...
//        exten=> 9101, 3, ...
//         */
//        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
//        dialPlanContext1.setContextName("name1");
//
//        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
//        dialPlanExtension1.setPhoneNumber("1234");
//        dialPlanExtension1.setPriority("1");
//
//        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
//        dialPlanExtension2.setPhoneNumber("1234");
//        dialPlanExtension2.setPriority("n");
//
//        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
//        dialPlanExtension3.setPhoneNumber("5678");
//        dialPlanExtension3.setPriority("3");
//
//        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
//        dialPlanExtension4.setPhoneNumber("9101");
//        dialPlanExtension4.setPriority("4");
//
//        DialPlanAppInterface dialPlanAppInterface1 = mock(DialPlanAppInterface.class);
//        DialPlanAppInterface dialPlanAppInterface2 = mock(DialPlanAppInterface.class);
//        DialPlanAppInterface dialPlanAppInterface3 = mock(DialPlanAppInterface.class);
//        DialPlanAppInterface dialPlanAppInterface4 = mock(DialPlanAppInterface.class);
//
//        List<DialPlanContext> configuration = dialPlanConfigBuilder
//                .addNewContext(dialPlanContext1)
//                .addNewExtension(dialPlanExtension4)
//                .setApplication(dialPlanAppInterface4)
//                .addNewExtension(dialPlanExtension2)
//                .setApplication(dialPlanAppInterface2)
//                .addNewExtension(dialPlanExtension3)
//                .setApplication(dialPlanAppInterface3)
//                .addNewExtension(dialPlanExtension1)
//                .setApplication(dialPlanAppInterface1)
//                .build();
//
//
//        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(4));
//        // Remember, the builder has side effects, so it essentially updates dialPlanContext
//        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0).getPriority(), equalTo("1"));
//        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1).getPriority(), equalTo("n"));
//        assertThat(dialPlanContext1.getDialPlanExtensionList().get(2).getPriority(), equalTo("3"));
//        assertThat(dialPlanContext1.getDialPlanExtensionList().get(3).getPriority(), equalTo("4"));
//    }

    @Test
    public void testExhaustiveConfigurationTwoBuildersWithBuild() throws Exception {
        /*
         The configuration would look like this:

        [name1]
        exten=> 1234, 1, ...
        exten=> 5678, 2, ...

        [name2]
        exten=> 1111, 1, ...
        exten=> 2222, 2, ...
         */
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name1");

        DialPlanContext dialPlanContext2 = spy(DialPlanContext.class);
        dialPlanContext2.setContextName("name2");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setPriority("2");

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setPhoneNumber("1111");
        dialPlanExtension3.setPriority("1");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setPhoneNumber("2222");
        dialPlanExtension4.setPriority("2");

        DialPlanAppInterface dialPlanAppInterface1 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface2 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface3 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface4 = mock(DialPlanAppInterface.class);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanAppInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanAppInterface2)
                // We let it build, to make sure we still can use this dialPlanConfigBuilder to initialize the new
                // one below.
                .build();

        DialPlanConfigBuilder dialPlanConfigBuilder2 = new DialPlanConfigBuilder(dialPlanConfigBuilder);

        List<DialPlanContext> configuration = dialPlanConfigBuilder2
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension3)
                .setApplication(dialPlanAppInterface3)
                .addNewExtension(dialPlanExtension4)
                .setApplication(dialPlanAppInterface4)
                .build();

        verify(dialPlanContext1, atLeastOnce()).validate();
        verify(dialPlanContext2, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension3, atLeastOnce()).validate();
        verify(dialPlanExtension4, atLeastOnce()).validate();
        verify(dialPlanAppInterface1, atLeastOnce()).validate();
        verify(dialPlanAppInterface2, atLeastOnce()).validate();
        verify(dialPlanAppInterface3, atLeastOnce()).validate();
        verify(dialPlanAppInterface4, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(2));

        assertThat(configuration.get(0), equalTo(dialPlanContext1));
        assertThat(configuration.get(1), equalTo(dialPlanContext2));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext1.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));

        assertThat(dialPlanExtension1.getDialPlanApplication(), equalTo(dialPlanAppInterface1));
        assertThat(dialPlanExtension2.getDialPlanApplication(), equalTo(dialPlanAppInterface2));


        assertThat(dialPlanContext2.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension3));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension4));

        assertThat(dialPlanExtension3.getDialPlanApplication(), equalTo(dialPlanAppInterface3));
        assertThat(dialPlanExtension4.getDialPlanApplication(), equalTo(dialPlanAppInterface4));
    }

    @Test
    public void testExhaustiveConfigurationTwoBuildersWithoutBuild() throws Exception {
        /*
         The configuration would look like this:

        [name1]
        exten=> 1234, 1, ...
        exten=> 5678, 2, ...

        [name2]
        exten=> 1111, 1, ...
        exten=> 2222, 2, ...
         */
        DialPlanContext dialPlanContext1 = spy(DialPlanContext.class);
        dialPlanContext1.setContextName("name1");

        DialPlanContext dialPlanContext2 = spy(DialPlanContext.class);
        dialPlanContext2.setContextName("name2");

        DialPlanExtension dialPlanExtension1 = spy(DialPlanExtension.class);
        dialPlanExtension1.setPhoneNumber("1234");
        dialPlanExtension1.setPriority("1");

        DialPlanExtension dialPlanExtension2 = spy(DialPlanExtension.class);
        dialPlanExtension2.setPhoneNumber("5678");
        dialPlanExtension2.setPriority("2");

        DialPlanExtension dialPlanExtension3 = spy(DialPlanExtension.class);
        dialPlanExtension3.setPhoneNumber("1111");
        dialPlanExtension3.setPriority("1");

        DialPlanExtension dialPlanExtension4 = spy(DialPlanExtension.class);
        dialPlanExtension4.setPhoneNumber("2222");
        dialPlanExtension4.setPriority("2");

        DialPlanAppInterface dialPlanAppInterface1 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface2 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface3 = mock(DialPlanAppInterface.class);
        DialPlanAppInterface dialPlanAppInterface4 = mock(DialPlanAppInterface.class);

        dialPlanConfigBuilder
                .addNewContext(dialPlanContext1)
                .addNewExtension(dialPlanExtension1)
                .setApplication(dialPlanAppInterface1)
                .addNewExtension(dialPlanExtension2)
                .setApplication(dialPlanAppInterface2);


        DialPlanConfigBuilder dialPlanConfigBuilder2 = new DialPlanConfigBuilder(dialPlanConfigBuilder);

        List<DialPlanContext> configuration = dialPlanConfigBuilder2
                .addNewContext(dialPlanContext2)
                .addNewExtension(dialPlanExtension3)
                .setApplication(dialPlanAppInterface3)
                .addNewExtension(dialPlanExtension4)
                .setApplication(dialPlanAppInterface4)
                .build();

        verify(dialPlanContext1, atLeastOnce()).validate();
        verify(dialPlanContext2, atLeastOnce()).validate();
        verify(dialPlanExtension1, atLeastOnce()).validate();
        verify(dialPlanExtension2, atLeastOnce()).validate();
        verify(dialPlanExtension3, atLeastOnce()).validate();
        verify(dialPlanExtension4, atLeastOnce()).validate();
        verify(dialPlanAppInterface1, atLeastOnce()).validate();
        verify(dialPlanAppInterface2, atLeastOnce()).validate();
        verify(dialPlanAppInterface3, atLeastOnce()).validate();
        verify(dialPlanAppInterface4, atLeastOnce()).validate();

        assertThat(configuration, is(not(nullValue())));
        assertThat(configuration, hasSize(2));

        assertThat(configuration.get(0), equalTo(dialPlanContext1));
        assertThat(configuration.get(1), equalTo(dialPlanContext2));

        // Remember, the builder has side effects, so it essentially updates dialPlanContext
        assertThat(dialPlanContext1.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext1.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext1.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension1));
        assertThat(dialPlanContext1.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension2));

        assertThat(dialPlanExtension1.getDialPlanApplication(), equalTo(dialPlanAppInterface1));
        assertThat(dialPlanExtension2.getDialPlanApplication(), equalTo(dialPlanAppInterface2));


        assertThat(dialPlanContext2.getDialPlanExtensionList(), is(not(nullValue())));
        assertThat(dialPlanContext2.getDialPlanExtensionList(), hasSize(2));

        assertThat(dialPlanContext2.getDialPlanExtensionList().get(0), equalTo(dialPlanExtension3));
        assertThat(dialPlanContext2.getDialPlanExtensionList().get(1), equalTo(dialPlanExtension4));

        assertThat(dialPlanExtension3.getDialPlanApplication(), equalTo(dialPlanAppInterface3));
        assertThat(dialPlanExtension4.getDialPlanApplication(), equalTo(dialPlanAppInterface4));
    }

}
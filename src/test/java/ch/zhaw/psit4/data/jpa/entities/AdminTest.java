package ch.zhaw.psit4.data.jpa.entities;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * @author Rafael Ostertag
 */
public class AdminTest {
    @Test
    public void testPasswordSetter() throws Exception {
        Admin admin = new Admin();
        admin.setPassword("test");

        assertThat(admin.getPassword(), is(not(equalTo("test"))));
    }

    @Test
    public void testConstructor() throws Exception {
        Admin admin = new Admin(null, "firstname", "lastname", "username", "password", false);
        assertThat(admin.getPassword(), is(not(equalTo("password"))));
        assertThat(admin.getCompany(), is(nullValue()));
        assertThat(admin.getUsername(), is(equalTo("username")));
        assertThat(admin.getLastname(), is(equalTo("lastname")));
        assertThat(admin.getFirstname(), is(equalTo("firstname")));
        assertThat(admin.getId(), is(equalTo(0L)));
        assertThat(admin.isSuperAdmin(), is(false));
    }

    @Test
    public void testSetters() throws Exception {
        Admin admin = new Admin(null, "firstname", "lastname", "username", "password", false);
        admin.setPassword("password2");
        admin.setCompany(Collections.emptyList());
        admin.setFirstname("firstname2");
        admin.setLastname("lastname2");
        admin.setUsername("username2");
        admin.setSuperAdmin(true);
        admin.setId(2);


        assertThat(admin.getPassword(), is(not(equalTo("password"))));
        assertThat(admin.getPassword(), is(not(equalTo("password2"))));

        assertThat(admin.getCompany(), is(not(nullValue())));

        assertThat(admin.getUsername(), is(equalTo("username2")));

        assertThat(admin.getLastname(), is(equalTo("lastname2")));

        assertThat(admin.getFirstname(), is(equalTo("firstname2")));

        assertThat(admin.getId(), is(equalTo(2L)));
        assertThat(admin.isSuperAdmin(), is(true));
    }

}
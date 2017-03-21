package ch.zhaw.psit4.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for ConfigurationController.
 *
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationController.class)
public class ConfigurationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getVersionedConfigZipTest() throws Exception {
        MvcResult mvcResult = this.mvc.perform(get("/v1/configuration")
                .accept("application/zip"))
                .andExpect(status().isOk())
                .andReturn();

        ByteArrayInputStream bais = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());

        ZipInputStream zipInputStream = new ZipInputStream(bais);

        ZipEntry zipEntry = zipInputStream.getNextEntry();
        String zipEntryName = zipEntry.getName();
        assertEquals("sip.conf", zipEntryName);
        zipInputStream.closeEntry();

        zipEntry = zipInputStream.getNextEntry();
        zipEntryName = zipEntry.getName();
        assertEquals("extension.conf", zipEntryName);
        zipInputStream.closeEntry();

        assertNull("there are more then two files in the zip", zipInputStream.getNextEntry());
        zipInputStream.close();
    }
}
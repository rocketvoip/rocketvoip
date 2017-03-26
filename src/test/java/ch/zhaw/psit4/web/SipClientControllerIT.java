package ch.zhaw.psit4.web;

import ch.zhaw.psit4.dto.SipClientDto;
import ch.zhaw.psit4.helper.Json;
import ch.zhaw.psit4.helper.SipClientGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static ch.zhaw.psit4.helper.matchers.SipClientDtoEqualTo.sipClientDtoEqualTo;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class SipClientControllerIT {
    private static final int NON_EXISTING_USER_ID = 100;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getAllSipClientEmpty() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(0))
        );
    }

    @Test
    public void getNonExistingSipClient() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients/{id}", NON_EXISTING_USER_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(equalTo("Could not find SIP Client with id " + NON_EXISTING_USER_ID))
        );
    }

    @Test
    public void deleteNonExistingSipCLient() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/v1/sipclients/{id}", NON_EXISTING_USER_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(startsWith("Could not delete SIP Client with id " +
                        NON_EXISTING_USER_ID))
        );
    }

    @Test
    public void createInvalidSipClient() throws Exception {
        SipClientDto sipClientDto = new SipClientDto();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/sipclients")
                        .content(Json.toJson(sipClientDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createSipClient() throws Exception {
        SipClientDto createdSipClient = createSipClient(1);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients/{id}", createdSipClient.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();
        SipClientDto actual = Json.toObjectTypeSafe(response, SipClientDto.class);

        assertThat(createdSipClient, sipClientDtoEqualTo(actual));
    }

    @Test
    public void getAllSipClients() throws Exception {
        SipClientDto createdSipClient1 = createSipClient(1);
        SipClientDto createdSipClient2 = createSipClient(2);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        SipClientDto[] actual = Json.toObjectTypeSafe(response, SipClientDto[].class);

        assertThat(actual, arrayContainingInAnyOrder(
                sipClientDtoEqualTo(createdSipClient1),
                sipClientDtoEqualTo(createdSipClient2)
        ));
    }

    @Test
    public void deleteSipClient() throws Exception {
        SipClientDto createdSipClient1 = createSipClient(1);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/v1/sipclients/{id}", createdSipClient1.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/sipclients/{id}", createdSipClient1.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        );
    }

    private SipClientDto createSipClient(int number) throws Exception {
        SipClientDto sipClientDto = SipClientGenerator.createTestSipClientDto(number);
        String creationResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/sipclients")
                        .content(Json.toJson(sipClientDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                jsonPath("$.id").value(not(equalTo(sipClientDto.getId())))
        ).andExpect(
                jsonPath("$.name").value(equalTo(sipClientDto.getName()))
        ).andExpect(
                jsonPath("$.phone").value(equalTo(sipClientDto.getPhone()))
        ).andExpect(
                jsonPath("$.secret").value(equalTo(sipClientDto.getSecret()))
        ).andReturn().getResponse().getContentAsString();

        return Json.toObjectTypeSafe(creationResponse, SipClientDto.class);
    }

}
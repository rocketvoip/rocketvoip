package ch.zhaw.psit4.web;

import ch.zhaw.psit4.data.jpa.repositories.CompanyRepository;
import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.helper.Json;
import ch.zhaw.psit4.services.implementation.CompanyGenerator;
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

import static ch.zhaw.psit4.helper.matchers.CompanyDtoEqualTo.companyDtoEqualTo;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jona Braun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CompanyControllerIT {
    private static final String V1_COMPANIES_PATH = "/v1/companies";
    private static final int NON_EXISTING_COMPANY_ID = 100;
    private final CompanyGenerator companyGenerator = new CompanyGenerator();

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        companyRepository.deleteAll();
    }

    @Test
    public void getAllCompaniesEmpty() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(0))
        );
    }

    @Test
    public void getNonExistingCompany() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH + "/{id}", NON_EXISTING_COMPANY_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(equalTo("Could not find company with id " + NON_EXISTING_COMPANY_ID))
        );
    }

    @Test
    public void deleteNonExistingCompany() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_COMPANIES_PATH + "/{id}", NON_EXISTING_COMPANY_ID)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.reason").value(startsWith("Could not delete company with id " +
                        NON_EXISTING_COMPANY_ID))
        );
    }

    @Test
    public void updateNonExistingCompany() throws Exception {
        CompanyDto companyDto = companyGenerator.getCompanyDto(1);

        mockMvc.perform(
                MockMvcRequestBuilders.put(V1_COMPANIES_PATH + "/{id}", NON_EXISTING_COMPANY_ID)
                        .content(Json.toJson(companyDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createInvalidCompany() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        mockMvc.perform(
                MockMvcRequestBuilders.post(V1_COMPANIES_PATH)
                        .content(Json.toJson(companyDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isBadRequest()
        );
    }

    @Test
    public void createCompany() throws Exception {
        CompanyDto createdCompanyDto = createNewCompany(1);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH + "/{id}", createdCompanyDto.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();
        CompanyDto actual = Json.toObjectTypeSafe(response, CompanyDto.class);

        assertThat(createdCompanyDto, companyDtoEqualTo(actual));

    }

    @Test
    public void getAllCompanies() throws Exception {
        CompanyDto createdCompanyDto1 = createNewCompany(1);
        CompanyDto createdCompanyDto2 = createNewCompany(2);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.length()").value(equalTo(2))
        ).andReturn().getResponse().getContentAsString();

        CompanyDto[] actual = Json.toObjectTypeSafe(response, CompanyDto[].class);

        assertThat(actual, arrayContainingInAnyOrder(
                companyDtoEqualTo(createdCompanyDto1),
                companyDtoEqualTo(createdCompanyDto2)
        ));
    }

    @Test
    public void updateCompany() throws Exception {
        CompanyDto createdCompany1 = createNewCompany(1);

        CompanyDto updatedCompany = companyGenerator.getCompanyDto(2);

        updatedCompany.setId(createdCompany1.getId());

        String putResult = mockMvc.perform(
                MockMvcRequestBuilders.put(V1_COMPANIES_PATH + "/{id}", createdCompany1.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(Json.toJson(updatedCompany))
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        CompanyDto actual = Json.toObjectTypeSafe(putResult, CompanyDto.class);
        assertThat(actual, companyDtoEqualTo(updatedCompany));

        String response = mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH + "/{id}", createdCompany1.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        actual = Json.toObjectTypeSafe(response, CompanyDto.class);

        assertThat(actual, companyDtoEqualTo(updatedCompany));
    }

    @Test
    public void deleteCompany() throws Exception {
        CompanyDto createdCompany1 = createNewCompany(1);
        mockMvc.perform(
                MockMvcRequestBuilders.delete(V1_COMPANIES_PATH + "/{id}", createdCompany1.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get(V1_COMPANIES_PATH + "/{id}", createdCompany1.getId())
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isNotFound()
        );
    }

    private CompanyDto createNewCompany(int number) throws Exception {
        CompanyDto companyDto = companyGenerator.getCompanyDto(number);

        String creationResponse = mockMvc.perform(
                MockMvcRequestBuilders.post(V1_COMPANIES_PATH)
                        .content(Json.toJson(companyDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                jsonPath("$.id").value(not(equalTo(companyDto.getId())))
        ).andExpect(
                jsonPath("$.name").value(equalTo(companyDto.getName()))
        ).andReturn().getResponse().getContentAsString();

        return Json.toObjectTypeSafe(creationResponse, CompanyDto.class);
    }
}
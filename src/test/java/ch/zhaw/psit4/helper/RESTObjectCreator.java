package ch.zhaw.psit4.helper;

import ch.zhaw.psit4.dto.CompanyDto;
import ch.zhaw.psit4.dto.SipClientDto;
import org.hamcrest.CoreMatchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RESTObjectCreator {

    private SipClientGenerator sipClientGenerator = new SipClientGenerator();
    private MockMvc mockMvc;

    /**
     * Set the mockMvc and the companyDto before calling this method.
     *
     * @param number number of sipClient
     * @return SipClientDto returned
     * @throws Exception if Json.toJson failed
     */
    public SipClientDto createSipClient(int number) throws Exception {
        SipClientDto sipClientDto = sipClientGenerator.createTestSipClientDto(number);
        String creationResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/sipclients")
                        .content(Json.toJson(sipClientDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(CoreMatchers.not(CoreMatchers.equalTo(sipClientDto.getId())))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(CoreMatchers.equalTo(sipClientDto.getName()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.phone").value(CoreMatchers.equalTo(sipClientDto.getPhone()))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.secret").value(CoreMatchers.equalTo(sipClientDto.getSecret()))
        ).andReturn().getResponse().getContentAsString();

        return Json.toObjectTypeSafe(creationResponse, SipClientDto.class);
    }

    /**
     * Set mockMvc before calling this method.
     *
     * @param number the number appended to the company name
     * @return CompanyDto
     * @throws Exception if Json.toJson failed
     */
    public CompanyDto createNewCompany(int number) throws Exception {
        CompanyDto companyDto = CompanyGenerator.getCompanyDto(number);

        String creationResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/companies")
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

    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public void setCompanyDto(CompanyDto companyDto) {
        sipClientGenerator.setCompanyDto(companyDto);
    }
}
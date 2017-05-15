package ch.zhaw.psit4.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Represents an admin which has admin rights for one ore more companies.
 *
 * @author Jona Braun
 */
@Data
@Validated
public class AdminDto {

    private long id;

    @NotNull
    private List<CompanyDto> companyDtoList;

    @NotNull
    @Size(min = 2)
    private String firstName;

    @NotNull
    @Size(min = 2)
    private String lastName;

    @NotNull
    @Size(min = 2)
    private String userName;

}

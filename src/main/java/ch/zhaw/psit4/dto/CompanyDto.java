package ch.zhaw.psit4.dto;

import lombok.Data;

/**
 * Company DTO. Used by Controllers to transfer company information.
 *
 * @author Jona Braun
 */
@Data
public class CompanyDto {
    private String name;

    private Long id;

}

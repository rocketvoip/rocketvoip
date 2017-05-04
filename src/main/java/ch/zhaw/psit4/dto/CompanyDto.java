package ch.zhaw.psit4.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Company DTO. Used by Controllers to transfer company information.
 *
 * @author Jona Braun
 */
public class CompanyDto {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Long id;

}

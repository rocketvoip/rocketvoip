package ch.zhaw.psit4.dto;

/**
 * Company DTO. Used by Controllers to transfer company information.
 *
 * @author Jona Braun
 */
public class CompanyDto {
    private String name;
    private Long id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

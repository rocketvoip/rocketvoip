package ch.zhaw.psit4.dto;

/**
 * @author Rafael Ostertag
 */
public class HelloDto {
    private String greetingString;
    private String name;

    public HelloDto() {
        greetingString = "Hello";
        name = "World";
    }

    public String getGreetingString() {
        return greetingString;
    }

    public void setGreetingString(String greetingString) {
        this.greetingString = greetingString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

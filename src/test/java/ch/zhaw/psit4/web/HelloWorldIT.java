package ch.zhaw.psit4.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafael Ostertag
 */
@RunWith(SpringRunner.class)
@WebMvcTest(HelloWorld.class)
public class HelloWorldIT {

    @Autowired
    private MockMvc mvc;


    @Test
    public void getHello() throws Exception {
        this.mvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.greetingString", is("Hello")))
                .andExpect(jsonPath("$.name", is("World")));
    }


    @Test
    public void fail() throws Exception {

    }
}
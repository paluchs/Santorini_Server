package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void createUser() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201)) //andDo(print()).
                .andExpect(jsonPath("$._links", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo("testUser")));

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(409));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void getUser() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"));

        this.mvc.perform(get("/users/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$._links", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo("testUser")));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void changeOnlineStatus() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201)) //andDo(print()).
                .andExpect(jsonPath("$._links", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo("testUser")))
                .andExpect(jsonPath("$.status", equalTo("ONLINE")));

        this.mvc.perform(post("/login/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().is(202))
                .andExpect(jsonPath("$.status", equalTo("OFFLINE")));


        this.mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(202))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.status", equalTo("ONLINE")));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void updateUser() throws Exception {
        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201)) //andDo(print()).
                .andExpect(jsonPath("$._links", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo("testUser")))
                .andExpect(jsonPath("$.status", equalTo("ONLINE")));

        MvcResult result =
        this.mvc.perform(get("/users/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$._links", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo("testUser")))
                .andReturn();

        String jsonAsString = result.getResponse().getContentAsString();

        JSONObject jsonObj = new JSONObject(jsonAsString);
        String token = jsonObj.getString("token");
        String putBody = "{\"name\": \"testName\",\"username\": \"testUserUpdated\", \"birthdayDate\": \"1993-02-06\", \"token\": \"" + token + "\"}";



        this.mvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(putBody))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$.name", equalTo("testName")))
                .andExpect(jsonPath("$.username", equalTo("testUserUpdated")))
                .andExpect(jsonPath("$.birthdayDate", equalTo("1993-02-06")));

        this.mvc.perform(put("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(putBody))
                .andExpect(status().is(404));
    }





}

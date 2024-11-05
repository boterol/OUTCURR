package co.edu.icesi.dev.outcome_curr_mgmt.testing.system.rs.curriculum_qa;

import co.edu.icesi.dev.outcome_curr.mgmt.model.stdoutdto.faculty.FacultyOutDTO;
import co.edu.icesi.dev.outcome_curr.mgmt.rs.banner.BannerFacultyImportController;
import co.edu.icesi.dev.outcome_curr_mgmt.testing.system.rs.util.BaseSmokeIT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = {BannerFacultyImportController.class})
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssmntGenPlanControllerSmokeIT extends BaseSmokeIT {

    public static final String OUT_CURR_TEST_USER = "OutCurrTestUser";
    public static final String USER_PASSWORD = "123456";
    private static String testUserJWTToken;
    public static final String V_1_AUTH_IMPORT_FACULTIES = "/outcurrapi/v1/external/banner/faculties/";
    public static final String OUTCURRAPI_V_1_AUTH_FACULTIES = "/outcurrapi/v1/auth/faculties/";

    public static final int FAC_ID = 100;
    public static final int ACAD_PROG_ID = 10;
    public static final int ASSMNT_GEN_PLAN_ID = 1;
    public static final String OUTCURRAPI_V_1_AUTH_ASSESMENTS= "/outcurrapi/v1/auth/faculties/"+FAC_ID+"/acad_programs/"+ACAD_PROG_ID+"/assessemnt_plans/";


    @Value("${test.server.url}")
    private String server;

    @BeforeAll
    void init() {
        testUserJWTToken = getTestUserJWTToken(OUT_CURR_TEST_USER, USER_PASSWORD, server);
    }


    @Test
    void getAssessments() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpEntity<String> jwtEntity = new HttpEntity<>(getRequestHeaders());

        String url = server + OUTCURRAPI_V_1_AUTH_ASSESMENTS;
        ResponseEntity<String> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                jwtEntity,
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private HttpHeaders getRequestHeaders() {
        String token = "Bearer " + testUserJWTToken;
        HttpHeaders headers = getHeaders();
        headers.set("Authorization", token);
        return headers;
    }

    private void deleteFaculty(Long facId){
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String token = "Bearer " + testUserJWTToken;
        HttpHeaders headers = getHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> jwtEntity = new HttpEntity<>(headers);

        String url = server + OUTCURRAPI_V_1_AUTH_FACULTIES + "/";
        ResponseEntity<Void> response = testRestTemplate.exchange(
                url+facId,
                HttpMethod.DELETE,
                jwtEntity,
                Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}


package com.testingapi.demo.working;

import org.testng.annotations.Test;

import com.testingapi.demo.data.reusablejsonpath;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class JiraApi {
	@Test
	public static void jiraApilogin() throws IOException {

		RestAssured.baseURI = "http://localhost:8080";

		String response = given().header("Content-Type", "application/json")

				.body("{ \"username\": \"admin\", \"password\": \"admin\" }")

				.when().post("/rest/auth/1/session")

				.then().log().all().extract().response().asString();

		JsonPath js = reusablejsonpath.rawtoJson(response);
		String sid = js.get("session.value");
		System.out.println("This is my new sesson id ===>" + sid);

		// Create a issue in jira
		String newresponse = given()

				.header("Content-Type", "application/json").header("cookie", sid).auth().preemptive()
				.basic("admin", "admin")
				.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\purna\\Desktop\\SBI\\API_New_Id.json"))))
				
				.when().post("/rest/api/2/issue")
				
				.then().log().all()
				.assertThat().statusCode(201)
				.extract().response().asString();

		System.out.println("This is " + newresponse + " ======>");

	}
}

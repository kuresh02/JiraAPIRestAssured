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

		//Authorization
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

				.header("Content-Type", "application/json").header("cookie", sid)
				.auth().preemptive().basic("admin", "admin")
//				.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\purna\\Desktop\\SBI\\API_New_Id.json"))))
				.body("{\r\n"
						+ "  \"fields\": {\r\n"
						+ "    \"project\": {\r\n"
						+ "      \"key\": \"RSA\"\r\n"
						+ "\r\n"
						+ "    },\r\n"
						+ "    \"summary\": \"RestAPI is created \",\r\n"
						+ "    \"description\": \"Creating my latest RSA-7 issue with postman\",\r\n"
						+ "    \"issuetype\": {\r\n"
						+ "			\"name\": \"Bug\"\r\n"
						+ "		}\r\n"
						+ "  }\r\n"
						+ "}")
				
				.when().post("/rest/api/2/issue")
				
				.then().log().all()
				.assertThat().statusCode(201)
				.extract().response().asString();

		System.out.println("This is " + newresponse + " ======>");
		JsonPath js1 = reusablejsonpath.rawtoJson(newresponse);
		String issueid= js1.get("id");
		System.out.println("Created issue id is "+issueid);
		
		//comment in a issue that we create previously 
		
		String response2= given()
			.pathParam("id",issueid)
			.auth().preemptive().basic("admin", "admin")
			.header("Content-Type", "application/json")
			.body("{\r\n"
					+ "    \"body\": \"This is my new comment in Jira through Postman\",\r\n"
					+ "    \"visibility\": {\r\n"
					+ "        \"type\": \"role\",\r\n"
					+ "        \"value\": \"Administrators\"\r\n"
					+ "    }\r\n"
					+ "}")
		.when()
			.post("/rest/api/2/issue/{id}/comment")
		.then()
			.log().all().assertThat().statusCode(201).extract().response().asString();
		JsonPath js2 = reusablejsonpath.rawtoJson(response2);
		String commentid =js2.get("id");
		
		given()
		.pathParam("commentid",commentid)
		.pathParam("issueid",issueid)
		.auth().preemptive().basic("admin", "admin")
		.header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"body\": \"This is my 2nd comment in Jira through Postman\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.when()
			.put("http://localhost:8080/rest/api/2/issue/{issueid}/comment/{commentid}")
		
		.then()
		.log().all()
		.assertThat().statusCode(200);
		System.out.println("This is sooo good API put over");
		
		
	}
}

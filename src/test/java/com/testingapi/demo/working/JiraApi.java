package com.testingapi.demo.working;

import org.testng.Assert;
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

import javax.mail.Session;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraApi {
	@Test
	public static void jiraApilogin() throws IOException {

		//Authorization
		RestAssured.baseURI = "http://localhost:8080";
		SessionFilter session = new SessionFilter();
		String response = 
				given()
					.header("Content-Type", "application/json")
					.body("{ \"username\": \"admin\", \"password\": \"admin\" }")
					.filter(session)

				.when()
					.post("/rest/auth/1/session")

				.then()
					.log().all()
					.extract().response().asString();

		JsonPath js = reusablejsonpath.rawtoJson(response);
		String sid = js.get("session.value");
		System.out.println("This is my new sesson id ===>" + sid);

		// Create a issue in jira
		String newresponse = given()

				.header("Content-Type", "application/json").header("cookie", sid)
				.auth().preemptive().basic("admin", "admin")
				.filter(session) //to capture the session initially created
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
				
				.when()
					.post("/rest/api/2/issue")
				
				.then()
					.log().all()
					.assertThat().statusCode(201)
					.extract().response().asString();

		System.out.println("This is " + newresponse + " ======>");
		JsonPath js1 = reusablejsonpath.rawtoJson(newresponse);
		String issueid= js1.get("id");
		System.out.println("Created issue id is "+issueid);
		
		//comment in a issue that we create previously 
		
		String response2= given()
			.pathParam("id",issueid)
//			.auth().preemptive().basic("admin", "admin")
			.filter(session) //to capture the session initially created
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
		
		// update cooment
		given()
			.pathParam("commentid",commentid)
			.pathParam("issueid",issueid)
//			.auth().preemptive().basic("admin", "admin")
			.filter(session) //to capture the session initially created
			.header("Content-Type", "application/json")
			.body("{\r\n"
					+ "    \"body\": \"This is my 2nd comment in Jira through Postman\",\r\n"
					+ "    \"visibility\": {\r\n"
					+ "        \"type\": \"role\",\r\n"
					+ "        \"value\": \"Administrators\"\r\n"
					+ "    }\r\n"
					+ "}")
			.filter(session)
		.when()
			.put("http://localhost:8080/rest/api/2/issue/{issueid}/comment/{commentid}")
		
		.then()
		.log().all()
		.assertThat().statusCode(200);
		System.out.println("This is sooo good API put over");
		
		//delete a request, here before 2 req deleted
		given()
//			.auth().preemptive().basic("admin", "admin")
			.filter(session) //to capture the session initially created
			.pathParam("issueid", (Integer.parseInt(issueid)-5))
		
		.when()
			.delete("/rest/api/2/issue/{issueid}"); 
		
		//add attachment
		
		given()
			.header("X-Atlassian-Token","no-check")
			.header("Content-Type", "multipart/form-data")
			.multiPart("file",new File("pppeee.txt"))
			.filter(session)
			.pathParam("key", issueid)
		.when()
			.post("/rest/api/2/issue/{key}/attachments")
		.then()
			.log().all().assertThat().statusCode(200);
		System.out.println("----------ooooooooo---------");
		
		String expected ="This is my 3rd and final comment";
		
		String response3 = given()	
			.filter(session)
			.pathParam("issueid", 10014)  //10101
			.queryParam("fields", "comment")
			.log().all()
		.when()
			.get("/rest/agile/1.0/issue/{issueid}")
		.then()
			.log().all()
			.assertThat().statusCode(200)
			.extract().response().asString();
		JsonPath js4 = reusablejsonpath.rawtoJson(response3);
		String actualid = js4.get("id");
		int arraysize = js4.getInt("fields.comment.comments.size()  ");
		for(int i=0;i<arraysize;i++) {
			String  commentids= js4.get("fields.comment.comments["+i+"].id");
			System.out.println(commentids);
			if(commentids.equalsIgnoreCase("10106")) {
				String msg=js4.getString("fields.comment.comments["+i+"].body");
					
					Assert.assertEquals(msg, expected);
				
			}
		}
	}
}

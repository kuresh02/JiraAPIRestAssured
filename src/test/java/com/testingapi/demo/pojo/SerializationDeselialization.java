package com.testingapi.demo.pojo;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import com.testingapi.demo.data.reusablejsonpath;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class SerializationDeselialization {
	@Test
		public static void TestPojo() {
		
			
			RestAssured.baseURI="https://rahulshettyacademy.com";
			SessionFilter sf = new SessionFilter();	
			String response= given()
				.filter(sf)
				.relaxedHTTPSValidation()
				/**client grant, aAuthorization Code
				PKCE
				Client Credentials
				Device Code
				Refresh Token**/
				.formParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
				.formParam("grant_type","client_credentials")
				.formParam("scope","trust")
				
				
			.when()
				.post("oauthapi/oauth2/resourceOwner/token")
				
			.then()
				.log().all()
				.extract().response().asString();
			JsonPath js = reusablejsonpath.rawtoJson(response);
			String token=js.get("access_token");
			System.out.println(token+" This is my access token");
			
			
			//access resources throug accesstoken
			String response1= given()
				.filter(sf)	
				.queryParam("access_token", token)
			
			.when()
				.get("/oauthapi/getCourseDetails")
			.then()
				.log().all().extract().response().asString();
			
//			DataDetails dd= given()
//					.relaxedHTTPSValidation()
//					.filter(sf)
//					.queryParam("access_token", token).expect().defaultParser(Parser.JSON)
//					
//					.when()
//						.get("https://rahulshettyacademy.com/getCourse.php")
//						.as(DataDetails.class);
//			
//			System.out.println(dd.getCourses());
//				
			DataDetails dd=	given()
					.queryParams("access_token", token)
					.when().log().all()
					.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(DataDetails.class);
			System.out.println(dd.getInstructor());
		}
	
}

package com.testingapi.demo.working;
import org.testng.annotations.Test;

import com.testingapi.demo.data.reusablejsonpath;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class OAuth2 {
	@Test
	
		public static void oAuthCheck() {
		
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
		given()
			.filter(sf)	
			.queryParam("access_token", token)
		
		.when()
			.get("/oauthapi/getCourseDetails")
		.then()
			.log().all();
		
		
		}
	
}

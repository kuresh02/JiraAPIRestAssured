package com.testingapi.demo;

import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class TestingEcom {

	@Test
	public static void testingEcom() {
		RequestSpecification req =new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON)
	            .build();
		ResponseSpecification res=new ResponseSpecBuilder().expectContentType(ContentType.JSON).expectStatusCode(200).build();
		
		
		Logindetails log = new Logindetails();
		log.setUserEmail("repir89954@ahieh.com");
		log.setUserPassword("Kingking02@");
				
		RequestSpecification req1 = given().spec(req).body(log);
		
		LoginResponse logrespo = req1.when()
			.post("/api/ecom/auth/login")
		
		.then()
			.log().all().extract().response().as(LoginResponse.class);
		
			System.out.println((logrespo.getToken())+(logrespo.getUserId())+(logrespo.getMessage()));
	}
}

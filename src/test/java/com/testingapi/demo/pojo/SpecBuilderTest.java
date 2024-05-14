package com.testingapi.demo.pojo;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SpecBuilderTest {
	@Test
	public void specBuilderTest() {

		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();

		String res = given().spec(req).body("dfheyh")

				.when()
					.post("/maps/api/place/add/json")
				.then()
					.assertThat().statusCode(200).extract().response().asString();

	}
}

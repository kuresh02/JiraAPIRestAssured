package com.testingapi.demo.pojo;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class Serialization {
	@Test
	public void ss() {
		SlClass sc =new SlClass();
		sc.setAccuracy(50);
		sc.setName("Frontline house");
		sc.setPhone_number("(+91) 983 893 3937");
		sc.setAddress("29, side layout, cohen 09");
		sc.setLanguage("French-IN");
		sc.setWebsite("http://google.com");
		
		List<String> ls =new ArrayList<String>();
		ls.add("shoe park");
		ls.add("shop");
		sc.setTypes(ls);
		
		Location loc = new Location();
		loc.setLat(-38.383494);
		loc.setLng(33.427362);
		sc.setLocation(loc);
		
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String re =given()
			.queryParam("key","qaclick123")
			
			.body(sc).log().all()
		
		.when()
			.post("/maps/api/place/add/json")
	
		.then()
		.log().all()
		.assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(re+"====<");
	}
}

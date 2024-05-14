package com.testingapi.demo.pojo;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Serialization {
    
    // Java object to JSON format serialization test
    @Test
    public void serializeTest() {
        // Creating an instance of SlClass and setting its properties
        SlClass sc = new SlClass();
        sc.setAccuracy(50);
        sc.setName("Frontline house");
        sc.setPhone_number("(+91) 983 893 3937");
        sc.setAddress("29, side layout, cohen 09");
        sc.setLanguage("French-IN");
        sc.setWebsite("http://google.com");

        // Adding types to the list
        List<String> ls = new ArrayList<String>();
        ls.add("shoe park");
        ls.add("shop");
        sc.setTypes(ls);

        // Creating an instance of Location and setting its properties
        Location loc = new Location();
        loc.setLat(-38.383494);
        loc.setLng(33.427362);
        sc.setLocation(loc);

        // Building request specification for the REST API call
        RequestSpecification req = new RequestSpecBuilder()
            .setBaseUri("https://rahulshettyacademy.com")
            .addQueryParam("key", "qaclick123")
            .setContentType(ContentType.JSON)
            .build();

        // Building response specification for the REST API call
        ResponseSpecification rsp = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();

        // Making POST request to add data and getting response as string
        String res = given().spec(req).body(sc)
            .when()
                .post("/maps/api/place/add/json")
            .then()
                .assertThat().spec(rsp)
                .extract().response().asString();

        // Printing the response
        System.out.println(res);


		
		
		//=============
/**		RestAssured.baseURI="https://rahulshettyacademy.com";
		String re =given()
			.queryParam("key","qaclick123")
			
			.body(sc).log().all()
		
		.when()
			.post("/maps/api/place/add/json")
	
		.then()
		.log().all()
		.assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(re+"====<"); **/
	}
}

package com.testingapi.demo.working;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import com.testingapi.demo.data.Load;
import com.testingapi.demo.data.reusablejsonpath;

import groovyjarjarantlr4.v4.parse.GrammarTreeVisitor.outerAlternative_return;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class testerCheking {
	public static void main(String[] args) {
		RestAssured.baseURI ="https://rahulshettyacademy.com";
		String response1 = given()
			.queryParam("key","qaclick123")
			.header("Content-Type","application/json")
			.body(Load.lod())
			
		.when()
			.post("/maps/api/place/add/json")
		
		.then()
			.assertThat().statusCode(200)
			.log().all().extract().response().asString();
		System.out.println("=============");
		System.out.println(response1);
		
		JsonPath jp = new JsonPath(response1);
		String placeid= jp.getString("place_id");
		System.out.println(placeid);
		
		String address="India,West Side";
		//update place
		given()
			.queryParam("key","qaclick123")
			.header("Content-Type","application/json")
			.body("{\r\n"
					+ "\"place_id\":\""+placeid+"\",\r\n"
					+ "\"address\":\""+address+"\",\r\n"
					+ "\"key\":\"qaclick123\"\r\n"
					+ "}\r\n"
					+ "")
		.when()
			.put("/maps/api/place/update/json")
		
		.then()
			.assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"))
			.log().all();
		
		String getbody= given()
			.queryParam("key","qaclick123")
			.queryParam("place_id",placeid)
			
			.header("Content-Type","application/json")
		.when().get("/maps/api/place/get/json")
		.then().log().all().extract().response().asString();
		JsonPath jp1=reusablejsonpath.rawtoJson(getbody);
		
		String actualadd =jp1.getString("address");
		Assert.assertEquals(actualadd, address);
		System.out.println("its true address is updated");
	}
}

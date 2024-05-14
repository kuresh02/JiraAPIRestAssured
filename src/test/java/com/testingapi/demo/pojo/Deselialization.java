package com.testingapi.demo.pojo;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.*;

import org.apache.commons.math3.analysis.function.Add;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.testingapi.demo.data.reusablejsonpath;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class Deselialization {
	//Deselialization is nothing but to convert a json file to java object  format
	//for the earch nested json and array object we need to create seprate class and also create pojo class.
	//make sure each method in pojo class return proper value
	
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
//					
//					.filter(sf)
//					.queryParam("access_token", token).expect().defaultParser(Parser.JSON)
//					
//					.when()
//						.get("https://rahulshettyacademy.com/getCourse.php")
//						.as(DataDetails.class);
//			
//			System.out.println(dd.getCourses());
//			
			String[] al= {"Selenium Webdriver Java","Cypress","Protractor"};
			DataDetails dd=	given()
					.queryParams("access_token", token)
					.expect().defaultParser(Parser.JSON)
				.when()
				.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(DataDetails.class);// return type is class type
				System.out.println(dd.getInstructor());
				// fetching the course
				int sz=dd.getCourses().getWebAutomation().size();
				int sum =0;
				ArrayList<String> a =new ArrayList<String>();
				for(int i=0;i<sz;i++) {
					String title =dd.getCourses().getWebAutomation().get(i).getCourseTitle();
					System.out.println("The "+i+" st/nd/rd course title is "+ title);
					String price =dd.getCourses().getWebAutomation().get(i).getPrice();
					System.out.println("The course title is "+ price);
					sum=sum+Integer.valueOf(price);
					a.add(title);
				
				}
				System.out.println("The course price is "+ sum);
				Assert.assertEquals(sum, 130);
				System.out.println("The api call is successful");
				java.util.List<String> nl = Arrays.asList(al);
				System.out.println(a+"========="+nl);
				Assert.assertEquals(a, nl);
				System.out.println("done========");
				
				
		}
	
}

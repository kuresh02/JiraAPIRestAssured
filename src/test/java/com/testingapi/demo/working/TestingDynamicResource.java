package com.testingapi.demo.working;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.testingapi.demo.data.Load;
import com.testingapi.demo.data.reusablejsonpath;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class TestingDynamicResource {
	@Test(dataProvider = "getBookData")

	public void dynamictest(String isbn, String aisle) throws IOException {

		RestAssured.baseURI = "http://216.10.245.166";
		String bookvalue = given().queryParam("key", "Library/Addbook.php").header("Content-Type", "application/json")
				//converting byte code to string as we 1st need to upload our file which is in byte format and then we need to covert this byte code to string 
				.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\purna\\Desktop\\Word\\ApiTesting.json"))))

				.when().post("Library/Addbook.php")

				.then().extract().response().asString();

		JsonPath js = reusablejsonpath.rawtoJson(bookvalue);
		String idvalue = js.getString("ID");
		System.out.println(idvalue);
	}

	@DataProvider
	public Object[][] getBookData() {
		return new Object[][] { { "ojfwty", "9363" }, { "cwetee", "4253" }, { "ojfwty", "533" } };
	}

}

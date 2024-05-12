package com.testingapi.demo.data;

import io.restassured.path.json.JsonPath;

public class reusablejsonpath{
	public  static JsonPath rawtoJson(String response) {
	JsonPath jp1 = new JsonPath(response);
	return jp1;
	}
}

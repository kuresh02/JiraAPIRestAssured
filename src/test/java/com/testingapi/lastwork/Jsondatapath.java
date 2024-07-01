package com.testingapi.lastwork;
import io.restassured.path.json.JsonPath;

public class Jsondatapath{
	public static JsonPath repetetivemethod(String str) {
		JsonPath js = new JsonPath(str);
		return js;
	}
}
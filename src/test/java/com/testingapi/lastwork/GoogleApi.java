package com.testingapi.lastwork;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class GoogleApi {
    public static void main(String args[]) throws IOException {
        System.out.println("=======00000======");
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        SessionFilter sf = new SessionFilter();

        // Add Place
        String response = given()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get(
                        "C:\\Users\\purna\\Desktop\\Test_Mat\\Api Testing_Folder\\Google_api\\Google+Place+API_Post_Request.json"))))
                .filter(sf)
                .log().all()
            .when()
                .post("/maps/api/place/add/json")
            .then()
                .assertThat()
                .statusCode(200)
                .log().all()
                .extract()
                .response()
                .asString();
        System.out.println("=======00000======");

        // Extract place_id
        JsonPath js = new JsonPath(response);
        String placeId = js.get("place_id");
        System.out.println("=======" + placeId + "======");

        // Get Place
        String getPlaceResponse = given()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .header("Content-Type", "application/json")
                .filter(sf)
            .when()
                .get("/maps/api/place/get/json")
            .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();
        System.out.println(111 + "=======" + getPlaceResponse + "======");

        // Update Place
        String newAddress = "1st Hacker Street, USA";
        String updatePlaceResponse = given()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\r\n" +
                        "\"place_id\":\"" + placeId + "\",\r\n" +
                        "\"address\":\"" + newAddress + "\",\r\n" +
                        "\"key\":\"qaclick123\"\r\n" +
                        "}\r\n")
            .when()
                .put("/maps/api/place/update/json")
            .then()
            	.log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        System.out.println(updatePlaceResponse);



//         Delete Place
        given()
            .queryParam("key", "qaclick123")
            .header("Connection", "keep-alive")
            .header("Content-Type", "application/json")
            .body("{\r\n" +
                    "    \"place_id\":\"" + placeId + "\"\r\n" +
                    "}\r\n")
        .when()
            .delete("/maps/api/place/delete/json")
        .then()
            .log().all()
            .assertThat()
            .statusCode(200)
            .extract()
            .response()
            .asString();
    }
}

package com.testingapi.demo.working;

import org.testng.Assert;

import com.testingapi.demo.data.Load;

import io.restassured.path.json.JsonPath;

public class complexJson {
	public static void main(String args[]) {
		JsonPath jss = new JsonPath(Load.coursePrice());
		
		//		1.Print No of courses returned by API
		int nocourse = jss.getInt("courses.size()");
		System.out.println(nocourse);

		//		2.Print Purchase Amount
		int pamount = jss.getInt("dashboard.purchaseAmount");
		System.out.println(pamount);
		
		//		3. Print Title of the first course
		String fstcourse = jss.getString("courses[0].title");
		System.out.println(fstcourse);
		
		//		4. Print All course titles and their respective Prices
		for(int i=0;i<nocourse;i++) {
			String coursename = jss.getString("courses["+i+"].title");
			int courseprice = jss.getInt("courses["+i+"].price");
			System.out.println("The title of the course is "+ coursename+" and the price of the course is "+courseprice);
		}
		
//		5. Print no of copies sold by RPA Course
		int numcopy = jss.getInt("courses[2].copies");
		System.out.println(numcopy);

//		6. Verify if Sum of all Course prices matches with Purchase Amount
		int sum=0;
		for(int i=0;i<nocourse;i++) {
			int price = jss.getInt("courses["+i+"].price");
			int totalprice = price*jss.getInt("courses["+i+"].copies");
			sum =sum+totalprice;
		}
		Assert.assertEquals(sum, pamount,"HIII");
		System.out.println("Total "+sum+" amout is same as total purchase amount "+pamount);
	}
}

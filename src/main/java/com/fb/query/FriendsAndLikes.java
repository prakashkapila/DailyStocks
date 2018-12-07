package com.fb.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class FriendsAndLikes {
	String urlstr="https://graph.facebook.com/me";
	String query="fields=id,name,friends.limit(25).fields(likes.limit(25)";
	String token = "access_token=";
	
	String oatoken="\"EAACEdEose0cBAKIIqCcc81snHMFZAadwEsrt7YHOLu8yZAZBYipqq3FT6tYLx1XZA80DiE3kv10xUfoXe9kyuB4SUIcpncsAmYHxp7YuFYSQePOBba1BNZBXfFHQWlAcdYwVZBRxs3zgAbInirZABkNut6SkFNm1JPl0v1hIBSWtjPL2pvyqTH1sEGsjgaLB0qZCV2JIWFm1cgZDZD\" \\";
			private String buildLikesUrl() {
		String ret = urlstr+"?"+query+"&"+token+oatoken;
		System.out.println("url is ="+ret);
		return ret;
	}
	public void getLikes() {
		try {
	URL url = new URL(buildLikesUrl());
 	HttpURLConnection conn =(HttpURLConnection)  url.openConnection();
	conn.setRequestMethod("GET");
	conn.addRequestProperty("fields", "id,name,friends.limit(25).fields(likes.limit(25)");
	conn.addRequestProperty("accessToken",oatoken);
	System.out.println(conn.getURL().toExternalForm());
	//conn.setRequestProperty(key, value);
	conn.connect();
	StringBuilder webpageDetails = new StringBuilder();
	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	do {
		webpageDetails.append(reader.readLine());
		webpageDetails.append("\n");
	} while (reader.ready());
	printWebPage(webpageDetails.toString());

		}catch(Exception esp )
		{
			System.out.println(esp.getMessage());
		}
 	}
	private void printWebPage(String webpageDetails) {
		System.out.println(webpageDetails);
	}

	public static void main(String arg[]) throws IOException {
		FriendsAndLikes likes = new FriendsAndLikes();
		likes.getLikes();
	}
}

package com.example.wifil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

@SuppressWarnings("deprecation")
public class ServerCommunication {

	/*
	 * Input: file name on the server side
	 * 
	 * This process will form a dynamic post request given the file name and url
	 * variables. The post will then be submitted
	 */
	public static String serverPost(String urlFile, String[] variables, String[] values) {

		try {

			// The url to send the post to
			String url = "http://wifil.bkingmedia.com/api/";

			// Append the file name to the base url destination
			url += urlFile;

			// The client will be the one sending the post request to the above destination
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			// Add header
			post.setHeader("WIFIL", "Mozilla/5.0");

			// Add url parameters to the post request
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			for (int i = 0; i < variables.length; i++) {
				urlParameters.add(new BasicNameValuePair(variables[i], values[i]));
			}
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			// Print information about the post and its status
			HttpResponse response = client.execute(post);
			//System.out.println("\nSending 'POST' request to URL : " + url);
			//System.out.println("Post parameters : " + post.getEntity());
			//System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();

			//System.out.println(result.toString());
			return result.toString();

		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean authenticate( String guid ) {

		// Build the input parameters for the post
		String[] authenticate_variable = { "guid" };
		String[] authenticate_value    = { guid };

		// Call post method
		String response = serverPost("authenticate.php", authenticate_variable, authenticate_value);

		// If post method succeeded and the authentication value is good, return true 
		// Needs to be changed once server side authentication is working
		if (response.equals("5678"))
			return true;
		else
			return false;

	}

	public static boolean deauth( String guid , String secret ) {

		// Build the input parameters for the post
		String[] deauth_variable = { "guid" , "secret" };
		String[] deauth_value    = {  guid   , secret  };

		// Call post method
		String response = serverPost("deauth.php", deauth_variable, deauth_value);

		// If post method succeeded and the authentication value is good, return true 
		if (response.equals("1"))
			return true;
		else 
			return false;

	}

	public static Hotspot[] getHotspots( String guid , String secret, String lat, String lon, String r ) {

		// Build the input parameters for the post
		String[] gethotspots_variable = { "guid" , "secret" , "lat" , "lon" , "r" };
		String[] gethotspots_value    = {  guid  ,  secret  ,  lat   , lon	 ,  r  };

		try {
			
			// Call post method
			String response = serverPost("gethotspots.php", gethotspots_variable, gethotspots_value);

			// Parse json file
			JSONArray hotspotArray = null;

			hotspotArray = (JSONArray) new JSONTokener(response).nextValue();

			// Create an array of hotspot objects
			Hotspot[] hotspots = new Hotspot[hotspotArray.length()];
			for (int i=0; i<hotspotArray.length(); i++) {
				JSONObject hotspot = hotspotArray.getJSONObject(i);
				hotspots[i] = new Hotspot(hotspot.getString("SSID"), hotspot.getString("MAC"), hotspot.getDouble("lat"), hotspot.getDouble("lon"), hotspot.getDouble("radius"), hotspot.getInt("isPublic"));
			}
			return hotspots;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		// Return the array of hotspots on success or null otherwise				
	}

	public static Boolean submitData( String guid , String secret, String data ) {
		
		// Build the input parameters for the post
		String[] submitdate_variable = { "guid" , "secret" , "data" };
		String[] submitdate_value    = {  guid  ,  secret  ,  data  };
		
		// Call post method, will echo 1 if the submit is bad and will respond give the error to the response String
		String response = serverPost("submitdata.php", submitdate_variable, submitdate_value);		
		
		// If post method succeeded and the authentication value is good, return true 
		if (response.equals("1"))
			return true;
		else
			return false;
		
	}
}

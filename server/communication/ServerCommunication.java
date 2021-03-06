import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

@SuppressWarnings({ "resource", "deprecation" })
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

	/*
	 * Input: UrlString, File to post, name of the file to be posetd, a description of the file to be posted
	 * 
	 * The input file is transported to the input url destination with the given filename and filedescrpition 
	 * as a POST request. an exception will be thrown if any sort of interruption occurs during the post
	 */
	public static void serverPost(String urlFile, File file, String[] variables, String[] values) {
		
		String url = "http://wifil.bkingmedia.com/api/";

		url += urlFile;
		
		HttpPost postRequest = new HttpPost(url);
		try {
			
			/*// Add url parameters to the post request
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			for (int i = 0; i < variables.length; i++) {
				urlParameters.add(new BasicNameValuePair(variables[i], values[i]));
			}
			postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));*/

			MultipartEntity multiPartEntity = new MultipartEntity();

			//test url request body
			multiPartEntity.addPart(variables[0], new StringBody(values[0]));
			multiPartEntity.addPart(variables[1], new StringBody(values[1]));
			FileBody fileBody = new FileBody(file, "application/octect-stream");
			multiPartEntity.addPart("data", fileBody);

			postRequest.setEntity(multiPartEntity);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}		
		
		String responseString = "";

		InputStream responseStream = null;
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(postRequest);
			if (response != null) {
				HttpEntity responseEntity = response.getEntity();

				if (responseEntity != null) {
					responseStream = responseEntity.getContent();
					if (responseStream != null) {
						BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
						String responseLine = br.readLine();
						String tempResponseString = "";
						while (responseLine != null) {
							tempResponseString = tempResponseString + responseLine + System.getProperty("line.separator");
							responseLine = br.readLine();
						}
						br.close();
						if (tempResponseString.length() > 0) {
							responseString = tempResponseString;
						}
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (responseStream != null) {
				try {
					responseStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		client.getConnectionManager().shutdown();

		System.out.println("\n"+responseString);
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

	public static String /*Hotspot[]*/ getHotspots( String guid , String secret, String lat, String lon, String r ) {
		
		// Build the input parameters for the post
		String[] gethotspots_variable = { "guid" , "secret" , "lat" , "lon" , "r" };
		String[] gethotspots_value    = {  guid  ,  secret  ,  lat   , lon	 ,  r  };
		
		// Call post method
		String response = serverPost("gethotspots.php", gethotspots_variable, gethotspots_value);
		
		// Parse json file
		
		//
		
		
		// If post method succeeded and the authentication value is good, return true 
		return response;
				
	}

	public static boolean submitData( String guid , String secret, File filedata ) {
		
		//convert json file to string
		
		// Build the input parameters for the post
		//String[] submitdate_variable = { "guid" , "secret" , "data" };
		//String[] submitdate_value    = {  guid  ,  secret  ,  data  };
		
		// Call post method
		//String response = serverPost("submitdata.php", submitdate_variable, submitdate_value);		
		
		// If post method succeeded and the authentication value is good, return true 
		return false;
				
	}
	
	
	public static void main(String[] args) {
		
        // Test Cases
		
		// Boolean authenticate( String guid )
		if (authenticate("1234"))
			System.out.println("PASS: authenticate");
		
		// Boolean deauth( String guid , String secret )
		if (deauth("1234", "5678"))
			System.out.println("PASS: deauth");		
		
		// Hotspot[] getHotspots( String guid , String secret, String lat, String lon, String r ) 
		System.out.println(getHotspots("1234", "5678", "40.42", "-86.92", "1"));
		
		// Boolean submitData( String guid , String secret, String data )
		File testFile = new File ("C:/Users/Administrator/Documents/WorkPlace/ServerCommunications/cache/jsonfile.json") ;
		if (submitData("1234", "5678", testFile))
			System.out.println("PASS: submitData");
		else
			System.out.println("FAIL: submitData");
	   
	}
	
}

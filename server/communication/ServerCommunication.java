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
	public static void serverPost(String file, String[] variables, String[] values) {
		
		try {
			
			// The url to send the post to
			String url = "http://wifil.bkingmedia.com/api/";
	
			// Append the file name to the base url destination
			url += file;
	
			// The client will be the one sending the post request to the above
			// destination
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
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + post.getEntity());
			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());
	
			BufferedReader rd = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
	
			System.out.println(result.toString());
			
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/*
	 * Input: UrlString, File to post, name of the file to be posetd, a description of the file to be posted
	 * 
	 * The input file is transported to the input url destination with the given filename and filedescrpition 
	 * as a POST request. an exception will be thrown if any sort of interruption occurs during the post
	 */
	public static void serverPostFile(String urlString, File file, String fileName, String fileDescription , String[] variables, String[] values) {
		
		HttpPost postRequest = new HttpPost(urlString);
		try {
			
			// Add url parameters to the post request
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			for (int i = 0; i < variables.length; i++) {
				urlParameters.add(new BasicNameValuePair(variables[i], values[i]));
			}
			postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));

			MultipartEntity multiPartEntity = new MultipartEntity();

			// The usual form parameters can be added this way
			multiPartEntity.addPart("fileDescription", new StringBody(fileDescription != null ? fileDescription : ""));
			multiPartEntity.addPart("fileName", new StringBody(fileName != null ? fileName : file.getName()));

			/*
			 * Need to construct a FileBody with the file that needs to be
			 * attached and specify the type of the file. Add the fileBody
			 * to the request as an another part. This part will be considered
			 * as file part and the rest of them as usual form-data parts
			 */
			FileBody fileBody = new FileBody(file, "application/octect-stream");
			multiPartEntity.addPart("attachment", fileBody);

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

	public static void main(String[] args) {


		// authenticate.php :: returns a google key given a guid
		String[] test_authenticate_variable = { "guidd" , "secret" };
		String[] test_authenticate_value    = { "1234"  , "5678" };
		serverPost("authenticate.php", test_authenticate_variable, test_authenticate_value);
		
		// deauth.php :: returns 0 if successful
		String[] test_deauth_variable = { "guid" , "secret" };
		String[] test_deauth_value    = { "1234" , "5678"   };
		serverPost("deauth.php", test_deauth_variable, test_deauth_value);
		
		// gethotspots.php :: returns a list of hotspots within a specific radius
		String[] test_gethotspots_variable = { "guid" , "secret" , "lat" , "lon" , "r" };
		String[] test_gethotspots_value    = { "1234" , "5678"   , "40.42"   , "-86.92"	  , "1" };
		serverPost("gethotspots.php", test_gethotspots_variable, test_gethotspots_value);
				
		// submitdata.php :: uploads the file given to a specific guide and secret, returns the number of modifications/changes
		String[] test_submitdata_variable = { "guid" , "secret" };
		String[] test_submitdata_value    = { "1234" , "5678" };
		File testFile = new File ("C:/Users/Administrator/Documents/WorkPlace/ServerCommunications/cache/testfile.txt") ;
		serverPostFile("http://wifil.bkingmedia.com/api/testrequest.php", testFile, testFile.getName(), "File Upload test testfile.txt description", test_submitdata_variable, test_submitdata_value);

		
	}

}

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class ServerCommunication {

	private static final String USER_AGENT = "Mozilla/5.0";
	
	/*
	 * Input: file name on the server side
	 * 
	 * This process will form a dynamic post request given the file name and url variables.
	 * The post will then be submitted
	 * 
	 */
	public static void serverPost(String file, String[] variables, String[] values) throws Exception
	{
		// The url to send the post to
		String url = "http://wifil.bkingmedia.com/api/";
		
		// Append the file name to the base url destination
		url += file;
		
		// The client will be the one sending the post request to the above destination
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
 
		// Add header
		post.setHeader("WIFIL", USER_AGENT);
		
		// Add url parameters to the post request
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		for (int i = 0; i < variables.length; i++)
		{
			urlParameters.add(new BasicNameValuePair(variables[i], values[i]));
		}
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		// Print information about the post and its status
		HttpResponse response = client.execute(post);
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		
		System.out.println(result.toString());
	}
	
	
	
	public static void main(String [ ] args)
	{	
		String[] test_variables = {"var1", "var2"};
		String[] test_values = {"val1", "val2"};
		
		// start demonstration
		try {
			serverPost("testrequest.php", test_variables, test_values);
			serverPost("authenticate.php", test_variables, test_values);
			serverPost("deauth.php", test_variables, test_values);

		} catch (Exception e)
		{
			//Catch Block
		}
	}
	
}

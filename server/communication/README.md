serverCommunication
====================

This class will be the primary communication source between the wifil android application and the wifil servers.

The responsibilities include:

1) posting files to the server

2) posting requests for server information

3) requesting data from the servers


API
-------------------

1) authenticate will return true if the guid given as a parameter is within the SQL database.
Test with guid = 1234
	
	boolean authenticate( String guid )

2) death will call to the server for de-authorization of specific a guid given the secret string.
Test with guid = 1234 , secret = 5678
	boolean deauth( String guid , String secret )
	
	
3) getHotspots will return an array of hotspots given the specific latitude, longitude, and radios. an authentication is required with guid and secret.
Test with guid = 1234 , secret = 5678, lat = 40.42 , lon = -86.92 , r = 1
CURRENTLY ONLY RETURN JSON FILE STRING
	
	Hotspot[] getHotspots( String guid , String secret, String lat, String lon, String r )

4) submitData will take a given json file and post it to the server. This requires a guid and secret
Test with 1234, 5678, and any json file
CURRENTLY ONLY RETUNS NULL AND WILL NOT SUBMIT ANYTHING TO THE SERVERS
	public static boolean submitData( String guid , String secret, File filedata )
	

5) serverPostFile will POST to a given urlString with the given file object. 
The name and description will need to be included for the POST request.

    serverPostFile(String urlString, File file, String fileName, String fileDescription) 

6) serverPost will post to the given url with the given variables and values as perimeters.

    serverPost(String url, String[] variables, String[] values) 


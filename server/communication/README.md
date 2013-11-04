ServerCommunication
====================

This class will be the primary commincation source between the wifil android application and the wifil servers.

The responsabilitys include:

1) posting files to the server

2) posting requests for server information

3) requesting data from the servers


API
-------------------

1) serverPostFile will POST to a given urlString with the given file object. 
The name and description will need to be included for the POST request.

    serverPostFile(String urlString, File file, String fileName, String fileDescription) 

2) serverPost will post to the given url with the given variables and values as perameters.

    serverPost(String url, String[] variables, String[] values) 

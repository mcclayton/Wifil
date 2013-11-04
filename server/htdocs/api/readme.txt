authenticate.php [guid] [other google stuff]
-returns secret key

submitdata.php [guid] [secret] [data (file upload)]
-returns # of entries added/modified

gethotspots.php [guid] [secret] [lat] [lon] [r]
-returns hotspots within radius (in mi) of lat and lon

deauth.php [guid] [secret]
-returns 0 if successful. use guid=1234&secret=5678 for testing
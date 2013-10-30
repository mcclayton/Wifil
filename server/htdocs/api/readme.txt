authenticate.php [guid] [other google stuff]
e.g. authenticate.php?guid=asdf&whatever=cool
-returns secret key

submitdata.php?guid=<guid>&data=<json>
e.g. submitdata.php?guid=0291384&data=[{"id":3,"SSID":"SuperHotspot3Million","MAC":"8r:4n:d4:nm:il:lr","lat":"123.45","lon":"67.890","radius":"20","meta":"asdfasdfasdf"}]
-returns # of entries added/modified

gethotspots.php?lat=<latitude>&lon=<longitude>&r=<radius>
e.g. gethotspots.php?lat=213.4&lon=111.1&r=10
-returns hotspots within radius (in mi) of lat and lon

deauth.php?guid=<guid>&secret=<secret>
-returns 0 if successful
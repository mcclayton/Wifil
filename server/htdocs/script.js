var map;
var markerlist = [];
var circlelist = [];
var hslist;
var infowindow;

function initialize() {
	var mapOptions = {
		center: new google.maps.LatLng(40.4254, -86.9194),
		zoom: 14
	};
	map = new google.maps.Map(document.getElementById("map-canvas"),
		mapOptions);
	
	infowindow = new google.maps.InfoWindow();
	
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var initialLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
			map.setCenter(initialLocation);
		});
	}
	
	updateHotspots();
}

Array.prototype.clear = function() {
	while(this.length>0) {
		this.pop();
	}
};

function buildInfo(hotspot) {
	var str = "";
	str += "<div id=\"map-hotspot-title\">"+hotspot.SSID+"</div>";
	str += "<div class=\"map-info\"><span class=\"map-info-heading\">MAC: </span>"+hotspot.MAC+"</div>";
	str += "<div class=\"map-info\"><span class=\"map-info-heading\">RADIUS: </span>"+hotspot.radius + " meters</div>";
	str += "<div class=\"map-info\"><span class=\"map-info-heading\">META: </span>"+hotspot.meta+"</div>";
	return str;
}

function updateHotspots() {
	for(var i = 0; i<markerlist.length; i++) {
		markerlist[i].setMap(null);
		circlelist[i].setMap(null);
	}
	markerlist.clear;
	circlelist.clear;
	var request = new XMLHttpRequest();
	request.open("POST","api/gethotspots.php",true);
	request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	var center = map.getCenter();
	var requestVars="guid=1234&secret=5678&lat="+center.ob+"&lon="+center.pb+"&r=5";
	request.send(requestVars);
	request.onreadystatechange = function() {
		if(request.readyState == 4 && request.status == 200) {
			hslist = JSON.parse(request.responseText);
			for(var i=0;i<hslist.length;i++) {
				var hotspot = hslist[i];
				var center = new google.maps.LatLng(hotspot.lat,hotspot.lon);
				var circle = new google.maps.Circle({
					strokeColor: '#0000ff',
					strokeOpacity: 0.8,
					strokeWeight: 2,
					fillColor: "#0000ff",
					fillOpacity: 0.35,
					map: map,
					center: center,
					radius: parseInt(hotspot.radius)
				});
				var marker = new google.maps.Marker({
					position: center,
					map: map,
					title: hotspot.SSID
				});
				markerlist.push(marker);
				circlelist.push(circle);
				google.maps.event.addListener(marker, 'click', function() {
					var idx = markerlist.indexOf(this);
					infowindow.setContent(buildInfo(hslist[idx]));
					infowindow.open(map,this);
				});
			}
		}
	};
}


google.maps.event.addDomListener(window, 'load', initialize);

<html>

<head>
    <title>Leaflet Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css">

    <script type="application/javascript"
            src="https://unpkg.com/leaflet-better-filelayer@0.1.1/dist/leaflet.betterfilelayer.min.js"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://unpkg.com/leaflet-better-filelayer@0.1.1/dist/leaflet.betterfilelayer.css"
          crossorigin="anonymous" referrerpolicy="no-referrer">



    <style>
			body {

				margin: 0;
				display: flex;
			}
        #map {
            height: 100vh;
 		      width: 90vw;
        }

			#charts {
            height: 100vh;
 		      width: 10vw;
			
			}
    </style>
</head>


<body>
 <div id="map"/>
  <div id="charts"/>



  <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
  <script>
        var coordinates = [49.6646852, 21.1573716]
        var map = L.map('map').setView(coordinates, 13);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(map);

        var marker = L.marker(coordinates).addTo(map);
        marker.bindPopup("You're here").openPopup();
        
        
        // user_id = 2353
        user_id = 2364  // place: 216
        var geoJsonUrl = `https://org.maziarz.org/api/participants/${user_id}/geojson`;

    var geojsonMarkerOptions = {
    radius: 4,
    fillColor: "#ff7800",
    color: "#000",
    weight: 1,
    opacity: 1,
    fillOpacity: 0.6
};


    fetch(geoJsonUrl)
        .then(response => response.json())
        .then(data => {
            L.geoJSON(data, {
                pointToLayer: function (feature, latlng) {
                     return L.circleMarker(latlng, geojsonMarkerOptions);
                }
            }).addTo(map);
        });

    fetch('https://org.maziarz.org/api/objects/1/geojson')
        .then(response => response.json())
        .then(data => {
            L.geoJSON(data).addTo(map);
        });

//		map.on('click', function(e) {
  //      console.log(e);
    //    var marker = L.marker([e.latlng.lat, e.latlng.lng]).addTo(map);
//});

    </script>

</body>


</html>

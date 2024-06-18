<template>
<div id="map"></div>
</template>


 <script setup lang="ts">

import leaflet from "leaflet";
import {onMounted, watchEffect } from "vue";

import {useGeolocation } from "@vueuse/core";


let map: leaflet.Map;
let userGeoMarker: leaflet.Marker;
const {coords, locatedAt, error, resume, pause } = useGeolocation();

import { userMarker, nearbyMarkers } from "@/stores/mapStore";




onMounted(() => {
  map = leaflet.map("map").setView([userMarker.value.latitude, userMarker.value.longitude], 13);

   
 leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
			            attribution: '© OpenStreetMap contributors'
							        }).addTo(map);


});


watchEffect(() => {
if (
    coords.value.latitude !== Number.POSITIVE_INFINITY &&
	     coords.value.longitude !== Number.POSITIVE_INFINITY
		    ) {
userMarker.value.latitude = coords.value.latitude;
userMarker.value.longitude = coords.value.longitude;


 if (userGeoMarker) {
			      map.removeLayer(userGeoMarker);
					    }
userGeoMarker = leaflet
      .marker([userMarker.value.latitude, userMarker.value.longitude])
		      .addTo(map)
				      .bindPopup("User Marker")

map.setView([userMarker.value.latitude, userMarker.value.longitude], 13);


const el = userGeoMarker.getElement();
    if (el) {
				      el.style.filter = "hue-rotate(120deg)";
						    }

				  }



});



</script>

<style scoped>
#map {
		    width: 100vw;
			 height: 100vh;
	 }
</style>

<template>
    <div id="map"></div>
</template>

<script setup lang="ts">
import leaflet from "leaflet";
import { onMounted, watchEffect } from "vue";

import { useGeolocation } from "@vueuse/core";

let map: leaflet.Map;
let userGeoMarker: leaflet.Marker;
const { coords, locatedAt, error, resume, pause } = useGeolocation();

import { userMarker, nearbyMarkers } from "@/stores/mapStore";

onMounted(() => {
    map = leaflet
        .map("map")
        .setView([userMarker.value.latitude, userMarker.value.longitude], 13)
        .setZoom(12);

    leaflet
        .tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            maxZoom: 25,
            attribution: "© OpenStreetMap SScontributors",
        })
        .addTo(map);

    // map.addEventListener("click", (e) => {
    //     const { lat: latitude, lng: longitude } = e.latlng;
    //     leaflet
    //         .marker([latitude, longitude])
    //         .addTo(map)
    //         .bindPopup(
    //             `Marker at (<strong>${latitude.toFixed(2)},${longitude.toFixed(2)}</strong>)`,
    //         );

    //     nearbyMarkers.value.push({ latitude, longitude });
    // });

    // nearbyMarkers.value.forEach(({ latitude, longitude }) => {
    //     leaflet
    //         .marker([latitude, longitude])
    //         .addTo(map)
    //         .bindPopup(
    //             `Marker at (<strong>${latitude.toFixed(2)},${longitude.toFixed(2)}</strong>)`,
    //         );
    // });

    fetch("https://org.maziarz.org/api/objects/1/geojson")
        .then((response) => response.json())
        .then((data) => {
            leaflet.geoJSON(data).addTo(map);
        });

    fetch("https://org.maziarz.org/api/participants/2422/geojson_points")
        .then((response) => response.json())
        .then((data) => {
            leaflet
                .geoJSON(data, {
                    pointToLayer: function (feature, latlng) {
                        var color = "red"; // Default color if not specified
                        var distance = feature.properties.distance;
                        return L.circleMarker(latlng, {
                            radius: 6,
                            fillColor: color,
                            color: "#000",
                            weight: 1,
                            opacity: 1,
                            fillOpacity: 0.8,
                        }).bindPopup(`Distance: <strong>${distance}</strong>`);
                    },
                })
                .addTo(map);
        });

    // fetch("https://org.maziarz.org/api/participants/2422/geojson_route")
    //     .then((response) => response.json())
    //     .then((data) => {
    //         leaflet
    //             .geoJSON(data, {
    //                 pointToLayer: function (feature, latlng) {
    //                     var color = feature.properties.color || "red"; // Default color if not specified
    //                     return L.circleMarker(latlng, {
    //                         radius: 3,
    //                         fillColor: color,
    //                         color: "#000",
    //                         weight: 1,
    //                         opacity: 1,
    //                         fillOpacity: 0.8,
    //                     });
    //                 },
    //             })
    //             .addTo(map);
    //     });

    fetch("https://org.maziarz.org/api/participants/2422/geojson")
        .then((response) => response.json())
        .then((data) => {
            leaflet.geoJSON(data).addTo(map);
        });
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
            .bindPopup("User Marker");

        map.setView(
            [userMarker.value.latitude, userMarker.value.longitude],
            13,
        );

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

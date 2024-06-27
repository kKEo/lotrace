<template>
    <div id="map"></div>
</template>

<script setup lang="ts">
import leaflet from "leaflet";
import { onMounted, watchEffect, watch } from "vue";

import { useGeolocation } from "@vueuse/core";

let map: leaflet.Map;
let userGeoMarker: leaflet.Marker;
let geoJsonLayerGroup: leaflet.LayerGroup;
let geoJsonStatsLayerGroup: leaflet.LayerGroup;
const { coords, locatedAt, error, resume, pause } = useGeolocation();

import {
    userMarker,
    nearbyMarkers,
    visibleArea,
    chosenParticipant,
    locationsChecked,
    statsChecked,
    userPoints,
    userPointsKeys,
    routePoints,
    userLocations,
    userLocationsKeys,
    showYourLocation,
} from "@/stores/mapStore";

function moveToCurrentLocation() {
    if (!showYourLocation.value) {
        console.log(
            "Show your location: ",
            showYourLocation.value,
            ". Coords: ",
            coords.value,
        );
        return;
    }

    if (
        coords.value.latitude !== Number.POSITIVE_INFINITY &&
        coords.value.longitude !== Number.POSITIVE_INFINITY
    ) {
        userMarker.value.latitude = coords.value.latitude;
        userMarker.value.longitude = coords.value.longitude;
        console.log(userMarker);

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
}

function calculateDistance(polyline) {
    var latlngs = polyline.getLatLngs();
    var distance = 0;
    for (var i = 0; i < latlngs.length - 1; i++) {
        distance += latlngs[i].distanceTo(latlngs[i + 1]);
    }
    return distance;
}

onMounted(() => {
    userMarker.value.latitude = 49.99825021043669;
    userMarker.value.longitude = 19.89727020263672;

    map = leaflet
        .map("map")
        .setView([userMarker.value.latitude, userMarker.value.longitude], 13)
        .setZoom(12);

    leaflet
        .tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            maxZoom: 25,
            attribution: "© OpenStreetMap",
        })
        .addTo(map);

    fetch("https://org.maziarz.org/api/objects/1/geojson")
        .then((response) => response.json())
        .then((data) => {
            leaflet.geoJSON(data).addTo(map);
        });

    moveToCurrentLocation();

    function filterPointsWithinBounds(geojson) {
        var sw = L.latLng(visibleArea.value.swlat, visibleArea.value.swlng);
        var ne = L.latLng(visibleArea.value.nelat, visibleArea.value.nelng);
        var bounds = L.latLngBounds(sw, ne);

        var filteredFeatures = geojson.features.filter(function (feature) {
            var coords = feature.geometry.coordinates;
            var latLng = L.latLng(coords[1], coords[0]);
            return bounds.contains(latLng);
        });

        return {
            type: "FeatureCollection",
            features: filteredFeatures,
        };
    }

    function loadUserStats(userId) {
        function addToMap(data) {
            var filteredGeojsonData = filterPointsWithinBounds(data);
            // console.log(filteredGeojsonData);

            if (geoJsonStatsLayerGroup) {
                map.removeLayer(geoJsonStatsLayerGroup);
            }
            geoJsonStatsLayerGroup = L.layerGroup();
            L.geoJSON(filteredGeojsonData);
            leaflet
                .geoJSON(filteredGeojsonData, {
                    pointToLayer: function (feature, latlng) {
                        var position_ts = feature.properties.position_ts;
                        var color =
                            position_ts !== undefined
                                ? new Date(
                                      position_ts.replace(" ", "T"),
                                  ).getHours() > 23 ||
                                  new Date(
                                      position_ts.replace(" ", "T"),
                                  ).getHours() < 5
                                    ? "darkred"
                                    : "yellow"
                                : "green";
                        var distance = feature.properties.distance;
                        var prev_dist =
                            Math.round(
                                100 *
                                    (feature.properties.distance -
                                        feature.properties.previous_dist),
                            ) / 100;
                        var velocity = feature.properties.avg_velocity;
                        var elevation = feature.properties.elevation;

                        return L.circle(latlng, {
                            radius: Math.floor(
                                1000 /
                                    (2 +
                                        100 /
                                            (Math.floor(velocity / 4) *
                                                Math.floor(velocity / 4))),
                            ),
                            fillColor: color,
                            color: color,
                            weight: 1,
                            opacity: 1,
                            fillOpacity: 0.6,
                        }).bindPopup(
                            `<ul>
                    <li>Distance: <strong>${distance}</strong></li>
                      <li>Dist from last point: <strong>${prev_dist}</strong></li>
                      <li>Avg Velocity: <strong>${velocity}</strong></li>
                    <li>Time: <strong>${position_ts}</strong></li>
                  </ul>
                  `,
                        );
                    },
                })
                .addTo(geoJsonStatsLayerGroup);
            geoJsonStatsLayerGroup.addTo(map);
        }

        if (statsChecked.value) {
            if (userPoints.value[userId] === undefined) {
                fetch(
                    `https://org.maziarz.org/api/participants/${userId}/geojson_points`,
                )
                    .then((response) => response.json())
                    .then((data) => {
                        userPoints.value[userId] = JSON.stringify(data);
                        userPointsKeys.value.push(userId);
                        console.log("Added to userPoints: ", userId);
                        addToMap(JSON.parse(userPoints.value[userId]));

                        if (userPointsKeys.value.length > 7) {
                            var toRemove = userPointsKeys.value.shift();
                            userPoints.value[toRemove] = undefined;
                            console.log("User removed from cache: ", toRemove);
                        }
                    });
            } else {
                console.log("Use preloaded: ", userId);
                addToMap(JSON.parse(userPoints.value[userId]));
            }
        } else {
            if (geoJsonStatsLayerGroup) {
                map.removeLayer(geoJsonStatsLayerGroup);
            }
        }
    }

    function loadUserPositions(userId) {
        function addToMap(data) {
            var filteredGeojsonData = filterPointsWithinBounds(data);
            if (geoJsonLayerGroup) {
                map.removeLayer(geoJsonLayerGroup);
            }
            geoJsonLayerGroup = L.layerGroup();
            // let geoJsonLayer =
            L.geoJSON(filteredGeojsonData, {
                pointToLayer: function (feature, latlng) {
                    return L.marker(latlng, {
                        weight: 1,
                        opacity: 1,
                        fillOpacity: 0.3,
                    });
                },
            }).addTo(geoJsonLayerGroup);
            geoJsonLayerGroup.addTo(map);
        }

        if (locationsChecked.value) {
            if (userLocations.value[userId] === undefined) {
                fetch(
                    `https://org.maziarz.org/api/participants/${userId}/geojson`,
                )
                    .then((response) => response.json())
                    .then((data) => {
                        console.log("Fetch user position: ", userId);
                        userLocations.value[userId] = JSON.stringify(data);
                        addToMap(JSON.parse(userLocations.value[userId]));
                        userLocationsKeys.value.push(userId);

                        if (userLocationsKeys.value.length > 3) {
                            var toRemove = userLocationsKeys.value.shift();
                            userLocations.value[toRemove] = undefined;
                            console.log("User removed from cache: ", toRemove);
                        }
                    });
            } else {
                console.log("Load user location from cache: ", userId);
                addToMap(JSON.parse(userLocations.value[userId]));
            }
        } else {
            if (geoJsonLayerGroup) {
                map.removeLayer(geoJsonLayerGroup);
            }
        }
    }

    function showVisibleAreaCoordinates() {
        var bounds = map.getBounds();
        var southWest = bounds.getSouthWest();
        var northEast = bounds.getNorthEast();

        visibleArea.value.swlat = southWest.lat;
        visibleArea.value.swlng = southWest.lng;
        visibleArea.value.nelat = northEast.lat;
        visibleArea.value.nelng = northEast.lng;

        // console.log("Visible area: ", visibleArea.value);
        loadUserPositions(chosenParticipant.value);
        loadUserStats(chosenParticipant.value);
    }

    showVisibleAreaCoordinates();
    map.on("moveend", showVisibleAreaCoordinates);
    map.on("zoomend", showVisibleAreaCoordinates);

    watch(chosenParticipant, (newValue) => {
        showVisibleAreaCoordinates();
    });
    watch(statsChecked, (value) => {
        loadUserStats(chosenParticipant.value);
    });
    watch(locationsChecked, (value) => {
        loadUserPositions(chosenParticipant.value);
    });

    watchEffect(() => {
        moveToCurrentLocation();
    });

    // var polylineMeasure = leaflet.control
    //     .polylineMeasure({
    //         position: "topright",
    //         unit: "metres",
    //         showBearings: false,
    //         clearMeasurementsOnStop: true,
    //         showClearControl: true,
    //         showUnitControl: true,
    //     })
    //     .addTo(map);

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
});

watchEffect(() => {});
</script>

<style scoped>
#map {
    width: 100vw;
    height: 90vh;
}
</style>

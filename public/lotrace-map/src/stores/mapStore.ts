import { useLocalStorage } from "@vueuse/core";

type VisibleArea = {
  swlat: number;
  swlng: number;
  nelat: number;
  nelng: number;
};

type Marker = {
  latitude: string;
  longitude: string;
};

export const userMarker = useLocalStorage<Marker>("USER_MARKER", {
  latitude: 0,
  longitude: 0,
});

export const nearbyMarkers = useLocalStorage<Marker[]>("NEARBY_MARKERS", []);

export const visibleArea = useLocalStorage<VisibleArea>("VISIBLE_AREA", {
  swlat: undefined,
  swlng: undefined,
  nelat: undefined,
  nelng: undefined,
});

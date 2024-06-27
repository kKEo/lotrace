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

type Participant = {
  id: number;
  place: number;
  name: string;
};

export const participants = useLocalStorage<Participant[]>("PARTICIPANTS", []);

export const chosenParticipant = useLocalStorage<Number>("PARTICIPANT_ID", 0);

export const nearbyMarkers = useLocalStorage<Marker[]>("NEARBY_MARKERS", []);

export const visibleArea = useLocalStorage<VisibleArea>("VISIBLE_AREA", {
  swlat: undefined,
  swlng: undefined,
  nelat: undefined,
  nelng: undefined,
});

export const locationsChecked = useLocalStorage<Boolean>(
  "SHOW_LOCATIONS",
  false,
);
export const statsChecked = useLocalStorage<Boolean>("SHOW_STATS", true);

export const showYourLocation = useLocalStorage<Boolean>(
  "SHOW_YOUR_LOCATION",
  false,
);

export const timeScope = useLocalStorage<Number[]>("TIME_SCOPE", [90, 110]);

export const userPoints = useLocalStorage<Record<string, string>>(
  "USER_POINTS",
  {},
);

type UserKey = {
  userId: string;
};

export const userPointsKeys = useLocalStorage<string[]>("USER_POINTS_KEYS", []);

export const userLocations = useLocalStorage<Record<string, string>>(
  "USER_LOCATIONS",
  {},
);
export const userLocationsKeys = useLocalStorage<string[]>(
  "USER_LOCATIONS_KEYS",
  [],
);

export const routePoints = useLocalStorage<Record<string, string>>(
  "ROUTE_POINTS",
  {},
);

<?php

namespace App\Http\Controllers;

use Laravel\Lumen\Routing\Controller as BaseController;
use App\Models\Participant;
use App\Models\Route;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class ParticipantController extends BaseController
{
    public function index()
    {
        return response()->json(Participant::all());
    }

   public function locations($participant_id)
    {
        $participant = Participant::find($participant_id);
        if (!$participant) {
            return response()->json(['message' => 'Participant not foundi??'], 404);
        }
        $locations = $participant->locations()->select('position_ts', 'latitude', 'longitude', 'page')->get();
        return response()->json($locations);
    }

   public function geojson($participant_id) {
   
        $participant = Participant::find($participant_id);
        if (!$participant) {
            return response()->json(['message' => 'Participant not foundi??'], 404);
        }
        $locations = $participant->locations()->select('position_ts', 'latitude', 'longitude', 'page')->get();

        $geoJson = [
            'type' => 'FeatureCollection',
            'features' => [],
        ];

        foreach ($locations as $location) {
            $geoJson['features'][] = [
                'type' => 'Feature',
                'geometry' => [
                    'type' => 'Point',
                    'coordinates' => [
                        $location->longitude,
                        $location->latitude,
                    ],
                ],
                'properties' => [
                    'id' => $location->id,
                    // Add other properties as needed
                ],
            ];
        }


		 return response()->json($geoJson);
}


  public function route($id)
    {

$route = DB::table('routes')
            ->select(DB::raw('id, name, ST_AsGeoJSON(route) as geojson'))
            ->where('id', $id)
            ->first();

        if (!$route) {
            return response()->json(['message' => 'Route not found'], 404);
        }

        $geoJson = [
            'type' => 'Feature',
            'geometry' => json_decode($route->geojson),
            'properties' => [
                'name' => $route->name,
            ]
        ];


        return response()->json($geoJson);
    }



}

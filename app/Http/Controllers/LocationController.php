<?php

namespace App\Http\Controllers;

use Laravel\Lumen\Routing\Controller as BaseController;
use App\Models\Location;
use Illuminate\Http\Request;

class LocationController extends BaseController
{
    public function index($participant_id)
    {
        return response()->json(Location::all());
    }
}

<?php

/** @var \Laravel\Lumen\Routing\Router $router */

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It is a breeze. Simply tell Lumen the URIs it should respond to
| and give it the Closure to call when that URI is requested.
|
*/

$router->get('/api/version', function () use ($router) {
    return $router->app->version();
});

$router->group(['prefix' => 'api'], function () use ($router) {
    $router->get('users', 'UserController@index');

    $router->get('participants', 'ParticipantController@index');
    $router->get('participants/{participant_id}/locations', 'ParticipantController@locations');
    $router->get('participants/{participant_id}/geojson', 'ParticipantController@geojson');
    $router->get('participants/{participant_id}/geojson_points', 'ParticipantController@geojson_points');
    $router->get('participants/{participant_id}/geojson_route', 'ParticipantController@geojson_route_points');

	 $router->get('objects/{id}/geojson', 'ParticipantController@route');
   
});

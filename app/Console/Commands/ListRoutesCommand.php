<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;
use Laravel\Lumen\Routing\Router;

class ListRoutesCommand extends Command
{
    protected $signature = 'route:list';
    protected $description = 'List all registered routes';

    protected $router;

    public function __construct(Router $router)
    {
        parent::__construct();
        $this->router = $router;
    }

    public function handle()
    {
        $routes = $this->router->getRoutes();

        $routeList = [];

        foreach ($routes as $route) {
            $routeList[] = [
                'method' => implode('|', [$route['method']]),
                'uri' => $route['uri'],
                'name' => $route['action']['as'] ?? '',
                'action' => $route['action']['uses'] ?? 'Closure',
            ];
        }

        $this->table(['Method', 'URI', 'Name', 'Action'], $routeList);
    }
}

<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Participant extends Model
{
    protected $table = 'w23_participants';

    protected $fillable = [
        'firstName',
        'lastName',
        'race',
        'startingNumber',
        'startDateTime',
        'finishDateTime',
        'raceOver',
    ];

    protected $casts = [
        'startDateTime' => 'datetime',
        'finishDateTime' => 'datetime',
        'raceOver' => 'boolean',
    ];

    public function locations()
    {
        return $this->hasMany(Location::class, 'entry', 'id')->orderBy('position_ts');
    }

}

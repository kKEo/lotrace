<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Location extends Model
{
    protected $table = 'w23_points';

    protected $fillable = [
        'entry',
        'position_ts',
        'latitude',
        'longitude',
        'speed',
        'page',
    ];

    protected $casts = [
        'position_ts' => 'datetime',
        'latitude' => 'decimal:7',
        'longitude' => 'decimal:7',
        'speed' => 'decimal:2',
    ];

    public function participant()
    {
        return $this->belongsTo(Participant::class, 'id', 'entry');
    }
}


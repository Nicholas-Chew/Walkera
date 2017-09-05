<?php

namespace App\Controller;

use Illuminate\Database\Eloquent\Model;

class user_dailySteps extends Model
{
   protected $table = 'user_dailySteps';
   protected $primaryKey = 'id_user_dailySteps';
   public $timestamps = true;
   
   protected $fillable = ['steps', 'steps_needed', 'id_user'];
}

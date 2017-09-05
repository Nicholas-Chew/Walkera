<?php

namespace App\Controller;

use Illuminate\Database\Eloquent\Model;

class user_bmi extends Model
{
   protected $table = 'user_bmi';
   protected $primaryKey = 'id_user_bmi';
   public $timestamps = true;
   
   protected $fillable = ['weight', 'height', 'BMI', 'id_user'];

}

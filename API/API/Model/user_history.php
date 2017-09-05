<?php

namespace App\Controller;

use Illuminate\Database\Eloquent\Model;

class user_history extends Model
{
   protected $table = 'user_history';
   protected $primaryKey = 'id_user_history';
   public $timestamps = true;
   
   protected $fillable = ['id_user'];
}

<?php

namespace App\Controller;

use Illuminate\Database\Eloquent\Model;

class user_address extends Model
{
   protected $table = 'user_address';
   protected $primaryKey = 'id_user_address';
   public $timestamps = false;
   
   protected $fillable = ['id_user', 'address', 'name', 'postalCode'];
}

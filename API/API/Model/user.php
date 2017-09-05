<?php

namespace App\Controller;

use Illuminate\Database\Eloquent\Model;

class user extends Model
{
    protected $table = 'user';
    protected $primaryKey = 'id_user';
    public $timestamps = true;
   
    protected $fillable = ['name', 'email', 'gender', 'age'];
    protected $guarded = ['password', 'remember_token'];
   
   //Foreign Keys
    public function bmi()
    {
        return $this->hasMany('Model\user_bmi');
    }
    
    public function address()
    {
        return $this->hasMany('Model\user_address');
    }
    
    public function dailysteps()
    {
        return $this->hasMany('Model\user_dailySteps');
    }
    
    public function history()
    {
        return $this->hasMany('Model\user_history');
    }
    
    //Check for Correct token
    public function checkToken($token)
    {
        $user = user::where('remember_token',$token);
        if($user!=null)
            return $user;
    }
}

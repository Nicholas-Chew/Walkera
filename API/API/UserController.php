<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Model\user as User;

class UserController extends Controller
{
    public function register(Request $request)
	{
		$validator = Validator::make($request->all(), [
    	'email' => 'unique:user,email',
    	'password' => 'required',
		]);
		
		if($validator->fails())
		{
			$respond = array('status' => 'Error',
                            'code'=> '401',
                            'message'=>'Email has been registered.');
		}
		else
		{
			$nPass = Hash::make($password);
			
			$user = new User;
			$user->email = $request->input('email');
			$user->password = Hash::make($request->input('password'));
			generateToken($user->email);
			$user->save();
		
			$respond = array('status' => 'Success',
                            'code'=> '200',
                            'token'=> $user->remember_token);
		}
		
		return Response::json($respond, $respond['code']);
	}
	
	public function updateDetail(Request $request)
	{
		$validator = Validator::make($request->all(), [
    	'token' => 'required',
    	'gender' => 'regex:([M]|[F])',
		'age'=>'integer',
		]);
		
		if($validator->fails())
		{
			$respond = array('status' => 'Error',
                            'code'=> '401',
                            'message'=>'Input Error');
		}
		else
		{
			$user = User::checkToken($request->input('token'));
			if($user!=NULL)
			{
				$user->name = $request->input('name');
				$user->gender = $request->input('gender');
				$user->age = $request->input('age');
				$user->save();
		
				$respond = array('status' => 'Success',
                           	 'code'=> '200');
			}
			else
			{
				$respond = array('status' => 'Error',
                           	 'code'=> '404',
							'message'=>'User Not Found!Invalid Token!');
			}
		}
							
		return Response::json($respond, $respond['code']);
	}
	
	private function generateToken($email)
	{
		$user = User::where(email,$email);
		$user->remember_token = str_random(100);
		$user->save();
		return $user->remember_token;
	}
}

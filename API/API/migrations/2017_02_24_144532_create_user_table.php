<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUserTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('user', function (Blueprint $table) 
		{
            $table->increments('id_user');
            $table->string('name',128);
            $table->string('email',128)->unique();
            $table->string('password',256);
            $table->string('gender',1);
            $table->smallInteger('age')->unsigned();
            $table->rememberToken();
            $table->timestamps();      
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('user');
    }
}

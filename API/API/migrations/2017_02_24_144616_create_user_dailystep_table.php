<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUserdailystepTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        
		Schema::create('user_dailysteps', function (Blueprint $table) 
		{
            $table->increments('id_user_dailysteps');
            $table->integer('id_user')->unsigned();
            $table->integer('steps')->unsigned();
            $table->integer('steps_needed')->unsigned();
            $table->timestamps();  

            //Indexes
            $table->index('id_user');  

            //Foreign Key
            $table->foreign('id_user')
                  ->references('id_user')->on('user');  
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
         Schema::table('user_dailysteps', function (Blueprint $table) {
                      //Dropping Foreign Key Constrain
                      $table->dropForeign('user_dailysteps_id_user_foreign');
        });
        Schema::dropIfExists('user_dailysteps');
    }
}

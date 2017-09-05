<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUserBmiTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
         Schema::create('user_bmi', function (Blueprint $table) 
		{
            $table->increments('id_user_bmi');
            $table->integer('id_user')->unsigned();
            $table->double('weight',3,2)->unsigned();
            $table->double('height',3,2)->unsigned();
            $table->double('BMI',3,2)->unsigned();
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
        Schema::table('user_BMI', function (Blueprint $table) {
                      //Dropping Foreign Key Constrain
                      $table->dropForeign('user_bmi_id_user_foreign');
        });
        Schema::dropIfExists('user_bmi');
    }
}

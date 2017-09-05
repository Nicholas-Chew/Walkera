<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUserHistoryTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('user_history', function (Blueprint $table) 
		{
            $table->increments('id_user_History');
            $table->integer('id_user')->unsigned();

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
        Schema::table('user_History', function (Blueprint $table) {
                      //Dropping Foreign Key Constrain
                      $table->dropForeign('user_history_id_user_foreign');
        });
        Schema::dropIfExists('user_history');
    }
}

<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUserAddressesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
         Schema::create('user_address', function (Blueprint $table) 
		{
            $table->increments('id_user_Address');
            $table->integer('id_user')->unsigned();
            $table->string('address',512);
            $table->string('name',256);
            $table->string('postalCode',64);

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
        Schema::table('user_address', function (Blueprint $table) {
                      //Dropping Foreign Key Constrain
                      $table->dropForeign('user_address_id_user_foreign');
        });
        Schema::dropIfExists('user_addresses');
    }
}

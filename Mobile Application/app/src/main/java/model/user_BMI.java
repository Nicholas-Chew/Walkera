package model;
import java.sql.Date;

/**
 * Created by ChewZhijie on 3/5/2017.
 */

class user_BMI {
    private float weight;
    private float height;
    private float bmi;
    private Date date;

    user_BMI(float weight, float height, float bmi, Date date)
    {
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.date = date;
    }

    public float getWeight(){return this.weight;}
    public float getHeight(){return this.height;}
    public float getBmi() {return  this.bmi;}
    public Date getDate_created(){return this.date;}

    public void setWeight(float weight){this.weight = weight;}
    public void setHeight(float height){this.height = height;}
    public void setBmi(float bmi){this.bmi = bmi;}
    public void setDate_created(Date date){this.date = date;}


}

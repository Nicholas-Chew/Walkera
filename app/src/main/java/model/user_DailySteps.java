package model;

import java.sql.Date;

/**
 * Created by ChewZhijie on 3/5/2017.
 */

class user_DailySteps {
    private int steps;
    private int steps_needed;
    private Date date;

    user_DailySteps(int steps, int steps_needed, Date date)
    {
        this.steps = steps;
        this.steps_needed = steps_needed;
        this.date = date;
    }

    public int getSteps(){return this.steps;}
    public int getSteps_needed(){return this.steps_needed;}
    public Date getDate(){return  this.date;}

    public void setSteps(int steps){this.steps = steps;}
    public void setSteps_needed(int steps_needed){this.steps_needed = steps_needed;}
    public void setDate(Date date){this.date = date;}
}

package model;


import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by ChewZhijie on 3/5/2017.
 */

public class user {
    private String name;
    private String email;
    private char gender;
    private int age;
    private String token;
    private ArrayList<user_Address> address;
    private ArrayList<user_BMI> bmi;
    private ArrayList<user_DailySteps> dailyStep;
    
    user(String token)
    {
        this.token = token;
        address = new ArrayList<>();
        bmi = new ArrayList<>();
        dailyStep = new ArrayList<>();
    }
    
    public String getName(){return this.name;}
    public String getEmail() {return this.email;}
    public char getGender() {return this.gender;}
    public int getAge() {return this.age;}
    public String getToken(){return this.token;}
    public ArrayList<user_Address> getAddress(){return this.address;}
    public ArrayList<user_BMI> getBmi(){return this.bmi;}
    public ArrayList<user_DailySteps> getDailyStep(){return this.dailyStep;}
    
    public void setName(String name){this.name = name;}
    public void setEmail(String email){this.email = email;}
    public void setGender(char gender){this.gender = gender;}
    public void setAge(int age){this.age = age;}
    public void setToken(String token){this.token = token;}
    
    
    public void AddAddress(String address, String postalCode, String address_name)
    {
        this.address.add(new user_Address(address,postalCode,address_name));
    }
    
    public void deleteAddress(String address_name)
    {
        for (int i = 0; i < address.size(); i++)
        {
            if (address.get(i).getAddress_name().equals(address_name))
            {
                address.remove(i);
            }
        }
    }

    public void AddBMI(float weight, float height, float bmi, Date date)
    {
        this.bmi.add(new user_BMI(weight, height, bmi, date));
    }

    public void AddDailySteps(int steps, int steps_needed, Date date)
    {
        this.dailyStep.add(new user_DailySteps(steps,steps_needed,date));
    }
    
    
}

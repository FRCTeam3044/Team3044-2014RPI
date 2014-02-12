/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team3044.robot;
import com.team3044.network.Dashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;




/**
 *
 * @authorasdf Joey
 */
public class Components {
      //(can?), doesn't have anythign for arduino , 
 //Pickup
    public static DigitalInput UpPickupLimit = new DigitalInput(1,6);
    public static DigitalInput DownPickupLimit = new DigitalInput(1,5);
    
    
    public static Talon PickupRollers =new Talon(1,4); //talon
    public static Relay LiftingPickup=new Relay(1,3); //spike
    
    public static AnalogChannel RollerPot = new AnalogChannel (1,2);
    
 //Shooter need motors
   public static DigitalInput DownShooterLimit = new DigitalInput(1,8);
    public static DigitalInput UpShooterLimit = new DigitalInput(1,9);
    
    //public static Jaguar shootermotorleft = new Jaguar (1,5);
    //public static Jaguar shootermotorright = new Jaguar (1,6);
    public static CANJaguar shootermotorleft;
    public static CANJaguar shootermotorright;
            
    public static AnalogChannel ShooterPot = new AnalogChannel (1,1);
     
 //drive
    public static Jaguar leftdrive = new Jaguar(1,1);
    public static Jaguar rightdrive = new Jaguar(1,2);
    
    public static Encoder encoderleftdrive = new Encoder(1,1,1,2);
    public static Encoder encoderrightdrive = new Encoder(1,3,1,4);
    
 //vision
    public static Servo servCameraPickupX=new Servo(1,8);    
    public static Servo servCameraPickupY=new Servo(1,7);
    
    //public static Servo servCameraShooterX=new Servo(2,1);    
    //public static Servo servCameraShooterY=new Servo(2,2);
    
 //driver station 
    public DriverStationLCD ds=DriverStationLCD.getInstance();
    public DriverStation DS= DriverStation.getInstance();
    public Dashboard dashboard = Dashboard.getInstance();
    
    Joystick GamePaddrive=new Joystick(1);//check with xbox controllers
    Joystick GamePadshoot=new Joystick(2);
    
  //other
    public static AnalogChannel ultrasonic = new AnalogChannel(1,3); // going on arduino?
    
  //GamepadDrive
    /*public static double leftdriveY=0.0;//left yaxis
    public static double rightdriveY=0.0;
    public static boolean button1 = false;//A Buton
    public static boolean button2 = false;//b button
    public static boolean button3 = false;//X button
    public static boolean button4 = false;//Y button
    public static boolean button5 = false;//left bumper(top not trigger)
    public static boolean button6 = false;//right bumper
    */
    public static boolean rollerfoward=false;
    public static boolean rollerreverse=false;
    public static boolean rollerstop=false;
    public static boolean pickupdown=false;
    public static boolean pickuptop=false;
    public static boolean pickupmiddle=false;
    public static boolean pickupstop=false;
    public static boolean shootsinglespeed =false;
    
    public static double pickuppot;
    
    public static double leftdriveY=0.0;//left yaxis
    public static double rightdriveY=0.0;
    
    public static boolean shoot=false;
    public static boolean pass=false;
    public static boolean truss=false;
    public static boolean shooterdown = false;
    //drive- drive+shoot 2-pickup+camera servos
    //rumble motors?
     
    //shooter
    public static boolean islimitshooteruptriggerd = false;
    public static boolean islimitshooterdowntriggerd = false;
    public static double shootspeedone = .5;
    public static double shootspeedtwo =.4;
    public static double shootspeedthree = .2;
    public static double shootdownspeed = -.25;
    public static double potvalue;
    public static double shooterpotpostion;
    private boolean pickupuplimit;
    private boolean pickupdownlimit;
    private double leftencoderd;
    private double rightencoderd;
    
   public  void upDateVals(){
            //drive? adssdf
            //button vals.
    
     //change with axis?
     rollerfoward=GamePaddrive.getRawButton(6);
     rollerreverse=GamePaddrive.getRawButton(5);
     rollerstop=GamePaddrive.getRawButton(8);
     
     pickupdown=GamePaddrive.getRawButton(1);
     pickuptop=GamePaddrive.getRawButton(2);
     pickupmiddle=GamePaddrive.getRawButton(4);
     pickupstop=GamePaddrive.getRawButton(3);//talk to minh
     
     shoot=GamePadshoot.getRawButton(6);//right bumper
     pass= GamePadshoot.getRawButton(2);
     truss=GamePadshoot.getRawButton(3);
     shooterdown=GamePadshoot.getRawButton(5);//leftbumper
     shootsinglespeed =GamePadshoot.getRawButton(1);
     
     potvalue=ShooterPot.getAverageVoltage();
     UpShooterLimit.get();
     DownShooterLimit.get();
     
     pickupuplimit=DownPickupLimit.get();
     pickupdownlimit=UpPickupLimit.get();
     
     pickuppot=RollerPot.getAverageVoltage();
     
     leftencoderd=encoderleftdrive.getDistance();
     rightencoderd=encoderrightdrive.getDistance();
     
        }
   public void updatedrivevals(){
     leftdriveY = GamePaddrive.getRawAxis(2);
     rightdriveY = GamePaddrive.getRawAxis(5);
   }
   double oldPotVal = 0;
   public void test(){
       //testing limit switches
           SmartDashboard.putBoolean("Limit up shooter ", islimitshooteruptriggerd);
           SmartDashboard.putBoolean("Limit down shooter ", islimitshooterdowntriggerd);
           SmartDashboard.putBoolean("Limit pickup up ", pickupuplimit);
           SmartDashboard.putBoolean("Limit pickup down ", pickupdownlimit);
       
      //pots
           
           SmartDashboard.putNumber("Shooter pot ", potvalue);
           SmartDashboard.putBoolean("Direction", oldPotVal >=  potvalue);
           SmartDashboard.putNumber("Pickup pot ", pickuppot);
           
      //encoder
           SmartDashboard.putNumber("Leftdrive encoder distance", leftencoderd);
           SmartDashboard.putNumber("rightdrive encoder distance", rightencoderd);
           
         oldPotVal = potvalue;
           
   }
    
}

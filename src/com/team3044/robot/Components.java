/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team3044.robot;


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
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @authorasdf Joey
 */
public class Components {

    //(can?), doesn't have anythign for arduino , 
    //Pickup
    public static DigitalInput UpPickupLimit = new DigitalInput(1, 6);
    public static DigitalInput DownPickupLimit = new DigitalInput(1, 5);

    public static Talon PickupRollers = new Talon(1, 4); //talon
    public static Relay LiftingPickup = new Relay(1, 1); //spike

    //public static AnalogChannel RollerPot = new AnalogChannel(1, 2);
    //Shooter need motors
    public static DigitalInput DownShooterLimit = new DigitalInput(1, 8);
    public static DigitalInput UpShooterLimit = new DigitalInput(1, 9);

    //public static Jaguar shootermotorleft = new Jaguar (1,5);
    //public static Jaguar shootermotorright = new Jaguar (1,6);
    public static CANJaguar shootermotorleft;
    public static CANJaguar shootermotorright;
    public static CANJaguar shootermotorleft2;
    public static CANJaguar shootermotorright2;

    public static AnalogChannel ShooterPot = new AnalogChannel(1, 1);

    //drive
    public static Jaguar leftdrive;
    public static Jaguar rightdrive;

    public static Encoder encoderleftdrive = new Encoder(1, 1, 1, 2);
    public static Encoder encoderrightdrive = new Encoder(1, 3, 1, 4);

    public static boolean shooterOverride = false;

    public static Encoder shooterLeft = new Encoder(1, 13, 1, 14);
    public static Encoder shooterRight = new Encoder(1, 10, 1, 11);
    public static double gamePadDriveTriggers = 0.0;
    public static double gamePadDriveX = 0;
    public static double gamePadDriveX2 = 0;
    //vision
    public static Servo servCameraPickupX = new Servo(1, 8);
    public static Servo servCameraPickupY = new Servo(1, 7);

    //public static Servo servCameraShooterX=new Servo(2,1);    
    //public static Servo servCameraShooterY=new Servo(2,2);
    //driver station 
    public DriverStationLCD ds = DriverStationLCD.getInstance();
    public DriverStation DS = DriverStation.getInstance();


    Joystick GamePaddrive = new Joystick(1);//check with xbox controllers
    Joystick GamePadshoot = new Joystick(2);

    public static double trussspeed = .8;
    public static double passspeed = .25;

    public static AnalogChannel ultrasonic = new AnalogChannel(1, 2); // going on arduino?

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
    public static boolean pickupEmergency = false;
    
    public static boolean rollerfoward = false;
    public static boolean rollerreverse = false;
    public static boolean rollerstop = false;
    public static boolean pickupdown = false;
    public static boolean pickuptop = false;
    public static boolean pickupmiddle = false;
    public static boolean pickupstop = false;
    private static double oldUltrasonicValue = 18.0;
    public static boolean driveStraight = false;

    public static double pickuppot;

    public static double leftdriveY = 0.0;//left yaxis
    public static double rightdriveY = 0.0;

    public static boolean shoot = false;
    private static boolean oldShoot = false;
    public static boolean shootButton = false;

    public static boolean pass = false;
    private static boolean oldPass = false;
    public static boolean passButton = false;

    public static boolean truss = false;
    private static boolean oldTruss = false;
    public static boolean trussButton = false;
    
    public static boolean longdistanceshoot =false;
    private static boolean oldlongdistanceshoot = false;
    public static boolean longdistanceshootbutton = false;
    
    public static boolean shooterdown = false;
    public static boolean shooterDownButton = false;

    public static boolean shootsinglespeed = false;
    private static boolean oldShootSingleSpeed = false;
    public static boolean singleSpeedButton = false;

    private static final double voltagescale = (1024 / 5);
    
    public static double uSonicDist = 0;


    public static double ultrasonicDistance = 0.0;

    public void robotInit() {
        leftdrive = new Jaguar(1, 1);
        rightdrive = new Jaguar(1, 2);
        Components.oldUltrasonicValue = (((Components.ultrasonic.getVoltage() * voltagescale) * .03281) / .716) - (.518 / .716);
        initCanJags();
    }

    public void initCanJags() {
        try {
            Components.shootermotorleft = new CANJaguar(5, CANJaguar.ControlMode.kVoltage);
            Components.shootermotorleft.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            Components.shootermotorleft.configEncoderCodesPerRev(250);
            Components.shootermotorleft.setVoltageRampRate(0.0);
            Components.shootermotorright = new CANJaguar(3, CANJaguar.ControlMode.kVoltage);
            Components.shootermotorright.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            Components.shootermotorright.configEncoderCodesPerRev(250);
            Components.shootermotorright.setVoltageRampRate(0.0);
            Components.shootermotorleft2 = new CANJaguar(2, CANJaguar.ControlMode.kVoltage);
            Components.shootermotorleft2.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            Components.shootermotorleft2.configEncoderCodesPerRev(250);
            Components.shootermotorleft2.setVoltageRampRate(0.0);
            Components.shootermotorright2 = new CANJaguar(4, CANJaguar.ControlMode.kVoltage);
            Components.shootermotorright2.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            Components.shootermotorright2.configEncoderCodesPerRev(250);
            Components.shootermotorright2.setVoltageRampRate(0.0);
            //jag.setSpeedReference(CANJaguar.SpeedReference.kEncoder);

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void stepTeleop(){
        updateJoystickVals();
        updateDriveVals();
        updateSensorVals();
    }
    
    public void stepAuto(){
        updateSensorVals();
    }
    
    private void updateJoystickVals() {
            //drive? adssdf
        //button vals.

        //change with axis?
        pickupEmergency = GamePaddrive.getRawButton(2);
        gamePadDriveTriggers = GamePaddrive.getRawAxis(3);
        rollerfoward = GamePaddrive.getRawButton(6);
        rollerreverse = GamePaddrive.getRawButton(5);
        rollerstop = GamePaddrive.getRawButton(8);

        pickuptop = GamePaddrive.getRawButton(4);
        pickupdown = GamePaddrive.getRawButton(1);
        //pickupmiddle = GamePaddrive.getRawButton(2);
        pickupstop = GamePaddrive.getRawButton(3);//talk to minh

        driveStraight = GamePaddrive.getRawButton(3);
        
        shootButton = GamePadshoot.getRawButton(6);
        passButton = GamePadshoot.getRawButton(2);
        trussButton = GamePadshoot.getRawButton(3);
        shooterDownButton = GamePadshoot.getRawButton(5);
        singleSpeedButton = GamePadshoot.getRawButton(4);
        longdistanceshootbutton = GamePadshoot.getRawButton(1);
    }
    public static double temp = 0.0;
    
    private void updateSensorVals() {

        shoot = shootButton && oldShoot == false;//A
        pass = passButton && oldPass == false;//B
        truss = trussButton && oldTruss == false;//X
        shooterdown = shooterDownButton;//leftbumper
        longdistanceshoot = longdistanceshootbutton&& oldlongdistanceshoot==false;
        shootsinglespeed = singleSpeedButton && oldShootSingleSpeed == false;//Y

        
        temp = (((Components.ultrasonic.getVoltage() * voltagescale) * .03281) / .716) - (.518 / .716);
        
        
        uSonicDist = temp;
        
        
        oldShoot = shootButton;
        oldPass = passButton;
        oldTruss = trussButton;
        oldShootSingleSpeed = singleSpeedButton;
        oldlongdistanceshoot = longdistanceshootbutton;
        oldUltrasonicValue = uSonicDist;

    }
    
    private void updateDriveVals() {
        leftdriveY = -GamePaddrive.getRawAxis(2);
        rightdriveY = -GamePaddrive.getRawAxis(5);
    }

}
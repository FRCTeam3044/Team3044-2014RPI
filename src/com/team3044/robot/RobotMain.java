/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team3044.robot;


import com.team3044.RobotComponents.Drive;
import com.team3044.RobotComponents.TestShooter;
import com.team3044.RobotComponents.Pickup;
import com.team3044.RobotComponents.Shooter;
import com.team3044.network.NetTable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
 //TEST
public class RobotMain extends IterativeRobot {
    private Utilities utils;
    private Components components = new Components(); // if this works as static... Check net tables
    Drive drive;
    Pickup pickup;
    Shooter shooter;
    NetTable table = NetTable.getInstance();
    DriverStationLCD lcd = DriverStationLCD.getInstance();
    DriverStation ds = DriverStation.getInstance();
    TestShooter testShooter = new TestShooter();
    int teleopState = 0;
    final int PRE_OPERATOR_MOVE = 0;
    final int STANDARD_TELEOP = 1;
    
    int autoType = 0;
    int autoIndex = 0;
    
    double autoStartTime = 0;
    double time = 0;
    
    final int MOVE_THEN_SHOOT = 0;
    final int SHOOT_THEN_MOVE = 1;
        
    //Camera camera = new Camera();
    
    public Utilities getUtilities(){
        return utils;
    }
    
    public Components getComponents(){
        return components;
    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        utils = new Utilities();
        components = new Components();
        components.robotInit();
        pickup = new Pickup();
        shooter = new Shooter();
        drive = new Drive();
        
        shooter.robotInit();
        pickup.robotInit();
        drive.robotInit();
        
        pickup.teleopInit();
        drive.teleopInit();
        shooter.teleopInit();
        
    
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        shooter.teleop();
        pickup.teleop();
        if(table.getDouble("ISHOT") == 1 && ds.getMatchTime() < 5){ //<--- Change to boolean
            autoType = SHOOT_THEN_MOVE;
        }
        switch(autoType){
            case MOVE_THEN_SHOOT:
                autoMoveAndShoot();
            break;
            
            case SHOOT_THEN_MOVE:
                autoShootAndMove();
            break;
                
        }
    }
    
    public void testInit(){
        drive.teleopInit();
    }
    
    public void testPeriodic(){
      //drive.Drivemain();
      components.test();
      components.upDateVals();
      shooter.singlespeed =(ds.getAnalogIn(1)/5);
      shooter.shoot();
      SmartDashboard.putNumber("ShooterState", shooter.getshooterstate());
    }

    public void teleopInit(){

    }
    
    /**
     * This function is called periodically during operator control
     */
    int tmp = 0;
    public void teleopPeriodic() {
        /*
            switch(teleopState){
            case PRE_OPERATOR_MOVE:
            
            Components.leftdriveY = -.75;
            Components.rightdriveY = .75;
            drive.Drivemain();
            if(Math.abs(Utilities.deadband(components.GamePaddrive.getRawAxis(2), .1)) > 0
            || Math.abs(Utilities.deadband(components.GamePaddrive.getRawAxis(5), .1)) > 0){
            teleopState = STANDARD_TELEOP;
            }
            
            break;
            case STANDARD_TELEOP:{
            components.upDateVals();
            components.updatedrivevals();
            pickup.teleop();
            shooter.teleop();
            drive.Drivemain();
            
            break;
            }

       
        
    }*/
        //testShooter.teleopPeriodic();
        components.upDateVals();
        components.updatedrivevals();
        if(Components.shoot){
            if(ds.getDigitalIn(1)){
                Components.LiftingPickup.set(Relay.Value.kForward);
            }else{
                Components.LiftingPickup.set(Relay.Value.kReverse);
            }
            
            try {
                Thread.sleep((long) (ds.getAnalogIn(2) * 100));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Components.LiftingPickup.set(Relay.Value.kOff);
        }else{
            Components.LiftingPickup.set(Relay.Value.kOff);
        }
        
        Components.PickupRollers.set(ds.getAnalogIn(3) - ds.getAnalogIn(4));
        System.out.println("Shooterpot: " + Components.ShooterPot.getVoltage());
        System.out.println("Pickup up: " + Components.UpPickupLimit.get());
        System.out.println("Pickup Down" + Components.DownPickupLimit.get());
    }
    
    public void autoInit(){
        
    }
    
    public void autoMoveAndShoot(){
        switch(autoIndex){
            case 0:
                
                //5 feet wheels are 12.25 in double check
                drive.setDistanceToTravel(60, 60);
                drive.resetDistance(true);
                autoIndex += 1;
            break;
            case 1:            
                if(drive.hasTraveledSetDistance() || ds.getMatchTime() >= 4){
                    
                    drive.stop();
                    autoIndex++;
                    time = ds.getMatchTime();
                }
            break;
            case 2:
                Components.pickupdown = true;
                //assuming it takes .5 seconds from shot to scoring
                if(ds.getMatchTime() > 4.5){
                    autoIndex ++;
                    Components.pickupdown = false;
                }
            break;
            case 3:
                if(/*pickup is down*/1==1 && shooter.getshooterstate() == shooter.down){
                   Components.shoot = true;
                   autoIndex ++;
                }
            break;
            case 4:
                Components.shoot = false;
                if(shooter.getshooterstate() == shooter.stopped){
                    Components.shooterdown = true;
                    autoIndex ++;
                }
            break;
            case 5:
                Components.shooterdown = false;
            break;
            
            
        }
    }
    
    public void autoLowGoal(){
        switch(autoIndex){
            case 0:
                drive.setDistanceToTravel(-180, -180);
                drive.resetDistance(true);
                autoIndex ++;
                break;
            case 1:
                if(drive.hasTraveledSetDistance()){
                    drive.stop();
                    autoIndex++;
                }
                break;
            case 2:
                if(ds.getMatchTime() > 7){
                    autoIndex++;
                }
                break;
            case 3:
                Components.pickupdown = true;
                autoIndex ++;
                break;
            case 4:
                Components.pickupdown = false;
                if(/*Pickup is down*/ 1 == 1){
                    Components.rollerreverse = true;
                    autoIndex ++;
                }
        }
    }
    
    public void autoShootAndMove(){
        switch(autoIndex){
            case 0:
                Components.pickupdown = true;
                if(/*Pickup is down*/ 1 == 1){
                    Components.pickupdown = false;
                    autoIndex ++;
                    
                }
                if(shooter.getshooterstate() == shooter.stopped){
                    Components.shooterdown = true;
                }else if(shooter.getshooterstate() == shooter.down){
                    Components.shooterdown = false;
                }
                
            break;
            case 1:
                Components.shoot = false;
                Components.shooterdown = false;
                if(shooter.getshooterstate() == shooter.down){
                    Components.shoot = true;
                    autoIndex++;
                }
            break;
            case 2:
                Components.shoot = false;
                
                if(shooter.getshooterstate() == shooter.stopped){
                    Components.shooterdown = true;
                    autoIndex++;
                }
                
            break;
            case 3:
                drive.setDistanceToTravel(60, 60);
                drive.resetDistance(true);
                if(ds.getMatchTime() >= 4.5){
                    autoIndex++;
                }
            break;
            case 4:
                if(drive.hasTraveledSetDistance()){
                    drive.stop();
                    autoIndex ++;
                }
        }
    }
    
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.team3044.robot;

import com.team3044.RobotComponents.Drive;
import com.team3044.RobotComponents.Pickup;
import com.team3044.RobotComponents.Shooter;

import com.team3044.network.NetTable;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
/*
 --------------------------------------
 Allocated Analog inputs on ds:
 --------------------------------------
 Analog 1: SingleSpeed speed
 Analog 2: Top shooter pot value
 --------------------------------------
 Digital Inputs:
 --------------------------------------
 None

 */
public class RobotMain extends IterativeRobot {

    private Utilities utils;
    private Components components = new Components(); // if this works as static... Check net tables
    Drive drive;
    Pickup pickup;
    Shooter shooter;
    NetTable table = NetTable.getInstance();
    DriverStationLCD lcd = DriverStationLCD.getInstance();
    DriverStation ds = DriverStation.getInstance();

    boolean inShootingRange = false;
    double calculatedShootVoltage = 0.0;
    double calculatedShootDistance = 0.0;
    double calculatedShootAngle = 0.0;


    final int PRE_OPERATOR_MOVE = 0;
    final int STANDARD_TELEOP = 1;
    int teleopState = STANDARD_TELEOP;

    int autoType = this.SHOOT_THEN_MOVE;
    int autoIndex = 0;

    double autoStartTime = 0;
    double time = 0;
    double autoCounter = 0;
    double teleopTime = 0;
    double oldTeleopTime = 0;

    final int MOVE_THEN_SHOOT = 0;
    final int SHOOT_THEN_MOVE = 1;

    //Camera camera = new Camera();
    public Utilities getUtilities() {
        return utils;
    }

    public Components getComponents() {
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

    }

    public void testPeriodic() {
        drive.DriveAuto();

        if (drive.hasTraveledSetDistance()) {
            drive.stop();

        }
    }

    public void autonomousInit() {
        drive.teleopInit();
        drive.autoInit();
        pickup.teleopInit();
        shooter.teleopInit();
        autoCounter = 0;
        autoIndex = 0;
        autoType = -1;

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        shooter.teleop();
        pickup.teleop();
        drive.DriveAuto();
        components.updateSensorVals();
        //this.autoMoveShootUltrasonic();
        dashboardUpdate();
        autoCounter++;
        SmartDashboard.putNumber("Auto Counter", autoCounter);
        
        this.autoMoveShootUltrasonic();

        
    }

    public void testInit() {
        drive.setDistanceToTravel(180, 180, .25);
        drive.startdriving(true);
        pickup.teleopInit();
        drive.teleopInit();
        shooter.teleopInit();
    }

    public void teleopInit() {
        pickup.teleopInit();
        shooter.teleopInit();
        drive.teleopInit();

        Components.encoderleftdrive.reset();
        Components.encoderrightdrive.reset();

    }

    public void dashboardUpdate() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "Shooter Up: " + Components.UpShooterLimit.get());
        lcd.println(DriverStationLCD.Line.kUser2, 1, "ShooterDown: " + Components.DownShooterLimit.get() + "       ");
        lcd.println(DriverStationLCD.Line.kUser3, 1, "Pickup Up: " + Components.UpPickupLimit.get() + "    ");
        lcd.println(DriverStationLCD.Line.kUser4, 1, "Pickup Down: " + Components.DownPickupLimit.get() + "     ");
        lcd.println(DriverStationLCD.Line.kUser5, 1, "Ultrasonic: " + Components.uSonicDist + "     ");
        lcd.println(DriverStationLCD.Line.kUser6, 1, "Shooter Pot: " + Components.ShooterPot.getAverageVoltage() + "      ");
        lcd.updateLCD();
        SmartDashboard.putNumber("Battery: ", ds.getBatteryVoltage());
        SmartDashboard.putNumber("Ultrasonic", Components.uSonicDist);
        SmartDashboard.putNumber("AutoState", autoIndex);

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        teleopTime = 0;
        dashboardUpdate();
        switch (teleopState) {

            case PRE_OPERATOR_MOVE:

                Components.leftdriveY = -.25;
                Components.rightdriveY = -.25;
                drive.Drivemain();
                if (Math.abs(Utilities.deadband(components.GamePaddrive.getRawAxis(2), .1)) > 0
                        || Math.abs(Utilities.deadband(components.GamePaddrive.getRawAxis(5), .1)) > 0) {

                    teleopState = STANDARD_TELEOP;
                }

                break;

            case STANDARD_TELEOP:

                components.upDateJoystickVals();
                components.updateSensorVals();
                components.updatedrivevals();
                pickup.teleop();
                shooter.teleop();
                drive.Drivemain();

                break;

        }
        if (teleopTime != oldTeleopTime) {
            oldTeleopTime = teleopTime;
        }

    }

    public double getLastUpdateTime() {
        return table.getDouble("TIME");
    }

    public void autoShootTwo() {
        switch (autoIndex) {
            case 0:
                Components.pickupdown = true;
                Components.rollerfoward = true;
                autoIndex++;
                break;

            case 1:
                if (pickup.getPickarm() == pickup.STOPPED_DOWN) {
                    Components.rollerfoward = false;
                    Components.rollerstop = true;
                    autoIndex++;
                }
                break;
            case 2:
                if (ds.getMatchTime() > 3) {
                    Components.rollerstop = false;
                    Components.shootsinglespeed = true;
                    autoIndex++;
                }
                break;
            case 3:
                if (shooter.getshooterstate() == shooter.movingup) {
                    Components.shootsinglespeed = false;
                }
                autoIndex++;
                break;
            case 4:
                if (shooter.getshooterstate() == shooter.down) {
                    Components.rollerfoward = true;
                    autoIndex++;
                }

                autoIndex++;

                break;
            case 5:
                if (ds.getMatchTime() > 7) {
                    Components.rollerfoward = false;

                    autoIndex++;
                }
                break;
            case 6:
                if (pickup.getPickarm() == pickup.STOPPED_DOWN) {
                    Components.rollerstop = true;
                    Components.shootsinglespeed = false;
                    autoIndex++;
                }
                break;

        }
    }

    public void autoMoveShootUltrasonic() {
        switch (autoIndex) {
            case 0:
                drive.setDistanceToTravel(500, 500, .25);
                drive.startdriving(true);
                autoIndex++;
                break;
            case 1:
                if ((ds.getMatchTime() > 2.5 && Components.encoderrightdrive.getDistance() > 171 && Components.uSonicDist < 11) || ds.getMatchTime() > 5.5) {
                    System.out.println("Stop");
                    drive.stop();
                    Components.pickupdown = true;
                    Components.rollerfoward = true;
                    autoIndex++;
                }
                break;
            case 2:
                if (pickup.getPickarm() == pickup.STOPPED_DOWN) {
                    Components.rollerfoward = false;
                    Components.rollerstop = true;
                    Components.pickupdown = false;
                    autoIndex++;
                }
                break;
            case 3:
                if (ds.getMatchTime() > 7) {
                    Components.rollerstop = false;
                    Components.singleSpeedButton = true;
                    autoIndex++;
                }
                break;
            case 4:
                if (shooter.getshooterstate() == shooter.stopped) {
                    Components.singleSpeedButton = false;
                    Components.shooterDownButton = true;
                    autoIndex++;
                }

                break;

            case 5:
                if (shooter.getshooterstate() == shooter.down) {
                    Components.pickuptop = true;
                    Components.shooterDownButton = false;
                    autoIndex++;
                }
                break;
            case 6:
                if (pickup.getPickarm() == pickup.STOPPED_UP) {
                    Components.pickuptop = false;

                }
                break;
        }
    }

    public void autoMoveShootUltrasonicHotZone() {
        switch (autoIndex) {
            case 0:
                drive.setDistanceToTravel(5000, 5000, .4);
                drive.startdriving(true);
                autoIndex++;
                break;
            case 1:
                if ((ds.getMatchTime() > 2.5 && Components.encoderrightdrive.getDistance() > 171 && Components.uSonicDist < 11) || ds.getMatchTime() > 5) {
                    
                    drive.stop();
                    Components.pickupdown = true;
                    Components.rollerfoward = true;
                    autoIndex++;
                }
                break;
            case 2:
                if (pickup.getPickarm() == pickup.STOPPED_DOWN) {
                    Components.rollerfoward = false;
                    Components.rollerstop = true;
                    Components.pickupdown = false;
                    autoIndex++;
                }
                break;
            case 3:
                if (ds.getMatchTime() > 3) {
                    Components.rollerstop = false;
                    Components.singleSpeedButton = true;
                    autoIndex++;
                }
                break;
            case 4:
                if (shooter.getshooterstate() == shooter.stopped) {
                    Components.singleSpeedButton = false;
                    Components.shooterDownButton = true;
                    autoIndex++;
                }

                break;

            case 5:
                if (shooter.getshooterstate() == shooter.down) {
                    Components.pickuptop = true;
                    Components.shooterDownButton = false;
                    autoIndex++;
                }
                break;
            case 6:
                if (pickup.getPickarm() == pickup.STOPPED_UP) {
                    Components.pickuptop = false;

                }
                break;
        }
    }

    public void autoShoot() {
        switch (autoIndex) {
            case 0:
                Components.pickupdown = true;
                Components.rollerfoward = true;
                autoIndex++;
                break;

            case 1:
                if (pickup.getPickarm() == pickup.STOPPED_DOWN) {
                    Components.rollerfoward = false;
                    Components.rollerstop = true;
                    Components.pickupdown = false;
                    autoIndex++;
                }
                break;
            case 2:
                if (ds.getMatchTime() > 3) {
                    Components.rollerstop = false;
                    Components.singleSpeedButton = true;
                    autoIndex++;
                }
                break;
            case 3:
                if (shooter.getshooterstate() == shooter.stopped) {
                    Components.singleSpeedButton = false;
                    Components.shooterDownButton = true;
                    autoIndex++;
                }

                break;

            case 4:
                if (shooter.getshooterstate() == shooter.down) {
                    Components.pickuptop = true;
                    Components.shooterDownButton = false;
                    autoIndex++;
                }
                break;
            case 5:
                if (pickup.getPickarm() == pickup.STOPPED_UP) {
                    Components.pickuptop = false;

                }
                break;
        }
    }
}

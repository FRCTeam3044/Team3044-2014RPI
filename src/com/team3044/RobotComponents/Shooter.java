package com.team3044.RobotComponents;

import com.team3044.robot.Components;
import com.team3044.robot.RobotMain;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 *
 *
 * Has CAN Limit Switches are set to normally closed ie will send a normally
 * true signal
 *
 *
 * @author Joey
 */
public class Shooter {

    Components Components;
    private boolean islimitshooteruptrigger;
    private boolean islimitshooterdowntrigger;

    private boolean shootdownbutton = Components.shooterdown;
    private boolean shootpass = Components.pass;
    private boolean shoottruss = Components.truss;
    private boolean shootsinglebutton = Components.shootsinglespeed;

    //  private double shootspeedone = Components.shootspeedone;
    //private double shootspeedtwo = Components.shootspeedtwo;
    //  private double shootspeedthree = Components.shootspeedthree;
    private double shootdownspeed = Components.shootdownspeed * 12;
    private double trussspeed = Components.trussspeed * 12;
    private double passspeed = Components.passspeed * 12;
    public double singlespeed = .1 * 12;

    //joey heres your speed that you wanted
    public double shooterspeed = .5;

    private double shootpotvalue = Components.potvalue;
    private double initialpot = 0;
    private double shootpotdown = 1.65;
    // private double shootpotlow =2.4;
    // private double shootpotmiddle = 2.6;
    public double shootpothigh = 3;
    private double shootsinglepot = 3;
    private double trusspothigh = 2.2;
    private double passpot = 3;
    private double shootpot;

    private int shootstate;

    public final int down = 0;
    public final int movingup = 1;
    public final int speedone = 2;
    public final int speedtwo = 3;
    public final int speedthree = 4;
    public final int stopped = 5;
    public final int movingdown = 6;

    public boolean tempbutton = false;
    public boolean templimit = true;

    DriverStationLCD ds = DriverStationLCD.getInstance();
    DriverStation DS = DriverStation.getInstance();
    
    //private double p;
    //private double i;
    //private double d;


    public void robotInit() {
        
        shootstate = down;
        try {
            Components.shootermotorleft.setX(0);
            Components.shootermotorleft2.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Components.shootermotorright.setX(0);
            Components.shootermotorright2.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        initialpot = Components.ShooterPot.getAverageVoltage();
        //shootpotdown +=initialpot;
        //shootpotlow+= initialpot;
        //shootpotmiddle+= initialpot;
        //shootpothigh+=initialpot;

    }

    ;
    
    public void autoInit() {
    }

    ;
    
    public void teleopInit() {
        //  p= DS.getAnalogIn(2);
        //i= DS.getAnalogIn(3);
        //d= DS.getAnalogIn(4);
        // try {
        //       Components.shootermotorleft.enableControl();
        //     Components.shootermotorright.enableControl();
        //   Components.shootermotorleft2.enableControl();
        //          Components.shootermotorright2.enableControl();
//        } 
        //  catch (CANTimeoutException ex) {
        //        ex.printStackTrace();
        //  }

    }

    ;
    
    public void disabledInit() {
        try {
            Components.shootermotorleft.setX(0);
            Components.shootermotorleft2.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Components.shootermotorright.setX(0);
            Components.shootermotorright2.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void teleop() {
        singlespeed = DS.getAnalogIn(1) * 12;
        shootsinglepot = DS.getAnalogIn(2);
        shoot();
    }

    public boolean islimitshooterup() {

        islimitshooteruptrigger = Components.UpShooterLimit.get();
        //limit normally closed
        return !islimitshooteruptrigger;
    }

    public boolean islimitshooterdown() {

        islimitshooterdowntrigger = Components.DownShooterLimit.get();
        //limit normally closed
        return !islimitshooterdowntrigger;
    }

    public double setshootspeed() {

        return shooterspeed;
    }

    public double setshootpot() {
        return shootpot;
    }

    public void shoot() {
        //boolean shootbutton = Components.shoot;
        //boolean shootdownbutton = Components.shooterdown;

        switch (shootstate) {
            case down:
                tempbutton = Components.shootsinglespeed;
                templimit = islimitshooterup();

                if (Components.shootsinglespeed && islimitshooterup() && Components.DownPickupLimit.get()) {
                    try {
                        Components.shootermotorleft.setX(singlespeed);
                        Components.shootermotorleft2.setX(singlespeed);
                        Components.shootermotorright.setX(-singlespeed);
                        Components.shootermotorright2.setX(-singlespeed);

                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();

                    }
                    shootpothigh = shootsinglepot;
                    shootstate = movingup;
                } else if (Components.truss == true && islimitshooterup() == true  && Components.DownPickupLimit.get()) {
                    try {
                        Components.shootermotorleft.setX(trussspeed);
                        Components.shootermotorleft2.setX(trussspeed);
                        Components.shootermotorright.setX(-trussspeed);
                        Components.shootermotorright2.setX(-trussspeed);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                    shootpothigh = trusspothigh;
                    shootstate = movingup;
                } else if (Components.pass == true && islimitshooterup() == true && Components.DownPickupLimit.get()) {
                    try {
                        Components.shootermotorleft.setX(passspeed);
                        Components.shootermotorleft2.setX(passspeed);
                        Components.shootermotorright.setX(-passspeed);
                        Components.shootermotorright2.setX(-passspeed);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                    shootpothigh = passpot;
                    shootstate = movingup;
                } else if (Components.shoot == true && islimitshooterup() == true  && Components.DownPickupLimit.get()) {
                    try {
                        Components.shootermotorleft.setX(shooterspeed);
                        Components.shootermotorleft2.setX(shooterspeed);
                        Components.shootermotorright.setX(-shooterspeed);
                        Components.shootermotorright2.setX(-shooterspeed);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                    shootpothigh = shootpot;
                    shootstate = movingup;
                }
                break;

            case movingup:
                if (islimitshooterup() == false || Components.potvalue >= shootpothigh) {
                    shootstate = stopped;
                    try {
                        Components.shootermotorleft.setX(0);
                        Components.shootermotorleft2.setX(0);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        Components.shootermotorright.setX(0);
                        Components.shootermotorright2.setX(0);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case stopped:
                try {
                    if (Components.shooterdown && islimitshooterdown() == true  && Components.DownPickupLimit.get()) {

                        Components.shootermotorleft.setX(shootdownspeed);
                        Components.shootermotorleft2.setX(shootdownspeed);

                        Components.shootermotorright.setX(-shootdownspeed);
                        Components.shootermotorright2.setX(-shootdownspeed);

                        shootstate = movingdown;
                    }
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
                break;

            case movingdown:
                if ((islimitshooterdown() == false) || (Components.potvalue < shootpotdown)) {

                    try {
                        Components.shootermotorleft.setX(0);
                        Components.shootermotorleft2.setX(0);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        Components.shootermotorright.setX(0);
                        Components.shootermotorright2.setX(0);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                    shootstate = down;
                }
        }
    }

    public int getshooterstate() {
        return shootstate;
    }

}

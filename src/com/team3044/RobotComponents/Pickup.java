/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author minh
 */
package com.team3044.RobotComponents;

import com.team3044.robot.Components;
import edu.wpi.first.wpilibj.*;

public class Pickup {

    //public static boolean rollerfoward = Components.rollerfoward;
    //public static boolean rollerreverse = Components.rollerreverse;
    //public static boolean rollerstop = Components.rollerstop;
    //public static boolean pickupdown = Components.pickupdown;
    //public static boolean pickuptop = Components.pickuptop;
    //public static boolean pickupmiddle=Components.pickupmiddle;
    //public static boolean pickupstop = Components.pickupstop;

    DriverStation ds = DriverStation.getInstance();
    //AnalogChannel Potentiometer= Components.RollerPot;
    //DigitalInput limitDown = Components.DownPickupLimit;
    //DigitalInput limitUp = Components.UpPickupLimit;

    private int n;
    private int k;
    final int STOPPED = 0;
    final int MOVING_FORWARD = 1;
    final int MOVING_BACKWARD = 2;
    public final int STOPPED_MID = 0;
    public final int STOPPED_DOWN = 1;
    public final int MOVE_DOWN = 2;
    public final int STOPPED_UP = 3;
    public final int MOVE_UP = 4;
    public final int MOVE_UP_INC = 5;
    public Pickup() {
       
    }

    public void robotInit() {
        n = STOPPED_MID;//problem need to be fix
        k = STOPPED;// problem need to be fix
        Components.PickupRollers.set(0.0);
        Components.LiftingPickup.set(Relay.Value.kOff);
    }

    public void teleopInit() {
        robotInit();
    }

    public void teleop() { //function 
        Arm();
        roller();
        

    }

    private void roller() {

        switch (k) {
            case STOPPED: {// Roller stopped
                if (Components.rollerfoward) {
                    k = MOVING_FORWARD;
                    Components.PickupRollers.set(1);
                } else if (Components.rollerreverse) {
                    k = MOVING_BACKWARD;
                    Components.PickupRollers.set(-1);
                }

            }
            break;
            case MOVING_FORWARD: {// roller is moving to pick the ball
                if (Components.rollerstop || Components.rollerreverse || !Components.rollerfoward) {
                    k = STOPPED;
                    Components.PickupRollers.set(0.0);
                }

            }
            break;
            case MOVING_BACKWARD: { //roller is moving to throu the ball out
                if (Components.rollerfoward || Components.rollerstop || !Components.rollerreverse) {
                    k = STOPPED;
                    Components.PickupRollers.set(0.0);
                }
            }
            break;

        }
    }

    private void Arm() {

        switch (n) {
            case STOPPED_MID: {
                if (Components.pickuptop) {
                    n = MOVE_UP;
                    Components.LiftingPickup.set(Relay.Value.kForward);
                } else if (Components.pickupdown) {
                    n = MOVE_DOWN;
                    Components.LiftingPickup.set(Relay.Value.kReverse);
                }
            }
            break;
            case STOPPED_DOWN: {
                if (Components.pickuptop) {
                    n = MOVE_UP;
                    Components.LiftingPickup.set(Relay.Value.kForward);
                }

            }
            break;
            case MOVE_DOWN: {
                if (Components.DownPickupLimit.get()) {
                    n = STOPPED_DOWN;
                    Components.LiftingPickup.set(Relay.Value.kOff);
                } else if (Components.pickuptop) {
                    n = STOPPED_MID;
                    Components.LiftingPickup.set(Relay.Value.kOff);
                }else if(Components.pickupEmergency && Components.UpPickupLimit.get()){
                    Components.LiftingPickup.set(Relay.Value.kForward);
                }

            }
            break;

            case STOPPED_UP: {
                if (Components.pickupdown) {
                    n = MOVE_DOWN;
                    Components.LiftingPickup.set(Relay.Value.kReverse);
                }
                
                if(!Components.UpPickupLimit.get()){
                    n = STOPPED_MID;
                }

            }
            break;
            case MOVE_UP: {
                if (Components.UpPickupLimit.get()) {
                    n = STOPPED_UP;
                    Components.LiftingPickup.set(Relay.Value.kOff);
                } else if (Components.pickupdown) {
                    n = STOPPED_MID;
                    Components.LiftingPickup.set(Relay.Value.kOff);
                }

            }
            break;
        }
    }

    public int getRoller() {
        return k;
    }

    public int getPickarm() {
        return n;
    }
    public void DisableInit(){
        
    }
    public void Disable(){
        Components.LiftingPickup.set(Relay.Value.kOff);
        Components.PickupRollers.set(0.0);
        k=STOPPED;
        if (n==MOVE_UP||n==MOVE_DOWN){
            n=STOPPED;
        }
       
    }
}
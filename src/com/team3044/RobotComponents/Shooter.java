package com.team3044.RobotComponents;

import com.team3044.robot.Components;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 *
 * Has CAN Limit Switches are set to normally closed ie will send a normally
 * true signal
 *
 *
 * @author Ally
 */
public class Shooter {
    
    private final double shootDownSpeed = -.1 * 12;
    private final double trussSpeed = .8 * 12;
    private final double passSpeed = .25 * 12;
    private double singlespeed = .55 * 12;
    private final double longDistanceSpeed = .86 * 12;
    private final double normalShootSpeed = .55 * 12;
    
    public double shootpothigh = 3.375;
    private double shootsinglepot = 3.55;
    private final double trussPot = 2.2;
    private final double passPot = 3;
    private final double normalShootPot = 3.55;
    private final double longDistancePot = 3.375;

    private double shootSpeed = singlespeed;
    private double targetUpPot = shootsinglepot;
    
    private int shootstate = 0;

    public final int DOWN = 0;
    public final int MOVING_UP = 1;
    public final int UP = 5;
    public final int MOVING_DOWN = 6;

    double time = 0.0;
    double oldTime = 0.0;

    DriverStationLCD dsLCD = DriverStationLCD.getInstance();
    DriverStation DS = DriverStation.getInstance();
    
    private final double DOWN_SHOOTER_POT_VALUE = 1.55; //Check this

    public void robotInit() {
        shootstate = DOWN;
    }
    
    public void disabledInit() {
        try {
            Components.shootermotorleft.setX(0);
            Components.shootermotorleft2.setX(0);
            Components.shootermotorright.setX(0);
            Components.shootermotorright2.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void init(){ 
        //RESET VALUES
    }

    public void teleop() {
        singlespeed = DS.getAnalogIn(1) * 12;
        shootsinglepot = DS.getAnalogIn(2);
        time = DS.getMatchTime();
        shoot();
    }


    public void shoot() {
        if(Components.shootsinglespeed){
            shootSpeed = singlespeed;
            targetUpPot = shootsinglepot;
        }else if(Components.pass){
            shootSpeed = passSpeed;
            targetUpPot = passPot;
        }else if(Components.truss){
            shootSpeed = trussSpeed;
            targetUpPot = trussPot;
        }else if(Components.longdistanceshoot){
            shootSpeed = longDistanceSpeed;
            targetUpPot = longDistancePot;
        }else if(Components.shoot){
            shootSpeed = normalShootSpeed;
            targetUpPot = normalShootPot;
        }
        switch (shootstate) {
            case DOWN:
                if ((Components.shootsinglespeed || Components.pass || Components.truss || Components.longdistanceshoot || Components.shoot)
                        && (Components.DownShooterLimit.get() || Components.ShooterPot.getAverageVoltage() <= DOWN_SHOOTER_POT_VALUE) && Components.DownPickupLimit.get()) {
                    try {
                        Components.shootermotorleft.setX(shootSpeed);
                        Components.shootermotorleft2.setX(shootSpeed);
                        Components.shootermotorright.setX(-shootSpeed);
                        Components.shootermotorright2.setX(-shootSpeed);

                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();

                    }
                    shootstate = MOVING_UP;
                }
            case MOVING_UP:
                if (Components.UpShooterLimit.get() || Components.ShooterPot.getAverageVoltage() >= targetUpPot) {
                    shootstate = UP;
                    oldTime = DS.getMatchTime();

                    try {
                        Components.shootermotorleft.setX(0);
                        Components.shootermotorleft2.setX(0);
                        Components.shootermotorright.setX(0);
                        Components.shootermotorright2.setX(0);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case UP:
                try {
                    if (time - oldTime > .2 && Components.DownPickupLimit.get()) {

                        Components.shootermotorleft.setX(shootDownSpeed);
                        Components.shootermotorleft2.setX(shootDownSpeed);

                        Components.shootermotorright.setX(-shootDownSpeed);
                        Components.shootermotorright2.setX(-shootDownSpeed);

                        shootstate = MOVING_DOWN;
                    }
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
                break;

            case MOVING_DOWN:
                if (Components.DownShooterLimit.get() || (Components.ShooterPot.getAverageVoltage() < DOWN_SHOOTER_POT_VALUE)) {

                    try {
                        Components.shootermotorleft.setX(0);
                        Components.shootermotorleft2.setX(0);
                        Components.shootermotorright.setX(0);
                        Components.shootermotorright2.setX(0);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }

                    shootstate = DOWN;
                }
        }
    }

    public int getshooterstate() {
        return shootstate;
    }

}

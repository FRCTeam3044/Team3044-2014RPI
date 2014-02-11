package com.team3044.RobotComponents;

import com.team3044.robot.Components;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
/**
 * dsaf
 * 
 * @author Joey
 */
public class Shooter {
    private boolean islimitshooteruptrigger = Components.islimitshooteruptriggerd;
    private boolean islimitshooterdowntrigger = Components.islimitshooterdowntriggerd;
    private DigitalInput upshooterlimit = Components.UpShooterLimit;
    private DigitalInput downshooterlimit = Components.DownShooterLimit;
    private AnalogChannel shootpot = Components.ShooterPot;
    CANJaguar shootermotor = Components.shootermotorleft;
    CANJaguar shootermotorneg = Components.shootermotorright;
    
    private boolean shootbutton = Components.shoot;
    private boolean shootdownbutton = Components.shooterdown;
    private boolean shootpass = Components.pass;
    private boolean shoottruss = Components.truss;
    private boolean shootsinglebutton = Components.shootsinglespeed;
    
    private double shootspeedone = Components.shootspeedone;
    private double shootspeedtwo = Components.shootspeedtwo;
    private double shootspeedthree = Components.shootspeedthree;
    private double shootdownspeed = Components.shootdownspeed;
    
    private double shootpotvalue = Components.potvalue;
    private double shootpotposition = Components.shooterpotpostion;
    
    private  int shootstate;
        
    private final int down =0;
    private final int movingup =1;
    private final int speedone= 2;
    private final int speedtwo = 3;
    private final int speedthree =4;
    private final int stopped =5;
    private final int movingdown = 6;
    
    
    private double singlespeed = 1; 
    private double initialpot=0;
    private double shootpotdown = 15;
    private double shootpotlow =45;
    private double shootpotmiddle = 65;
    private double shootpothigh = 75;
    
    
    
    public Shooter(){
    
    }
    
    public void robotInit(){
    shootstate = down;
    shootermotor.set(0);
    shootermotorneg.set(0);
    initialpot =shootpot.getAverageVoltage();
    shootpotdown +=initialpot;
    shootpotlow+= initialpot;
    shootpotmiddle+= initialpot;
    shootpothigh+=initialpot;
    
    try {
            shootermotor = new CANJaguar(1,CANJaguar.ControlMode.kPercentVbus);
            shootermotor.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shootermotor.configEncoderCodesPerRev(250);
            shootermotorneg = new CANJaguar(2,CANJaguar.ControlMode.kPercentVbus);
            shootermotorneg.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            shootermotorneg.configEncoderCodesPerRev(250);
            
            //jag.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            
            
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    };
    
    public void autoInit(){};
    
    public void teleopInit(){
    try {
            shootermotor.enableControl();
            shootermotorneg.enableControl();
        } 
    catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    
    };
    
    public void disabledInit(){
        try {
            shootermotor.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            shootermotorneg.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
   public void teleop(){
       shoot();
   }
    
    public boolean islimitshooterup(){
        
        islimitshooteruptrigger = upshooterlimit.get();
        
        return islimitshooteruptrigger;
    }
    
    
    public boolean islimitshooterdown(){
        
        islimitshooterdowntrigger = downshooterlimit.get();
        
        return islimitshooterdowntrigger;
    }
    
    
    public double setshootspeedone(){
        
        if(shootbutton == true){
            shootspeedone= 1;
        }
        else if (shootpass == true){
            shootspeedone =.5;
        }
        else if (shoottruss ==true){
            shootspeedone = .6;
        }
        
        return shootspeedone;
    }
    
    
    public double setshootspeedtwo(){
        if(shootbutton == true){
            shootspeedtwo= .75;
        }
        else if (shootpass == true){
            shootspeedtwo = .3;
        }
        else if (shoottruss ==true){
            shootspeedtwo = .4;
        }
        return shootspeedtwo;
    }
    
    
    public double setshootspeedthree(){
        
        if(shootbutton == true){
            shootspeedthree= .4;
        }
        else if (shootpass == true){
            shootspeedthree = .2;
        }
        else if (shoottruss ==true){
            shootspeedthree = .2;
        }
        
        return shootspeedthree;
    }
    
    
    public void shoot(){
        /*
        if(buttonone&& islimitshooteruptrigger == false && shootpotposition ==2){//moveup
            shootermotor.set(shootspeed);
            shootermotorneg.set(-shootspeed);
        }
        else if(buttonone && islimitshooteruptrigger == true && shootpotposition ==3){//stopped
            shootermotor.set(0);
            shootermotorneg.set(0);
         }
        else if(buttontwo && islimitshooterdowntrigger == false&& shootpotposition ==2){//movedown
            shootermotor.set(shootdownspeed);
            shootermotor.set(-shootdownspeed);
        }
        else if (buttontwo && islimitshooterdowntrigger == true && shootpotposition ==1){//down
            shootermotor.set(0);
            shootermotorneg.set(0);
        }
     */
        switch(shootstate){
            case down:
                /*if(islimitshooterdowntrigger==true || shootpotposition ==1)
                {
                    shootstate =down;
                    shootermotor.set(0);
                    shootermotorneg.set(0);
                }*/
               if(shootbutton==true &&  islimitshooteruptrigger ==false)
                {
            try {
                shootermotor.setX(shootspeedone);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(-shootspeedone);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                    shootstate = speedone;
                }
               else if(shootsinglebutton==true){
            try {
                shootermotor.setX(singlespeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotor.setX(-singlespeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                    shootstate = movingup;
               }
               
              break;
           case movingup:
                if(islimitshooteruptrigger==true || shootpotposition==3)
                {
                    shootstate=stopped;
            try {
                shootermotor.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                }
                break;
            case speedone:
               if(shootdownbutton == true)
               {
            try {
                shootermotor.setX(shootdownspeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(-shootdownspeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                   shootstate=movingdown;
               } 
               else if(shootpot.getAverageVoltage()>shootpothigh || islimitshooteruptrigger == true){
            try {
                shootermotor.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                    shootstate = stopped;
                }
                else if(shootpot.getAverageVoltage()> shootpotmiddle){
            try {
                shootermotor.setX(shootspeedthree);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(-shootspeedthree);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                    shootstate = speedthree;
                }
                else if(shootpot.getAverageVoltage()>shootpotlow)
                 {
            try {
                shootermotor.setX(shootspeedtwo);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try { 
                shootermotorneg.setX(-shootspeedtwo);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                   shootstate = speedtwo;
                 }
                 
                break;
                
            case speedtwo:
                 if(shootdownbutton ==true){
            try {
                shootermotor.setX(shootdownspeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(-shootdownspeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                     shootstate =movingdown;
                 }
                 else if(shootpot.getAverageVoltage() >shootpothigh || islimitshooteruptrigger ==true){
            try {
                shootermotor.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                     shootstate =stopped;
                 }
                 else if(shootpot.getAverageVoltage()>shootpotmiddle)
                 {
            try {
                shootermotor.setX(shootspeedthree);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotor.setX(-shootspeedthree);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                     shootstate = speedthree;
                 }
                
                break;
                
            case speedthree:
                
                if(shootdownbutton ==true){
            try {
                shootermotor.setX(shootdownspeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(-shootdownspeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                    shootstate =movingdown;
                }
                else if(shootpot.getAverageVoltage() >shootpothigh || islimitshooteruptrigger ==true){
            try {
                shootermotor.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                     shootstate =stopped;
                 }
                
                break;
                
            case stopped:
                
                if(shootdownbutton&& islimitshooterdowntrigger ==false)
                {
            try {
                shootermotor.setX(shootdownspeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(-shootdownspeed);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                    shootstate= movingdown;
                }
                
                break;
                
            case movingdown:
                 if(islimitshooterdowntrigger==false || shootpot.getAverageVoltage()<shootpotdown)
                {
                    
                    try {
                shootermotor.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                shootermotorneg.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
                    shootstate=down;
                }
        } 
    }
    
    public int getshooterstate()
    {
        return shootstate;
    }
    
}

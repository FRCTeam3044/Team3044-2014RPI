/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team3044.network;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.io.IOException;

/**
 *
 * @author Joey
 */
public class NetTable {
    NetworkTable table;
    private static NetTable network;
    /***
     * 
     * @param name Name of the network table to create
     * Use all caps for the tag names
     */
    public NetTable(String name){
        /*try {
            NetworkTable.initialize();
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
        table = NetworkTable.getTable(name);
    }
    
    /***
     * 
     * @param tag String for the tag (All caps)
     * @param number Number value to add
     */
    public void putDouble(String tag, double number){
        tag = tag.toUpperCase();
        table.putNumber(tag, number);
    }
    /***
     * 
     * @param tag
     * @return the value of the double at the tag location
     */
    public double getDouble(String tag){
        tag = tag.toUpperCase();
        return table.getNumber(tag);
    }
    
    public double getDouble(String tag, double defaultValue){
        tag = tag.toUpperCase();
        return table.getNumber(tag,defaultValue);
    }
    /***
     * 
     * @param tag 
     * @param string Value of string to put in place of the value at tag 
     */
    public void putString(String tag, String string){
        tag = tag.toUpperCase();
        table.putString(tag, string);
    }
    
    /**
     * 
     * @param tag
     * @return Returns the string at the location
     */
    public String getString(String tag){
        tag = tag.toUpperCase();
        return table.getString(tag);
    }

    
    public static NetTable getInstance(){
        if(network == null){
            network = new NetTable("CAMERA");
        }
        return network;
    }
    
}

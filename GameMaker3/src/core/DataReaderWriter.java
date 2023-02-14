/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;


import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Some methods to READ/WRITE data.
 * @author Panca
 */
public class DataReaderWriter 
{
    /**
     * Write the bytes of a string to an file.
     */
    public static void writeString(String s, DataOutputStream dos)
    {
        try{
            if(s==null || s.length() == 0)
            {
                dos.writeInt(0);
                return;
            }
            dos.writeInt(s.length());
            dos.writeBytes(s);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Reads a string from a datainputstream.
     * @param dis
     * @return 
     */
    public static String readString(DataInputStream dis)
    {
        String s = "";
        try{
            
            int numChar = dis.readInt();
            if(numChar == 0)
                return null;
            for(int i = 0; i < numChar; i++) {
                s += (char)dis.readByte();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return s;
    }
}

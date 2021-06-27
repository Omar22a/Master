/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xtea;

import static xtea.XTEA.char2int;
import static xtea.XTEA.decipher;
import static xtea.XTEA.encipher;
import static xtea.XTEA.int2char;
import static xtea.XTEA.key_treatment;
import static xtea.XTEA.split;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Omar
 */
public class OFB {
     OFB() {}
     
     
    public String OFBchiffrer (char[] k, String plaintext, char[] iv){
        List<String> ptext = split(plaintext); //ptext contient des bloc de 8octet
        String textchif="";
        int [] c;
        
        int[] civ= encipher(char2int(iv),key_treatment(k));//chiffrement de if
        char[] eiv=int2char(civ);
        
        char[] x=new char[8];
        
        for (int j=0;j<iv.length;j++)
                x[j]=(char) (ptext.get(0).toCharArray()[j] ^ eiv[j]); //xor de text avex resultat de chiffrement de iv
        
        String blocchiffre=new String(x);
        
        textchif+=blocchiffre;
        
        for(int i=1;i<ptext.size();i++)
        {
            String s=ptext.get(i);
            while (s.length()<8)
            {
               s+=" "; //pour obtient des bloc de exactement 8octet si la taiile du bloc et inf de 8
            }
             c= encipher(char2int(eiv),key_treatment(k));
             eiv=int2char(c);
             
            for (int j=0;j<s.toCharArray().length;j++)
            x[j]=(char) (s.toCharArray()[j] ^ eiv[j]);// i iem bloc xor avec le i-1  cipher precedant
            blocchiffre=new String(x);
            textchif=textchif+blocchiffre;
        }

          return textchif;
    }
    
    
    
    public String OFBdechiffrer (char[] k, String textchif, char[] iv){
     List<String> bb = split(textchif);
     
        String textcliare="";
        
        int[] dd= encipher(char2int(iv),key_treatment(k));
        char[] eiv=int2char(dd);
        char[] xx=new char[8];
        for (int j=0;j<iv.length;j++)
                xx[j]=(char) (bb.get(0).toCharArray()[j] ^ eiv[j]);
         String p=new String(xx);//obtenant M1
        textcliare=textcliare+p;
        
        
        for(int i=1;i<bb.size();i++)
        {

            dd= encipher(char2int(eiv),key_treatment(k));
            eiv=int2char(dd);
            for (int j=0;j<bb.get(i).toCharArray().length;j++)
                xx[j]=(char) ( bb.get(i).toCharArray()[j]^eiv[j] );
            p=new String(xx);
            textcliare+=p;
        }
        return textcliare;
    
    }
    
    
   
}
 

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
public class CBC {
     CBC() {}
     
     
    public String CBCchiffrer (char[] k, String plaintext, char[] iv){
        List<String> ptext = split(plaintext); //ptext contient des bloc de 8octet
        String textchif="";
        char[] x=new char[8];
        
        for (int j=0;j<iv.length;j++)
                x[j]=(char) (ptext.get(0).toCharArray()[j] ^ iv[j]); //XOR de premier bloc de text
        
        int[] d= encipher(char2int(x),key_treatment(k));//chiffrement premier blocavec la cle IV =  C1
        char[] e=int2char(d);
        String blocchiffre=new String(e);
        textchif+=blocchiffre;
        
        
        for(int i=1;i<ptext.size();i++)
        {
            String s=ptext.get(i);
            while (s.length()<8)
            {
               s+=" "; //pour obtient des bloc de exactement 8octet si la taiile du bloc et inf de 8
            }
            for (int j=0;j<s.toCharArray().length;j++)
            x[j]=(char) (s.toCharArray()[j] ^ e[j]);// i iem bloc xor avec le i-1  cipher precedant
            d= encipher(char2int(x),key_treatment(k));// i em cipher 
            e=int2char(d);
            blocchiffre=new String(e);
            textchif+=blocchiffre;
        }

          return textchif;
    }
    
    
    
    public String CBCdechiffrer (char[] k, String textchif, char[] iv){
     List<String> bb = split(textchif);
        String textcliare="";
        int[] cc= char2int(bb.get(0).toCharArray());// c1
        int[] dd= decipher(cc,key_treatment(k));//dechiffremen de c1 avec la cle k
        char[] ee=int2char(dd);
        char[] xx=new char[8];
        for (int j=0;j<iv.length;j++)
                xx[j]=(char) (ee[j] ^ iv[j]);
        String p=new String(xx);//obtenant M1
        textcliare+=p;
        
        for(int i=1;i<bb.size();i++)
        {
            cc= char2int(bb.get(i).toCharArray());
            dd= decipher(cc,key_treatment(k));
            ee=int2char(dd);
            for (int j=0;j<bb.get(i).toCharArray().length;j++)
                xx[j]=(char) (ee[j] ^ bb.get(i-1).toCharArray()[j]);
            p=new String(xx);
            textcliare+=p;
        }
        return textcliare;
    
    }
    
  
}
 

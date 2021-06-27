/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xtea;


import java.util.List;
import static xtea.XTEA.char2int;
import static xtea.XTEA.encipher;
import static xtea.XTEA.int2char;
import static xtea.XTEA.key_treatment;
import static xtea.XTEA.split;

/**
 *
 * @author asus tatti
 */

public class CTR {
    
    
   public String  CTRchiffre (char[] key, String plaintext, String nonce){
       
        List<String> pText = split(plaintext);
        String txtchif="";
        char[] Block=new char[8];
        for(int i=0;i<pText.size();i++)
        {
            String iv=nonce+String.format("%04d", i);
            int[] IV= char2int(iv.toCharArray());
            int[] fctcrypt= encipher(IV,key_treatment(key));
            char[] e=int2char(fctcrypt);
            for (int j=0;j<pText.get(i).toCharArray().length;j++)
                Block[j]=(char) (pText.get(i).toCharArray()[j] ^ e[j]);
            String blocks=new String(Block);
            if(pText.get(i).toCharArray().length<8)
            {
                blocks=blocks.substring(0, Math.min(new String(Block).length(), pText.get(i).toCharArray().length));
            }
            txtchif+=blocks;
        }
        return txtchif;
   }
   
     public String  CTRdechiffre (char[] key, String txtchif, String nonce){
       
       List<String> textchiffre = split(txtchif);
        String txtdechif="";
        char[] bolckchif=new char[8];
        for(int i=0;i<textchiffre.size();i++)
        {
            String iv=nonce+String.format("%04d", i);
            int[] IV2= char2int(iv.toCharArray());
            int[] fctdecrypt= encipher(IV2,key_treatment(key));
            char[] ee=int2char(fctdecrypt);
            for (int j=0;j<textchiffre.get(i).toCharArray().length;j++)
                bolckchif[j]=(char) (ee[j] ^ textchiffre.get(i).toCharArray()[j]);
            String blockdechif=new String(bolckchif);
            if(textchiffre.get(i).toCharArray().length<8)
            {
                blockdechif=blockdechif.substring(0, Math.min(new String(bolckchif).length(), textchiffre.get(i).toCharArray().length));
            }
            txtdechif+=blockdechif;
        }
       return txtdechif;
   }
   
 
    

    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xtea;

import java.util.List;
import static xtea.XTEA.char2int;
import static xtea.XTEA.decipher;
import static xtea.XTEA.encipher;
import static xtea.XTEA.int2char;
import static xtea.XTEA.key_treatment;
import static xtea.XTEA.split;

/**
 *
 *
 */
public class ECB {

    ECB() {
    }

    public String ECBchfrer(char[] k, String t) {
        List<String> txtClr = split(t);
        String textChif = "";

        for (int i = 0; i < txtClr.size(); i++) {
            String s = txtClr.get(i);
            while (s.length() < 8) {
                s += " ";
            }
            int[] a1 = char2int(s.toCharArray());//2block de 4octet
            int[] chiffrer = encipher(a1, key_treatment(k));
            String textChiffrer = new String(int2char(chiffrer));
            textChif += textChiffrer;
        }
        return textChif;
    }

    public String ECBDechiffre(char[] k, String textChif) {

        List<String> txtChfr = split(textChif);
        // int textClair=txtClr.get(txtClr.size()-1).length();
        String textDechif = "";
        for (int i = 0; i < txtChfr.size(); i++) {
            int[] a2 = char2int(txtChfr.get(i).toCharArray());
            int[] dechiffrer = decipher(a2, key_treatment(k));
            String p = new String(int2char(dechiffrer));
            /*  if ((i==txtChfr.size()-1))
             p=p.substring(0, p.length());*/
            textDechif += p;
        }
        return textDechif;
    }

    
}

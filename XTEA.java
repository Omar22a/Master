/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xtea;

/**
 *
 * @author M@averick Ouelhaci Mohammed Abdelkader
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XTEA {

    private static final int NUM_ROUNDS = 32;

    static String read(String filename) {
        File f = new File(filename);
        try {
            byte[] bytes = Files.readAllBytes(f.toPath());
            return new String(bytes, "UTF-8");
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return "";
    }

    static int[] char2int(char[] c) {
        int[] res = new int[2];
        res[0] = (int) ((((int) c[0] & 0xff) << 24)
                | (((int) c[1] & 0xff) << 16)
                | (((int) c[2] & 0xff) << 8)
                | ((int) c[3] & 0xff));
        res[1] = (int) ((((int) c[4] & 0xff) << 24)
                | (((int) c[5] & 0xff) << 16)
                | (((int) c[6] & 0xff) << 8)
                | ((int) c[7] & 0xff));
        return res;
    }

    static char[] int2char(int[] i) {
        char[] res = new char[8];
        res[0] = (char) ((i[0] & 0xff000000) >>> 24);
        res[1] = (char) ((i[0] & 0x00ff0000) >>> 16);
        res[2] = (char) ((i[0] & 0x0000ff00) >>> 8);
        res[3] = (char) (i[0] & 0x000000ff);
        res[4] = (char) ((i[1] & 0xff000000) >>> 24);
        res[5] = (char) ((i[1] & 0x00ff0000) >>> 16);
        res[6] = (char) ((i[1] & 0x0000ff00) >>> 8);
        res[7] = (char) (i[1] & 0x000000ff);
        return res;
    }

    static int[] key_treatment(char[] c) //creer un tableau de 4 pour la cle avec shifft right 
    {
        int[] res = new int[4];
        res[0] = (int) ((((int) c[0] & 0xff) << 24)
                | (((int) c[1] & 0xff) << 16)
                | (((int) c[2] & 0xff) << 8)
                | ((int) c[3] & 0xff));
        res[1] = (int) ((((int) c[4] & 0xff) << 24)
                | (((int) c[5] & 0xff) << 16)
                | (((int) c[6] & 0xff) << 8)
                | ((int) c[7] & 0xff));
        res[2] = (int) ((((int) c[8] & 0xff) << 24)
                | (((int) c[9] & 0xff) << 16)
                | (((int) c[10] & 0xff) << 8)
                | ((int) c[11] & 0xff));
        res[3] = (int) ((((int) c[12] & 0xff) << 24)
                | (((int) c[13] & 0xff) << 16)
                | (((int) c[14] & 0xff) << 8)
                | ((int) c[15] & 0xff));
        return res;
    }

    static int[] encipher(int[] block, int[] key) {
        int sum = 0;
        int delta = 0x9E3779B9;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[0] += (((block[1] << 4) ^ (block[1] >> 5)) + block[1]) ^ (sum + key[sum & 3]);
            sum += delta;
            block[1] += (((block[0] << 4) ^ (block[0] >> 5)) + block[0]) ^ (sum + key[(sum >> 11) & 3]);
        }
        return block;
    }

    static int[] decipher(int[] block, int[] key) {
        int delta = 0x9E3779B9;
        int sum = delta * NUM_ROUNDS;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            block[1] -= ((block[0] << 4 ^ block[0] >> 5) + block[0]) ^ (sum + key[(sum >> 11) & 3]);
            sum -= delta;
            block[0] -= ((block[1] << 4 ^ block[1] >> 5) + block[1]) ^ (sum + key[sum & 3]);
        }
        return block;
    }

    static List<String> split(String t)//creer list contient des des text de taille de 8octet
    {
        List<String> blocks = new ArrayList<>();
        int i = 0;
        while (i < t.length()) {
            blocks.add(t.substring(i, Math.min(i + 8, t.length())));
            i += 8;
        }
        return blocks;
    }

    public static void main(String[] args) {
        String k = "";
        char[] iv = "95625487".toCharArray();

        while (k.length() < 16) {
            System.out.println("Introduisez votre clé 1 (16Octet) (k1) :");
            Scanner sc = new Scanner(System.in);
            k = sc.nextLine();
        }

        String kk = k.substring(0, Math.min(k.length(), 16));//prendre 16 premier octet de clé seulment
        char[] k1 = kk.toCharArray();

        //*************************//Chiffrement XTEA DE 8Octet ////////........................
        String t1;
        System.out.println("le mode Xtea\n");

        do {
            System.out.println("Le Text (8Octet) : ");
            Scanner sc = new Scanner(System.in);
            t1 = sc.next();
        } while (t1.length() < 8);
        String t2 = t1.substring(0, Math.min(t1.length(), 8));//prendre 8 premier octet de text
        char[] t3 = t2.toCharArray();
        int[] a1 = char2int(t3);
        int[] chiffrer = encipher(a1, key_treatment(k1));
        String textChiffrer = new String(int2char(chiffrer));
        System.out.println("Le text chiffré: \n" + textChiffrer);

        System.out.println("");
        int[] a2 = char2int(textChiffrer.toCharArray());
        int[] dechiffrer = decipher(a2, key_treatment(k1));
        String p = new String(int2char(dechiffrer));
        System.out.println("Le text Dechiffré: \n" + p);

        //*************************//Chiffrement ECB d un text Choisi ////////........................
        System.out.println("");
        String t = read("src/xtea/text.txt");

        System.out.println("le mode ECB \n");
        ECB ecb = new ECB();
        System.out.println("");
        System.out.println("Chifrement : \n" + ecb.ECBchfrer(k1, t));
        System.out.println("");
        System.out.println("Dechiffrement :  \n" + ecb.ECBDechiffre(k1, ecb.ECBchfrer(k1, t)));

        System.out.println("");
        //*************************//Chiffrement CBC d un text Choisi ////////........................
        System.out.println("le mode CBC \n");
        CBC cbc = new CBC();
        System.out.println("");
        System.out.println("Chifrement : \n" + cbc.CBCchiffrer(k1, t, iv));
        System.out.println("");
        System.out.println("Dechiffrement :  \n" + cbc.CBCdechiffrer(k1, cbc.CBCchiffrer(k1, t, iv), iv));

        System.out.println("");
        //*************************//Chiffrement OFB d un text Choisi ////////........................
        System.out.println("le mode OFB \n");
        OFB ofb = new OFB();
        System.out.println("");
        System.out.println("Chifrement : \n" + ofb.OFBchiffrer(k1, t, iv));
        System.out.println("");
        System.out.println("Dechiffrement :  \n" + ofb.OFBdechiffrer(k1, ofb.OFBchiffrer(k1, t, iv), iv));

        System.out.println("");
        //*************************//Chiffrement CTR d un text Choisi ////////........................
        System.out.println("le mode CTR \n");
        CTR ctr = new CTR();
        System.out.println("");
        System.out.println("Chifrement : \n" + ctr.CTRchiffre(k1, t, "nonce"));
        System.out.println("");
        System.out.println("Dechiffrement :  \n" + ctr.CTRdechiffre(k1, ctr.CTRchiffre(k1, t, "nonce"), "nonce"));

        System.out.println("");
             //*************************//Chiffrement XTS d un text Choisi ////////........................

        String k2 = "";
        char[] ii = "95625487".toCharArray();
        while (k2.length() < 16) {
            System.out.println("Introduisez votre clé 2 pour XTS (16Octet) (k2) : ");
            Scanner sc = new Scanner(System.in);
            k2 = sc.nextLine();
        }

        String kkk = k2.substring(0, Math.min(k2.length(), 16));//prendre 16 premier octet de clé seulment
        char[] k12 = kkk.toCharArray();
        System.out.println("");
        System.out.println("le mode XTS \n");
        XTS xts = new XTS();
        System.out.println("");
        System.out.println("Chifrement : \n" + xts.XTSchiffrer(k1, k12, t, ii));
        System.out.println("");
       //   System.out.println("Dechiffrement :  \n"+ctr.CTRdechiffre(k1, ctr.CTRchiffre(k1, t,"mnlk22"), "mnlk22"));

    }
}

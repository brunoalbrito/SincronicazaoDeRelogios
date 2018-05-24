/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mack.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class FileUtil {

    public static void writeFile(String nomeDoArquivo, String mensagem) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(nomeDoArquivo, true);
            bw = new BufferedWriter(fw);
            bw.write(mensagem + "\n");
            System.out.println("Escreveu!!");
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public static ArrayList<String> readFile(String nomeArquivo) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader fw = new FileReader(nomeArquivo);
            BufferedReader br = new BufferedReader(fw);
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
}

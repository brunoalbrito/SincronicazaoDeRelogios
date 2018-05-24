/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mack.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class Cliente {

    private boolean estado;
    private Date dataLocal;
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private int porta;
    private String nome;

    public Cliente() {
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getDataLocal() {
        return dataLocal;
    }

    public void setDataLocal(String dataLocal) {

        try {
            this.dataLocal = format.parse(dataLocal);
        } catch (ParseException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

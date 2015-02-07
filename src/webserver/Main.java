/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import static java.lang.Thread.currentThread;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Francisco
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {

            ServerSocket server = new ServerSocket(80);
            System.out.println("Esperando cliente");

            while (true) {
                Socket cliente = server.accept(); // Devuelve el socket resultado de acpetar una peticion, para llevar
                //a cabo la comunicacion con el cliente
                WebServer web = new WebServer(cliente);
                web.start();
            }

        } catch (Exception e) {
            System.out.println(currentThread().toString() + " - " + "Error en servidor\n" + e.toString());
        }

    }

}

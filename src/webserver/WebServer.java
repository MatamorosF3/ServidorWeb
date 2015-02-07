/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Francisco
 */
public class WebServer extends Thread {

    private Socket cliente = null;		// representa la petición de nuestro cliente
    private PrintWriter out = null;		// representa el buffer donde escribimos la respuesta

    WebServer(Socket ps) {
        cliente = ps;
        setPriority(NORM_PRIORITY - 1); // hacemos que la prioridad sea baja

    }

    public void run() // emplementamos el metodo run
    {
        System.out.println(currentThread().toString() + " - " + "Procesamos conexion");
        //InputStream devuelve el canal de lectura del socket
        //OutputStream devuelve el canal de escritura del socket
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            //Tambien se pudo haber utilizado DataInputStream(recibir datos) y DataOutputStream (enviar datos)
            out = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()));

            String linea = ""; // string para leer las lineas
            int i = 0;

            do {
                linea = in.readLine();

                if (linea != null) {
                    // sleep(500);
                    System.out.println(currentThread().toString() + " - " + linea + "-");
                }

                if (i == 0) // aqui averiguamos que archivo hay que busar
                {
                    i++;

                    System.out.println("Cadena Antes de formar tokens:");
                    System.out.println("Cadena: " + linea);
                    // 

                    StringTokenizer st = new StringTokenizer(linea);

                    //   System.out.println("StringTokenizer: " + st);
                    // System.out.println("NextToken: " + st.nextToken());
                    if ((st.countTokens() >= 2) && st.nextToken().equals("GET")) {

                        mostarPagina(st.nextToken());
                    } else {
                        out.println("400 Petición Incorrecta");
                    }
                }

            } while (linea != null && linea.length() != 0);

        } catch (Exception e) {
            System.out.println(currentThread().toString() + " - " + "Error en servidor\n" + e.toString());
        }

        System.out.println(currentThread().toString() + " - " + "Hemos terminado");
    }

    void mostarPagina(String archivo) {
        System.out.println(currentThread().toString() + " - " + "Recuperamos el fichero " + archivo);

        // comprobamos si tiene una barra al principio
        if (archivo.startsWith("/")) {
            archivo = archivo.substring(1); /* si se obtiene una direccion localhost/prueba.html 
             lo que hacemos con el subtring es solo obtener "prueba.html"
             Y así poder saber que archivo local es el que vamos a buscar*/

            System.out.println("Substring: " + archivo);
        }

        try {

            // leemos el archivo y lo mostramos
            File mifichero = new File(archivo);

            if (mifichero.exists()) {
                /* http response header */
                out.println("HTTP/1.0 200 ok");
                out.println("Date: " + new Date());
                out.println("Content-Type: text/html");
                out.println("Content-Length: " + mifichero.length());
                out.println("\n");

                BufferedReader archivoLocal = new BufferedReader(new FileReader(mifichero));

                String linea = "";

                do {
                    linea = archivoLocal.readLine();

                    if (linea != null) {
                        // sleep(500);
                        out.println(linea);
                    }
                } while (linea != null);

                System.out.println("archivo procesado");

                archivoLocal.close();
                out.close();

            } // fin de si el archivox existe 
            else {
                System.out.println("Archivo " + mifichero.toString() + " no encontrado");
                out.println("HTTP/1.0 400 ok");
                out.close();
            }

        } catch (Exception e) {
            System.out.println("Error en archivo");
        }

    }

}

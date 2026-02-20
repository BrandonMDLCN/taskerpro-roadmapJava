/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tasktraker.taskerpro.utils;

import java.io.File;
import java.io.IOException;

public class VerificarOCrearArchivo {
    
    private VerificarOCrearArchivo(){
        throw new IllegalStateException("No existe un constructor para la clase VerificarOCrearArchivo");
    }
    public static void buscarOCrear(String rutaArchivo) {

        // Crear objeto File
        File archivo = new File(rutaArchivo);

        try {
            if (archivo.exists()) {
                System.out.println("✅ El archivo ya existe: " + archivo.getAbsolutePath());
            } else {
                // Intentar crearlo
                if (archivo.createNewFile()) {
                    System.out.println("📄 Archivo creado: " + archivo.getAbsolutePath());
                } else {
                    System.out.println("⚠ No se pudo crear el archivo.");
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error al verificar o crear el archivo: " + e.getMessage());
        }
    }
}
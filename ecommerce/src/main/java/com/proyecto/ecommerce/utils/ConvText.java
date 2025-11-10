package com.proyecto.ecommerce.utils;


public class ConvText {
    private ConvText(){

    }

    public static String toUpperWords(String texto) {

        if (texto == null || texto.isEmpty()) return texto;

        String[] palabras = texto.toLowerCase().split(" ");

        StringBuilder response = new StringBuilder();

        for (String p : palabras) {
            if (!p.isEmpty()) {
                response.append(Character.toUpperCase(p.charAt(0)))
                        .append(p.substring(1))
                        .append(" ");
            }

        }
        return response.toString().trim();
    }
}

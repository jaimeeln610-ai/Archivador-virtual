package com.example;
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
                GestorUI ui = new GestorUI();
                ui.setVisible();
        });
    }
}
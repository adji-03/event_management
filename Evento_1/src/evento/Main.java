/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evento;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class Main extends JFrame{
    public static void main(String[] args) {
        String[] options = {"Home", "Progress"};
        int choix = JOptionPane.showOptionDialog(
            null, 
            "Choisissez l'interface à ouvrir :", 
            "Sélection", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            options, 
            options[0]
        );

        // Ouvrir la fenêtre correspondante
        if (choix == 0) {
            new Home().setVisible(true);
        } else if (choix == 1) {
            new progress().setVisible(true);
            
        }
    }
    }
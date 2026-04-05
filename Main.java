package com.example;
import javax.swing.*;

//main class which runs the program
public class Main {
    public static void main(String[]args){
        SwingUtilities.invokeLater(BudgetBase::createAndShowGUI);
        
    }
    
}

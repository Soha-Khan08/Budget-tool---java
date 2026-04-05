package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import java.util.Stack;

public class BudgetBase extends JPanel {

    //this section declares all the GUI components and the Undo stack
    public JFrame topLevelFrame;
    public GridBagConstraints layoutConstraints = new GridBagConstraints();
    public JTextField wagesField, loansField, otherIncomeField;
    public JComboBox<String> wagesPeriod, loansPeriod, otherIncomePeriod;
    public JTextField foodField, rentField, otherSpendingField;
    public JComboBox<String> foodPeriod, rentPeriod, otherSpendingPeriod;
    public JTextField totalIncomeField, totalSpendingField, surplusField;
    public JButton calculateButton, exitButton, undoButton;
    public Stack<BudgetState> undoStack = new Stack<>();

    private static final String[] PERIODS = { "Weekly", "Monthly", "Yearly" };
    private static final int WEEKS_IN_YEAR = 52;
    private static final int MONTHS_IN_YEAR = 12;

    //this constructor initializes the GUI frame
    public BudgetBase(JFrame frame) {
        this.topLevelFrame = frame;
        setLayout(new GridBagLayout());
        initializeComponents();
        initListeners();
        pushState();
    }
  
     //creates the GUI and displays it
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Budget Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BudgetBase panel = new BudgetBase(frame);
        frame.setContentPane(panel);                       
        frame.pack();                              
        frame.setLocationRelativeTo(null);    
        frame.setVisible(true);                    
    }

    public void initializeComponents() {

        int row = 0;
        addComponent(new JLabel("Income"), row++, 0);

        //adds a wages field to the GUI
        addComponent(new JLabel("Wages:"), row, 0);
        wagesField = new JTextField("0", 10);
        wagesField.setHorizontalAlignment(JTextField.RIGHT);//Aligns the text to the right side
        addComponent(wagesField, row, 1);
        wagesPeriod = new JComboBox<>(PERIODS);//combo box for selcting period
        addComponent(wagesPeriod, row++, 2);

        //adds loans field
        addComponent(new JLabel("Loans:"), row, 0);
        loansField = new JTextField("0", 10);
        loansField.setHorizontalAlignment(JTextField.RIGHT);
        addComponent(loansField, row, 1);
        loansPeriod = new JComboBox<>(PERIODS);
        addComponent(loansPeriod, row++, 2);

        //adds other income field
        addComponent(new JLabel("Other Income:"), row, 0);
        otherIncomeField = new JTextField(" 0", 10);
        otherIncomeField.setHorizontalAlignment(JTextField.RIGHT);
        addComponent(otherIncomeField, row, 1);
        otherIncomePeriod = new JComboBox<>(PERIODS);
        addComponent(otherIncomePeriod, row++, 2);

        addComponent(new JLabel("Spending"), row++, 0);

        //adds food field
        addComponent(new JLabel("Food:"), row, 0);
        foodField = new JTextField(" 0", 10);
        foodField.setHorizontalAlignment(JTextField.RIGHT);
        addComponent(foodField, row, 1);
        foodPeriod = new JComboBox<>(PERIODS);
        addComponent(foodPeriod, row++, 2);

        //adds the rent field
        addComponent(new JLabel("Rent:"), row, 0);
        rentField = new JTextField(" 0", 10);
        rentField.setHorizontalAlignment(JTextField.RIGHT);
        addComponent(rentField, row, 1);
        rentPeriod = new JComboBox<>(PERIODS);
        addComponent(rentPeriod, row++, 2);

        //adds other spending field
        addComponent(new JLabel("Other Spending:"), row, 0);
        otherSpendingField = new JTextField(" 0", 10);
        otherSpendingField.setHorizontalAlignment(JTextField.RIGHT);
        addComponent(otherSpendingField, row, 1);
        otherSpendingPeriod = new JComboBox<>(PERIODS);
        addComponent(otherSpendingPeriod, row++, 2);

        //adds total income fieldd
        addComponent(new JLabel("Total Income:"), row, 0);
        totalIncomeField = new JTextField(" 0", 10);
        totalIncomeField.setHorizontalAlignment(JTextField.RIGHT);
        totalIncomeField.setEditable(false);
        addComponent(totalIncomeField, row++, 1);

        //adds total spending field
        addComponent(new JLabel("Total Spending:"), row, 0);
        totalSpendingField = new JTextField(" 0", 10);
        totalSpendingField.setHorizontalAlignment(JTextField.RIGHT);
        totalSpendingField.setEditable(false);
        addComponent(totalSpendingField, row++, 1);

        //adds surplus/deficit field
        addComponent(new JLabel("Surplus/Deficit:"), row, 0);
        surplusField = new JTextField(" 0", 10);
        surplusField.setHorizontalAlignment(JTextField.RIGHT);
        surplusField.setEditable(false);
        addComponent(surplusField, row++, 1);

        //adds calculate, exit and undo buttons
        calculateButton = new JButton("Calculate");
        addComponent(calculateButton, row, 0);
        exitButton = new JButton("Exit");
        addComponent(exitButton, row, 1);
        undoButton = new JButton("Undo");
        addComponent(undoButton, row++, 2);
        

    }

    //intitializes the listener components
    public void initListeners(){
        exitButton.addActionListener(e -> System.exit(0));

        calculateButton.addActionListener(e -> {
            pushState();
            calculateTotals();
        });

        //undo button action lisener
        undoButton.addActionListener(e -> restoreState());
                JTextField[] allFields = {wagesField, loansField, otherIncomeField, foodField, rentField, otherSpendingField};
        JComboBox<?>[] allCombos = {wagesPeriod, loansPeriod, otherIncomePeriod, foodPeriod, rentPeriod, otherSpendingPeriod};

        //adds focus listener
        for (JTextField field : allFields) {
            field.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    pushState();
                    calculateTotals();
                }
            });
        }

        //adds action listener for the periods combo box
        for (JComboBox<?> combo : allCombos) {
            combo.addActionListener(e -> {
                pushState();
                calculateTotals();
            });
        }
    }   
    
    //adds grib bag layout to the frame (row, column)
    public void addComponent(Component component, int row, int col) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 5, 2, 5); //equal spacing around components
        c.weightx = 1.0;//expands the component horizontally
        this.add(component, c);
    }
 
    //adds grid bag layout (row, column, width)
    public void addComponent(Component component, int row, int col, int width) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = width;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 5, 2, 5);
        c.weightx = 1.0;
        this.add(component, c);
    }

    ////checks for invalid input
    public String getTextFieldValue(JTextField field){
        String text = field.getText().trim();
        if (text.isEmpty()) return "0";

        try{
            Double.parseDouble(text);
            return text;
        } catch (NumberFormatException e) {
            return "INVALID";
        }
    }

    //normalizes the inputs into a yealry amount
    public double normalize(String valueStr, JComboBox<String> periodCombo) {
        if (valueStr.equals("INVALID")) return -1; 

        double value = Double.parseDouble(valueStr);
        String period = (String) periodCombo.getSelectedItem();

        //ocnverts to yearly amount
        if (period.equals("Weekly")) return value * WEEKS_IN_YEAR;
        if (period.equals("Monthly")) return value * MONTHS_IN_YEAR;
        return value; 
    }
    //nomalises inputs and caluclates ottal
    public void calculateTotals() {
        double wages = normalize(getTextFieldValue(wagesField), wagesPeriod);
        double loans = normalize(getTextFieldValue(loansField), loansPeriod);
        double otherIncome = normalize(getTextFieldValue(otherIncomeField), otherIncomePeriod);

        double food = normalize(getTextFieldValue(foodField), foodPeriod);
        double rent = normalize(getTextFieldValue(rentField), rentPeriod);
        double otherSpending = normalize(getTextFieldValue(otherSpendingField), otherSpendingPeriod);

    //checks for invalid inputs
    if (wages < 0 || loans < 0 || otherIncome < 0 ||
        food < 0 || rent < 0 || otherSpending < 0) {
        totalIncomeField.setText("");
        totalSpendingField.setText("");
        surplusField.setText("");
        return;
    }
        //calculations
        double totalIncome = wages + loans + otherIncome;
        double totalSpending = food + rent + otherSpending;
        double surplus = totalIncome - totalSpending;

        //calculates and formats everything to 2 decimal places
        totalIncomeField.setText(String.format("%.2f", totalIncome));
        totalSpendingField.setText(String.format("%.2f", totalSpending));
        surplusField.setText(String.format("%.2f", surplus));
        surplusField.setForeground(surplus < 0 ? Color.RED : Color.GREEN);
    }

    //undo stack methods
    public void pushState() {
        undoStack.push(new BudgetState(
                wagesField.getText(), loansField.getText(), otherIncomeField.getText(),
                foodField.getText(), rentField.getText(), otherSpendingField.getText(),
                wagesPeriod.getSelectedIndex(), loansPeriod.getSelectedIndex(), otherIncomePeriod.getSelectedIndex(),
                foodPeriod.getSelectedIndex(), rentPeriod.getSelectedIndex(), otherSpendingPeriod.getSelectedIndex()
        ));
    }

    //this restores the previous values from the undo stack
    public void restoreState() {
        if (undoStack.size()<=1) {
            return;
        }
        undoStack.pop();
        BudgetState prevState = undoStack.peek();
        prevState.restore();
        calculateTotals();
    }

    //this class stores previous inputes for the undo stack
    public class BudgetState {
        String wages, loans, otherIncome, food, rent, otherSpending;
        int wagesP, loansP, otherIncomeP, foodP, rentP, otherSpendingP;

        //constructor for BudgetState
        BudgetState(String wages, String loans, String otherIncome,
                    String food, String rent, String otherSpending,
                    int wagesP, int loansP, int otherIncomeP,
                    int foodP, int rentP, int otherSpendingP) {
            this.wages = wages; this.loans = loans; this.otherIncome = otherIncome;
            this.food = food; this.rent = rent; this.otherSpending = otherSpending;
            this.wagesP = wagesP; this.loansP = loansP; this.otherIncomeP = otherIncomeP;
            this.foodP = foodP; this.rentP = rentP; this.otherSpendingP = otherSpendingP;
        }

        //restores the preious vvalues
        void restore() {
            wagesField.setText(wages);
            loansField.setText(loans);
            otherIncomeField.setText(otherIncome);
            foodField.setText(food);
            rentField.setText(rent);
            otherSpendingField.setText(otherSpending);
            
            wagesPeriod.setSelectedIndex(wagesP);
            loansPeriod.setSelectedIndex(loansP);
            otherIncomePeriod.setSelectedIndex(otherIncomeP);
            foodPeriod.setSelectedIndex(foodP);
            rentPeriod.setSelectedIndex(rentP);
            otherSpendingPeriod.setSelectedIndex(otherSpendingP);
        }
    }

}

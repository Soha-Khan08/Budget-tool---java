Budget Tool (Java Basic System)
This Java‑based budget tool allows users to manage their personal finances by entering multiple income and expenditure fields. Users can input at least three income sources (e.g., wages, loans, other) and three spending categories (e.g., food, rent, other). When the Calculate button is pressed, the system computes the total income, total spending, and displays the resulting surplus or deficit. Surplus values (income ≥ spending) are shown in black, while deficits (income < spending) are highlighted in red.

The system validates user input to ensure only numbers are accepted, producing error messages for invalid entries. Empty fields are treated as zero without errors.

Extensions:

Users can specify whether amounts are weekly, monthly, or yearly. Totals are adjusted accordingly using standard conversions (52 weeks/year, 12 months/year, 4.333 weeks/month).

Spreadsheet‑like behavior is implemented: totals update automatically whenever a value or time period changes, without needing to press Calculate.

Undo Functionality:

A single‑level undo allows reversal of the most recent action by restoring the previous state.

Multiple undo is supported using a stack of saved states, enabling users to step back through several changes.

JUnit tests are included to verify undo operations.

Programming Style:  
The code follows good practices with clear variable names, comments, and modular methods for readability and maintainability.

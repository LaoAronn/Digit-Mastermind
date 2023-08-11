
import java.util.Scanner;
import java.lang.Math;

public class MastermindGame{

    final static char INSTRUCTIONS = 'A';
    final static char NORMAL = 'B';
    final static char CHEAT = 'C';
    final static char EXIT = 'D';
    final static int ATTEMPTLIMIT = 5;

    static int totalGuess = 0;
    static int totalGamePlayed = 0;
    

    public static void main(String[] args){

        gameMenu();

    }

    // printIdentification
    private static void printIdentification() {
     System.out.println("Homework: Mastermind Game   Author: Aronn");
     System.out.println("### Course/Section - CPSC1150- 003");
     System.out.println("### St.# - 100370879 ###\n");
     System.out.println();
    } 

    

    // Main game menu
    private static void gameMenu(){
        Scanner input = new Scanner(System.in);
        char userChoice = ' ';

        while (userChoice != EXIT){

            displayMenu();
            userChoice = input.next().toUpperCase().charAt(0);

            // Checking the choice for user
            while(!isValidInput(userChoice)){
                
                displayMenu();
                System.out.println("--Please enter a proper selection--");
                userChoice = input.next().toUpperCase().charAt(0); 

            }

            if (userChoice == INSTRUCTIONS) {
                displayInstruction();
            } else if (userChoice == NORMAL) {
                startGame(false);
            } else if (userChoice == CHEAT) {
                startGame(true);
            }

            
        }

        System.out.println("-----Thanks for Playing!-----");
    }

    // Game Menu Choices
    private static void displayMenu(){
        System.out.println("<----------------------->");
        System.out.println("<----Digit Mastermind---->");
        System.out.println("<----------------------->");
        System.out.println("A) Instructions");
        System.out.println("B) Normal Mode (Unlimited tries, no hints)");
        System.out.println("C) Cheat Mode (Given correct digits at correct place but limited to 5 tries only)");
        System.out.println("D) Exit");
        System.out.println("Please Choose an option: ");
    }

    // Game Instructions
    private static void displayInstruction(){
        System.out.println("\n\n<---------INSTRUCTIONS--------->");
        System.out.println("You will need to provide an amount of digits that should be guessed (not more than 10, but at least 2.)");
        System.out.println("Then you must keep guessing until you get all the digits correct. Good Luck!");
        System.out.println("<------------------------------->\n\n");
    }

    // Normal Game
    private static void startGame(boolean isCheat){

        // Checking for User choice for gamemode
        Scanner input = new Scanner(System.in);
        int digits = -1;
        boolean runItBack = true;

        System.out.println("How many digits would you like to guess? (2-10)");
        digits = input.nextInt();

        while(digits < 2 || digits > 10){
            System.out.println("Please give proper digits (from 2 to 10) ");
            digits = input.nextInt();
        }

        while (runItBack){

            // Game Variables
            boolean isWinner = false;
            int userAttempt = 1;
            int guessWrong;
            char userDecision;
            int[] gameDigits = randomDigits(digits);

            // printArray(gameDigits); /* ONLY UN-HIGHLIGHT FOR TESTING */

            int[] userGuess = getUserGuess(digits);

            // While the userGuess does not match at all
            while (!isWinner) {
                
                // Cheater Mode: If user doesnt finish the game in 5 attempts
                if ( isCheat == true && userAttempt == ATTEMPTLIMIT ){
                    isWinner = false;
                } else {
                    guessWrong = compareUserGuess(gameDigits, userGuess);

                    // If the differences of user guess and actual digits are 0, set to winner
                    if (guessWrong == 0) {
                        isWinner = true;
                    } else { 
                        
                        // Output the results of his attempt
                        System.out.println( getMatchingDigits(gameDigits, userGuess)-(gameDigits.length - guessWrong) + " digits is/are correct and in the wrong place.");
                        System.out.println( (gameDigits.length - guessWrong) + " digits is/are correct and in the correct place.");

                        if (isCheat == true){
                            System.out.println("\n[CHEAT MODE]");
                            System.out.println( "You have " + (ATTEMPTLIMIT-userAttempt) + " tries left");

                            // First Hint: Providing the Digits that they got correct
                            System.out.print("Digit Hints: " + giveHint(gameDigits, userGuess) + "\n");

                            // Second Hint: After 3 attempts, give them the sum of all digits
                            if (userAttempt >= 3){
                                System.out.println("The sum of all the digits is " + getSum(gameDigits));
                            }
                        
                        }

                        System.out.println( "Give Up? (Yes / No)");
                        userDecision = userGiveUp();

                        // If User doesnt give up, keep going!
                        if (userDecision == 'N'){
                            // Prompt for another guess
                            userGuess = getUserGuess(digits);
                            // User will need to make another guess
                            userAttempt++;

                        } else { // If User gives up
                            isWinner = true;
                        }

                    }

                }
                

            }

            // Produce game stats
            System.out.println("Game Over! You have made " + userAttempt + " guesses.");
            System.out.print("The computer answers are: ");
            printArray(gameDigits);

            // Accumulating overall game statistics
            totalGuess += userAttempt;
            totalGamePlayed++;

            // Ask if user would like to run it back
            System.out.println( "\nWould you like to try again? (Yes / No)");
            userDecision = userGiveUp();
            // If user chooses to stop
            if (userDecision == 'N'){
                runItBack = false;

                // Produce final results
                System.out.println("----------FINAL----------");
                System.out.println("Among " + totalGamePlayed + " games played, you have averaged " + (totalGuess/totalGamePlayed) + " per game.");
                System.out.println("-------------------------\n");

                if ( (totalGuess/totalGamePlayed) >= 5){
                    System.out.println("WOW! You have bad luck...best of luck next time!\n");
                }
                
            }

        }
        

    }

    private static int getMatchingDigits(int[] digitAnswer, int[] userGuess){
        int matchingDigit = 0;

        for (int i = 0; i < digitAnswer.length; i++){
            for (int j = 0; j < userGuess.length; j++){
                if (digitAnswer[i] == userGuess[j]){
                    matchingDigit++;
                }
            }
        }

        return matchingDigit;
    }

    private static int getSum(int[] intArray){
        int sum = 0;

        for (int i = 0; i < intArray.length; i++){
            sum += intArray[i];
        }

        return sum;
    }

    private static String giveHint(int[] gameAnswer, int[] userGuess){

        String result = "";

        for (int i = 0; i < gameAnswer.length; i++){

            // if the user guesses it right, show it to them
            if (gameAnswer[i] == userGuess[i]){
                result += gameAnswer[i] + " ";
            } else {
                result += "_ ";
            }
        }

        return result;
    }

    // Checking for User if they give up or not by typing Yes or No
    private static char userGiveUp(){
        Scanner input = new Scanner(System.in);
        
        char userDecision = input.next().toUpperCase().charAt(0);

        while (userDecision != 'Y' && userDecision != 'N'){
            System.out.println("Please enter a proper choice. (Yes / No) ");
            userDecision = input.next().toUpperCase().charAt(0);
        }

        return userDecision;
    }

    // Compare computer digits and user guess difference
    private static int compareUserGuess(int[] computerDigit, int[] userDigit){
        int digitDifference = 0;

        for (int i = 0; i < computerDigit.length; i++){
            // If the user guess doesnt match with the computers random digit
            if (computerDigit[i] != userDigit[i]) {
                digitDifference++;
            }
        }

        return digitDifference;
    }

    // Asking user to make a guess
    private static int[] getUserGuess(int size){

        Scanner input = new Scanner(System.in);
        int[] digitGuess = new int[size];
        int userGuess;
        int digitsGuessed = 0;
        boolean isDigitRepeated = false;


        for(int i = 0; i < size; i++){

            System.out.print("Digit #" + (i+1) + ": ");
            userGuess = input.nextInt();

            // Checking if the digits are repeated or not
            if (digitsGuessed > 0){

                // Checking if the digits have been replicated
                for(int j = 0; j < digitsGuessed; j++){
                    if(digitGuess[j] == userGuess){
                        isDigitRepeated = true; // Set to true to fix it
                    }
                }

                while (isDigitRepeated ) {
                    System.out.println("Digit should not be repeated and from 0 to 9. Try again");
                    System.out.print("Digit #" + (i+1) + ": ");
                    userGuess = input.nextInt();
                    isDigitRepeated = false;

                    // Checking if the digits have been replicated
                    for(int j = 0; j < digitsGuessed; j++){
                        if(digitGuess[j] == userGuess){
                            isDigitRepeated = true; // Set to true to fix it
                        }
                    }
                    
                    
                }

            } else {

                if ( userGuess < 0 && userGuess > 9 ) {
                    System.out.println("Digit should not be over 9 or under 0, Try again");
                    System.out.print("Digit #" + (i+1) + ": ");
                    userGuess = input.nextInt();
                }

            }


            digitGuess[i] = userGuess;
            digitsGuessed++;
        }

        return digitGuess;

    }

    

    // Setting random variables for the user to guess
    private static int[] randomDigits(int size){
        int[] finalArray = new int[size];
        int digitsDeclared = 0;
        boolean isDigitRepeated = true;

        // First digit
        int randomDigit = (int)(Math.random() * 10);
        finalArray[0] = randomDigit;
        digitsDeclared++;

        // Dealing with the rest of the digits
        for(int i = 1; i < size; i++){

            // Checking if the digits are repeated or not
            while (isDigitRepeated) {
                randomDigit = (int)(Math.random() * 10);
                isDigitRepeated = false;

                // Checking if the digits have been replicated
                for(int j = 0; j < digitsDeclared; j++){
                    if(finalArray[j] == randomDigit){
                        isDigitRepeated = true; // Set to true to fix it
                    }
                }
                
            }

            finalArray[i] = randomDigit;
            digitsDeclared++;
            isDigitRepeated = true;
        }

        return finalArray;
    }

    private static void printArray(int[] intArray){
        for (int i = 0; i < intArray.length; i++){
            System.out.print(intArray[i] + " ");
        }
    }


    // Checking user choice in the game Menu
    private static boolean isValidInput(char character) {
      
        if (character == INSTRUCTIONS || character == NORMAL || character == CHEAT || character == EXIT ){
            return true;
        } else {
            return false;
        }
            
    } 



}
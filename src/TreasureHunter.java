import java.awt.*;
import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean samuraiMode;
    public static OutputWindow window = new OutputWindow();

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
        samuraiMode = false;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        window.addTextToWindow("Welcome to TREASURE HUNTER!" + "\n", Colors.brown);
        window.addTextToWindow("Going hunting for the big treasure, eh?" + "\n", Colors.brown);
        window.addTextToWindow("What's your name, Hunter? " + "\n", Color.BLACK);
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, 20);

        window.addTextToWindow("Easy, normal, or hard mode? (e/n/h): " + "\n", Color.BLACK);
        String hard = SCANNER.nextLine().toLowerCase();
        if (hard.equals("e")) {
            easyMode = true;
        } else if (hard.equals("h")) {
            hardMode = true;
        }else if (hard.equals("test")) {
            hunter = new Hunter(name, 100);
            hunter.testMode();
        } else if (hard.equals("test lose")) {
            hardMode = true;
            hunter = new Hunter(name, 1);
        } else if (hard.equals("s")){
            samuraiMode = true;
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;
            // and the town is "tougher"
            toughness = 0.75;
        } else if (easyMode) {
            markdown = 1;
            toughness = 0.2;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, samuraiMode);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, samuraiMode);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);

    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x")) {
            window.addTextToWindow(currentTown.getLatestNews() + "\n", Colors.salmon);
            window.addTextToWindow("***" + "\n", Color.darkGray);
            window.addTextToWindow(hunter.infoString() + "\n", Colors.pastelBlue);
            window.addTextToWindow(currentTown.infoString() + "\n", Colors.pastelGreen);
            window.addTextToWindow("(B)uy something at the shop." + "\n", Color.GRAY);
            window.addTextToWindow("(S)ell something at the shop." + "\n", Color.GRAY);
            window.addTextToWindow("(E)xplore surrounding terrain." + "\n", Color.GRAY);
            window.addTextToWindow("(M)ove on to a different town." + "\n", Color.GRAY);
            window.addTextToWindow("(L)ook for trouble!" + "\n", Color.GRAY);
            window.addTextToWindow("(H)unt for treasure!" + "\n", Color.GRAY);
            window.addTextToWindow("(D)ig for gold!" + "\n", Color.GRAY);
            window.addTextToWindow("Give up the hunt and e(X)it." + "\n", Color.GRAY);
            window.addTextToWindow("\n", Color.GRAY);
            window.addTextToWindow("What's your next move? " + "\n", Color.GRAY);
            choice = SCANNER.nextLine().toLowerCase();
            choice = processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private String processChoice(String choice) {
        window.clear();
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("e")) {
            window.addTextToWindow(currentTown.getTerrain().infoString() + "\n", Colors.salmon);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                window.addTextToWindow(currentTown.getLatestNews() + "\n", Colors.salmon);
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
            if (hunter.getHunterGold() < 0) {
                window.addTextToWindow("\n", Color.GRAY);
                window.addTextToWindow(currentTown.getLatestNews() + "\n", Colors.salmon);
                window.addTextToWindow("\n", Color.GRAY);
                window.addTextToWindow("You don't have enough gold to pay!" + "\n", Color.RED);
                window.addTextToWindow("Fare thee well, " + hunter.getHunterName() + "!" + "\n", Color.red);
                return "x";
            }
        } else if (choice.equals("h")) {
            currentTown.lookForTreasure();
            if (hunter.checkTreasures()) {
                window.addTextToWindow("Congratulations, you have found the last of the three treasures, you win!" + "\n", Color.RED);
                return "x";
            }
        } else if (choice.equals("d")) {
            currentTown.digForGold();
        } else if (choice.equals("x")) {
            window.addTextToWindow("Fare thee well, " + hunter.getHunterName() + "!" + "\n", Color.red);
            return "x";
        } else {
            window.addTextToWindow("Yikes! That's an invalid option! Try again." + "\n", Color.red);
        }
        return "";
    }
}
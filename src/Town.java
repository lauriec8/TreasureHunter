import java.awt.*;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String[] treasures = {"crown", "trophy", "gem", "dust"};
    private String townTreasure;
    private boolean searched = false;
    private boolean dug = false;
    private boolean easyTown;
    private boolean samuraiMode;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean samuraiMode) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        easyTown = (toughness == 0.2);

        this.samuraiMode = samuraiMode;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    public void resetNews(){
        printMessage = "";
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
        townTreasure = treasures[(int) (Math.random() * 4)];
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak(easyTown)) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else if (easyTown){
            noTroubleChance = 0.2;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = "You go into the sketchy part of town at night looking for someone to pick a fight with";
            printMessage += "\nFrom the shadows, you see a shiny toothy grin glimmering.. You found someone to fight";
            boolean wonBrawl = false;
            if (hunter.hasItemInKit("sword")){
                boolean scared = Math.random() > 0.5;
                if (scared) {
                    printMessage += "\nYou begin to unsheathe your sword, the dim street light reflecting to reveal the opponent's face";
                    printMessage += "\nTheir face was ghastly pale with fear and immediately fled the scene, leaving all their gold";
                } else {
                    printMessage += "\nYou draw your sword and swung at them, but with no defenses against a sword, they surrendered";
                }
                wonBrawl = true;
            } else {
                printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
                if (Math.random() > noTroubleChance) {
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    wonBrawl = true;
                } else {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                }
            }
            printMessage += "\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (wonBrawl) {
                printMessage += "\nYou won the brawl and receive " + goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                hunter.changeGold(-goldDiff);
            }
        }
    }

    public void lookForTreasure() {
        if (searched) {
            TreasureHunter.window.addTextToWindow("You have already searched this town!" + "\n", Color.red);
        } else {
            if (townTreasure.equals("dust")) {
                TreasureHunter.window.addTextToWindow("You found dust!" + "\n", Colors.smokyBlue);
            } else {
                TreasureHunter.window.addTextToWindow("You found a " + townTreasure + "!" + "\n", Colors.smokyBlue);
                if (!hunter.addTreasure(townTreasure)) {
                    TreasureHunter.window.addTextToWindow("You have already collected this treasure!" + "\n", Colors.smokyBlue);
                }
            }
        }
        searched = true;
    }

    public void digForGold() {
        if (!dug) {
            if (hunter.hasItemInKit("shovel")) {
                dug = true;
                if (Math.random() > 0.5) {
                    TreasureHunter.window.addTextToWindow("You dug up " + (int) (Math.random() * 20 + 1) + " gold!" + "\n", Colors.smokyBlue);
                } else {
                    TreasureHunter.window.addTextToWindow("You dug but only found dirt." + "\n", Colors.brown);
                }
            } else {
                TreasureHunter.window.addTextToWindow("You can't dig for gold without a shovel!" + "\n", Color.red);
            }
        } else {
            TreasureHunter.window.addTextToWindow("You already dug for gold in this town." + "\n", Color.red);
        }
    }

    public String infoString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        int rnd = (int) (Math.random() * 6) + 1;
        if (rnd == 1) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd == 2) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd == 3) {
            return new Terrain("Plains", "Horse");
        } else if (rnd == 4) {
            return new Terrain("Desert", "Water");
        } else if (rnd == 5){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak(boolean easyTown) {
        if (easyTown){
            return false;
        } else {
            double rand = Math.random();
            return (rand < 0.5);
        }
    }
}
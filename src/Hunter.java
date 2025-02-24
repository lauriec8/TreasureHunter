/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private String[] kit;
    private String[] treasures;
    private int gold;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     * @param startingGold The gold the hunter starts with.
     */
    public Hunter(String hunterName, int startingGold) {
        this.hunterName = hunterName;
        kit = new String[8]; // only 5 possible items can be stored in kit <- upgraded to 6 to fit boots
        treasures = new String[4];
        gold = startingGold;
    }

    //Accessors
    public String getHunterName() {
        return hunterName;
    }

    public int getHunterGold() {
        return gold;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
    }

    public void testMode(){
        kit = new String[] {"water", "rope", "machete", "horse", "boat", "boots", "shovel"};
        treasures = new String[] {"gem", "crown", null};
    } //updated to fit TH-4

    public boolean checkTreasures() {
        if (hasItemInKit("crown", treasures) && hasItemInKit("trophy", treasures) && hasItemInKit("gem", treasures)) {
            return true;
        }
        return false;
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (costOfItem < 0) {
            return false;
        }
        if (gold < costOfItem || hasItemInKit(item, kit)){
            return false;
        }
        if (item.equals("sword")) {
            addItem(item);
            return true;
        }
        if (hasItemInKit("sword")){
            addItem(item);
            return true;
        }
        gold -= costOfItem;
        addItem(item);
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item, kit)) {
            return false;
        }
        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    public boolean addTreasure(String treasure) {
        if (!hasItemInKit(treasure, treasures)) {
            int idx = emptyPositionInKit(treasures);
            treasures[idx] = treasure;
            return true;
        }
        return false;
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item) {
        if (!hasItemInKit(item, kit)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }
        return false;
    }

    /**
     * Checks if the kit Array has the specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item, String[] array) {
        for (String tmpItem : array) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

     /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
    public String getInventory(String[] array) {
        String printableKit = "";
        String space = " ";

        for (String item : array) {
            if (item != null) {
                printableKit += item + space;
            }
        }
        return printableKit;
    }

    public String getInventory() {
        String printableKit = "";
        String space = " ";

        for (String item : kit) {
            if (item != null) {
                printableKit += item + space;
            }
        }
        return printableKit;
    }

    /**
     * @return A string representation of the hunter.
     */
    public String infoString() {
        String str = hunterName + " has " + gold  + " gold";
        if (!kitIsEmpty()) {
            str += " and " + getInventory();
        }
        if (!treasuresIsEmpty()) {
            str += "\nTreasures found: " + getInventory(treasures);
        } else {
            str += "\nTreasures found: none";
        }
        return str;
    }

    /**
     * Searches kit Array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @return true if kit is completely empty.
     */
    private boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    private boolean treasuresIsEmpty() {
        for (String string : treasures) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first index where there is a null value.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private int emptyPositionInKit(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
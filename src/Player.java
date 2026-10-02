import java.util.ArrayList;

public class Player {
    private Room currentroom;
    private ArrayList<Item> inventory = new ArrayList<>();
    private int health = 100;
    private Weapon equippedWeapon;


    public Player(Room startRoom) {
        this.currentroom = startRoom;
        this.equippedWeapon = null;
    }

    //health getter og setter metode

    public int getHealth() {
        return health;
    }

    //ekstra metode bare hvis man skal sætte health
    public void setHealth(int health) {
        this.health = health;
    }

    public EatResult eat(String itemName) {
        Item foundItem = null;

        //Led først i dit inventory
        for (Item item : inventory) {
            if (item.getShortName().equalsIgnoreCase(itemName)) {
                foundItem = item;
                break;
            }
        }

        //Hvis det ikke ligger i inventory, så led i det nuværende rum
        if (foundItem == null) {
            foundItem = currentroom.findItems(itemName); //Bruger søgemetode fra Room klassen
        }


        //Udfald 1: Tingen slet ikke findes hverken som Food-objektet eller bare objekt
        if (foundItem == null) {
            return EatResult.NOT_FOUND;
        }
        //Udfald 2 og 3: Tingen findes! nu tjekker vi, om det food

        // Hvis foundItem er en food, så gem variablen 'foodToEat' med det samme i stedet for at bruge casting - Food foodToEat = (Food) foundItem;
        if (foundItem instanceof Food foodToEat) {

            //Spilleren spiser maden og får health/mister health
            //Udfald 3: Tingen findes og er mad

            //+= bruges til at lægge til, så f.eks. 100 + 10 = 110 - health stiger
            //110 + (-50) = 110-50 = 60 - health falder
            this.health += foodToEat.getHealthPoints();

            //Fjern maden fra inventory og currentroom
            if (inventory.contains(foodToEat)) {
                inventory.remove(foodToEat); //fjerner maden fra inventory
            } else {
                currentroom.removeItem(foodToEat); //fjerner fra Currentroom
            }
            return EatResult.EATEN;

        } else {
            //Udfald 2: Tingen findes, men er ikke et Food-objekt
            return EatResult.NOT_FOOD;
        }
    }

    //Equip metoden
    public String equip(String itemName) {
        Item item = findItemsInInventory(itemName); //finder itemet i inventory

        if (item == null) {
            return "You don't have " + itemName + " in your inventory.";
        }
        Weapon weapon = item.isWeapon();
        if (weapon != null) {
            //Hvis man allerede har et våben udrustet, lægger vi det tilbage i inventory først
            if (this.equippedWeapon != null){
                this.inventory.add(this.equippedWeapon); //læg våbnet tilbage

            }
            this.equippedWeapon = weapon; //gemmer våbnet i varablen
            this.inventory.remove(item); //fjen våbnet fra inventory, så det ikke er begge steder
            return "You have equipped the " + item.getShortName() + ".";
        } else {
            return "You cannot equip the " + item.getShortName() + "!";
        }
    }

    public String getEquippedWeaponName(){
        if (this.equippedWeapon != null){
            return this.equippedWeapon.getShortName();
        }
        return null; //returnerer null (ingenting) hvis man ikke har et våben på sig
    }

    public String attack() {
        if (this.equippedWeapon == null) {
            return "You do not have a weapon equipped!";
        }
        //kalder på enten MeleeWeapon eller RangedWeapon
        return this.equippedWeapon.attack();
    }




    //Item-metoder og inventory metoder

    //Søgemetode i spillerens inventory
    public Item findItemsInInventory(String itemName){
        for (Item item : inventory){
            if (item.getShortName().equalsIgnoreCase(itemName) || item.getLongName().toLowerCase().contains(itemName.toLowerCase())) {
                return item;
            }
        }
        return null; //Itemet blev ikke fundet i inventory
    }


    //Denne søgemetode inkluderer currentRoom med itemsøgning
    public Item findItemsAnywhere(String itemName){
        for (Item item : inventory){
            if (item.getShortName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return currentroom.findItems(itemName);
    }

    public Item takeItem(String itemName){
        Item item = currentroom.findItems(itemName);
        if (item != null){
            currentroom.removeItem(item); //fjernet fra rummet / currentroom
            inventory.add(item); //blev tilføjet til spillerens inventory
        }
        return item; //returnerer objektet eller null
    }


    public Item dropItem(String itemName){
        //Hvis spileren dropper sit udrustede våben, bliver det lagt inventory og dermed i findItemsInInventory
        if (this.equippedWeapon != null && (this.equippedWeapon.getShortName().equalsIgnoreCase(itemName) || this.equippedWeapon.getLongName().toLowerCase().contains(itemName.toLowerCase()))) {
            this.inventory.add(this.equippedWeapon);
            this.equippedWeapon = null;
        }

        Item item = findItemsInInventory(itemName);
        if (item != null){
            inventory.remove(item); //Fjern item fra spillerens inventory
            currentroom.addItem(item); //Tilføjet/droppet tl rummet / currentroom
        }
        return item; //returnerer objektet / null
    }



    public ArrayList<Item> getInventory(){
        return inventory;
    }


    //Room og move
    public Room getCurrentroom(){
        return currentroom;
    }

    public String getCurrentRoomName(){
        return currentroom.getName();
    }

    public String getCurrentRoomDescription(){
        return currentroom.getDescription();
    }

    //Håndterer bevægelse/korsør. Returnerer true, hvis det lykkedes, og false hvis det er en mur (null)
    public boolean move(String direction){
        Room nextRoom = null;

        //Modtager kun de fulde retninger fra UI/Controller
        switch (direction){
            case "north", "n" -> nextRoom = currentroom.getNorth();
            case "south", "s" -> nextRoom = currentroom.getSouth();
            case "east", "e" -> nextRoom = currentroom.getEast();
            case "west", "w" -> nextRoom = currentroom.getWest();
        }

        if (nextRoom != null){
            currentroom = nextRoom; //Husk hvor vi står lige nu
            return true; //flytning lykkedes
        }
        return false; //Der er ikke noget at gå tilbage til endnu /blindgyde
    }

}

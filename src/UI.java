import java.util.ArrayList;

public class UI {
    Adventure game = new Adventure();
    boolean running = true;

    public void startGame() {
        //Velkomstbesked og beskrivelse af start-rummet
        IO.println("Welcome to the Adventure Game!");
        IO.println("Commands: go north/south/east/west (or just n/s/e/w), look, help, inventory, health, take <item>, drop <item>, eat <food>, equip <weapon> exit.");
        IO.println("----------------------------------------------------------------------------");
        printRoomInfo();

        while (running) {
            //Vis et simpelt prompt-tegn og læs input fra brugeren i små bogstaver.
            String input = IO.readln("> ").toLowerCase().trim();


            //Tjek for intenting. Hvis bruger bare trykker Enter
            if (input.isEmpty()) {
                IO.println("You must type a command! Type 'help' for list of commands.");
                continue; //Springer resten af koden over og starter forfra i løkken
            }

            //Hvis spilleren skriver f.eks. 'go east', fjerner vi 'go ' så der står 'east'
            if (input.startsWith("go ")) {
                input = input.substring(3).trim();
            }
            //Håndtering af kommandoer
            switch (input) {
                case "take", "t" -> IO.println("What do you want to take? (e.g., 'take lamp')");
                case "drop", "d" -> IO.println("What do you want to drop? (e.g., 'drop lamp')");
                case "eat" -> IO.println("What do you want to eat? (e.g., 'eat bread')");
                case "equip" -> IO.println("What weapon do you want to equip? (e.g., 'equip revolver')");
                case "attack" -> handleAttack();
                case "look" -> printRoomInfo();
                case "inventory", "inv" -> printInventory();
                case "health", "h" -> printPlayerHealth();
                case "help" -> printHelp();
                case "exit" -> {
                    IO.println("Game Over");
                    running = false;
                }

                //Retninger
                case "north", "n" -> handleMove("north");
                case "south", "s" -> handleMove("south");
                case "east", "e" -> handleMove("east");
                case "west", "w" -> handleMove("west");

                //Skraldspand: Alt andet sendes herned til tjek
                default -> handleComplexCommands(input);
            }
        }
    }


    private void handleMove(String direction){
        boolean success = game.movePlayer(direction);
        if (success) {
            printRoomInfo();
        } else {
            IO.println("Ouch! You hit a wall. You cannot go that way");
        }
    }
    private void handleTake(String itemName){
        Item takenItem = game.takeItem(itemName);
        if (takenItem != null){
            IO.println("You have taken the " + takenItem.getShortName());
        } else {
            IO.println("There is nothing like " + itemName + " to take around here.");
        }
    }


    private void handleDrop(String itemName){
        Item droppedItem = game.dropItem(itemName);
        if (droppedItem != null){
            IO.println("You have dropped the " + droppedItem.getLongName());
        } else {
            IO.println("You don't have anything like " + itemName + " in your inventory.");
        }
    }

    private void handleEat(String itemName){
        //finder vi itemet først, så vi kan aflæse longName og healthPoints
        Item item = game.findItemInGame(itemName);

        //Kør enum Eat-logikken
        EatResult result = game.eatItem(itemName);

        switch (result) {
            case NOT_FOUND -> IO.println("There is nothing like " + itemName + " to eat around here.");
            case NOT_FOOD -> {
                if (item != null){
                    IO.println("You cannot eat the " + item.getLongName());
                }
            }
            case EATEN -> {
                // Fordi vi fandt 'item' før den blev slettet fra listen (inventory.remove eller item.remove), kan vi tjekke dens Food-egenskaber nu!
                if (item instanceof Food food){
                    //Klipper vi 'a' hvis det står i starten af teksten
                    String foodDescription = food.getLongName();
                    if (foodDescription.startsWith("a ")) {
                        foodDescription = foodDescription.replace("a ", "");
                        // Tjek om det er sundt eller giftigt (plus eller minus point)
                        if (food.getHealthPoints() > 0){
                            IO.println("You eat the " + foodDescription + ". You feel a little better.");
                        } else {
                            IO.println("You eat the " + foodDescription + ". That was a mistake.");
                        }
                    }
                }
                printPlayerHealth(); //Vis deres nye health status med det samme
            }
        }
    }

    private void handleEquip(String itemName){
        //Kalder på Adventure, som kalder på Player og returner teksten
        String result = game.equipWeapon(itemName);
        IO.println(result);
    }

    private void handleAttack(){
        //Kalder på Adventure, som kalder på Player
        String result = game.attackWithWeapon();
        IO.println(result);
    }

    private void printInventory(){
        //Viser ens inventory
        ArrayList<Item> inventory = game.getPlayerInventory();

        if (inventory.isEmpty()){
            IO.println("You are not carrying anything right now.");
        } else {
            IO.println("You are carrying: ");
            for (Item item : inventory){
                IO.println("- " + item.getLongName());
            }
        }

        //Viser equipped weapon i inventory
        String equippedName = game.getEquippedWeaponName();
        IO.println("----------------");
        if (equippedName != null){
            IO.println("Equipped weapon: " + equippedName);
        } else {
            IO.println("Equipped weapon: None");
        }
        IO.println(" ");
    }

    private void printPlayerHealth(){
        int currentHealth = game.getPlayerHealth();

        if (currentHealth >= 100){
            IO.println("Health: " + currentHealth + " - you are in perfect health");
        } else if (currentHealth >= 50){
            IO.println("Health: " + currentHealth + " - you are in good health, but avoid fighting right now");
        } else {
            IO.println("Health: " + currentHealth + " - you are in poore health. Find some food quickly!");
        }
    }


    private void printHelp(){
        IO.println("\n--- (Help Menu) ---");
        IO.println("Move: 'south', 'north', 'west', 'east'");
        IO.println("Look: See the room description and items again");
        IO.println("Take/Drop <item>: 'take lamp, 'drop book'");
        IO.println("Inventory: Type 'inventory' or 'inv' to see your items.");
        IO.println("Health: type 'health' or 'h' to see your health.");
        IO.println("Equip <weapon>: 'equip sword', 'equip knife'");
        IO.println("Attack: type 'attack' to use your weapon");
        IO.println("Eat <food>: 'eat mushroom' or 'eat bread' increases or decreases your health.");
        IO.println("Exit: Quit the game.");
    }

    private void printRoomInfo() {
        IO.println("You are in " + game.getCurrentRoomName());
        IO.println(game.getCurrentRoomDescription());
        IO.println();

        //Vis item i rummet
        ArrayList<Item> roomItems = game.getCurrentRoomItems();
        if (!roomItems.isEmpty()){
            IO.print("Here you see: ");
            for (int i = 0; i < roomItems.size(); i++){
                IO.print(roomItems.get(i).getLongName());

                //hvis der er mere end et item i rummet
                if (i < roomItems.size() -1){
                    //Hvis vi står ved det næstsidste item, sætter vi ", and" inden det sidste
                    if (i == roomItems.size() - 2) {
                        IO.print(", and ");
                    } else {
                        IO.print(", "); //Ellers sætter vi bare almindelig komma
                    }
                }
            }
            IO.println("");
        }
    }

    private void handleComplexCommands(String input) {
        //Håndtering af input som "take lamp" eller "drop <item>"
        if (input.startsWith("take ") || input.startsWith("t ")) {
            String itemName = parseItemName(input);
            handleTake(itemName);
        } else if (input.startsWith("drop ") || input.startsWith("d ")) {
            String itemName = parseItemName(input);
            handleDrop(itemName);
        } else if (input.startsWith("eat ") || input.startsWith("e ")) {
            String itemName = parseItemName(input);
            handleEat(itemName);
        } else if (input.startsWith("equip ")) {
            String itemName = parseItemName(input);
            handleEquip(itemName);
        } else if (input.startsWith("attack ") || input.startsWith("a ")) {
            handleAttack();
        } else {
            IO.println("Unknown command. Type 'help' for a lists of commands.");
        }
    }


    //Klipper bare alt efter det første mellemrum af
    private String parseItemName(String input) {
        int firstSpace = input.indexOf(" ");
        if (firstSpace != -1){
            return input.substring(firstSpace + 1).trim();
        }
        return "";
    }
}



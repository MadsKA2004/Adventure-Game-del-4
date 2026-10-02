import java.util.ArrayList;

public class Adventure {
    private Map map;
    private Player player;

    public Adventure(){
        this.map = new Map();
        this.player = new Player(map.getStartRoom());
    }

    //Item metoder

    public Item takeItem(String itemName){
        return player.takeItem(itemName);
    }

    public Item dropItem(String itemName){
        return player.dropItem(itemName);
    }

    public ArrayList<Item> getPlayerInventory(){
        return player.getInventory();
    }

    public ArrayList<Item> getCurrentRoomItems(){
        return player.getCurrentroom().getItems();
    }

    public String getCurrentRoomName(){
        return player.getCurrentRoomName();
    }

    public String getCurrentRoomDescription(){
        return player.getCurrentRoomDescription();
    }

    public boolean movePlayer(String direction){
        return player.move(direction);
    }

    //Health - getPlayerHealth metode
    public int getPlayerHealth(){
        return player.getHealth();
    }

    public EatResult eatItem(String itemName){
        return player.eat(itemName); //kalder på eat-metoden på player-objektet og sender resultatet tilbage
    }

    //Tunnelmetode til søgemetode i Player i Adventure klassen
    public Item findItemInGame(String itemName){
        return player.findItemsAnywhere(itemName);
    }

    public String equipWeapon(String itemName){
        return player.equip(itemName); //kalderr på equip metoden
    }

    public String getEquippedWeaponName(){
        return player.getEquippedWeaponName();
    }

    public String attackWithWeapon(){
        return player.attack(); //kalder på playerens attack
    }

}
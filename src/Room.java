import java.util.ArrayList;

public class Room {
    private String name;
    private String description;
    private Room north;
    private Room east;
    private Room south;
    private Room west;
    private ArrayList<Item> items = new ArrayList<>();

    public Room(String name, String description){
        this.name = name;
        this.description = description;
        //Javas standardværdi for objekter er automatisk 'null',
        //så alle fire retninger starter med at være tomme.
    }


    //Additem, removeitem og finditem metoder

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    public String getName(){
        return name;
    }

    //Søgemetode: Løber listen igennem og matecher det korte navn
    public Item findItems(String itemName){
        for (Item item : items){
            if (item.getShortName().equalsIgnoreCase(itemName)){
                return item;
            }
        }
        return null; //Returnerer null hvis item ikke blev fundet
    }

    public String getDescription(){
        return description;
    }

    //North
    public void setNorth(Room room){
        this.north = room;
    }

    public Room getNorth(){
        return north;
    }

    //South
    public void setSouth(Room room){
        this.south = room;
    }

    public Room getSouth(){
        return south;
    }

    //East
    public void setEast(Room room){
        this.east = room;
    }

    public Room getEast(){
        return east;
    }

    //West
    public void setWest(Room room){
        this.west = room;
    }

    public Room getWest(){
        return west;
    }

}

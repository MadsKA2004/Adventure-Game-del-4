public class Item {
    private String shortName; //Omdøbt fra name
    private String longName; //Omdøbt fra description


    public Item(String shortName, String longName){
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getShortName(){
        return shortName;
    }

    public String getLongName(){
        return longName;
    }


    public Weapon isWeapon(){
        return null; //Almindelige items som Food er ikke våben
    }
}

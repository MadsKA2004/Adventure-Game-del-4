abstract class Weapon extends Item {

    public Weapon(String shortName, String longName){
        super(shortName, longName);
    }

    public abstract int use();

    public abstract boolean canUse();

    @Override
    public Weapon isWeapon(){
        return this; //Våben overskriver
    }

    public abstract String attack();

}

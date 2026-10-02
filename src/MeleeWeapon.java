public class MeleeWeapon extends Weapon {

    public MeleeWeapon(String shortName, String longName){
        super(shortName, longName);
    }


    @Override
    public boolean canUse() {
        return true; //kan altid bruges
    }

    @Override
    public int use() {
        return -1; //Ubegrænset brug
    }

    public String attack(){
        return "You swing the rusty sword at the empty air";
    }
}

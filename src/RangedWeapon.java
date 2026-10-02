public class RangedWeapon extends Weapon {
    private int ammunition;

    public RangedWeapon(String shortName, String longName, int ammunition) {
        super(shortName, longName);
        this.ammunition = ammunition;

    }

    @Override
    public boolean canUse() {
        return ammunition > 0;
    }

    @Override
    public int use() {
        if (canUse()) {
            ammunition--; //træk et skud fra
        }
        return ammunition; //returnerer fx 4, 3, 2, 1 eller 0
    }

    public String attack() {
        String descripton = getLongName();
        if (descripton.startsWith("a ") || descripton.startsWith("an ")) {
            descripton = descripton.replaceFirst("^(a |an )", "");
            return "You fire the " + descripton + "into the empty air. " + use() + " shots left.";
        }
        return null;
    }
}


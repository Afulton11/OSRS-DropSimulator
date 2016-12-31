package graphics.events.types.button;

/**
 * Created by Andrew on 12/30/2016.
 */
public class ButtonSetEvent extends ButtonEvent {

    private final String name;
    private final int kills;

    public ButtonSetEvent(String npcName, int kills) {
        super(Type.BUTTON_SET);
        this.name = npcName;
        this.kills = kills;
    }

    public String getNpcName() {
        return name;
    }

    public int getKills() {
        return kills;
    }
}

package graphics.events.types.field;

/**
 * Created by Andrew on 12/30/2016.
 */
public class NumKillsEvent extends NumEvent<Integer> {

    public NumKillsEvent(int number) {
        super(number, Type.NUM_KILLS);
    }
}

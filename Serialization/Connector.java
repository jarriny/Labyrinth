package Serialization;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Represents the possible directions that can be reached with a given Connector.
 * Held in a boolean array representing [NORTH, EAST, SOUTH, WEST], true if the Connector
 * points in the designated direction, false if not.
 */
public enum Connector {

    @SerializedName("┼")
    ULDR(new boolean[]{true, true, true, true}),
    @SerializedName("┤")
    UDL(new boolean[]{true, false, true, true}),
    @SerializedName("┴")
    ULR(new boolean[]{true, true, false, true}),
    @SerializedName("├")
    ULD(new boolean[]{true, true, true, false}),
    @SerializedName("┬")
    LDR(new boolean[]{false, true, true, true}),
    @SerializedName("┘")
    UL(new boolean[]{true, false, false, true}),
    @SerializedName("┌")
    DR(new boolean[]{false, true, true, false}),
    @SerializedName("└")
    UR(new boolean[]{true, true, false, false}),
    @SerializedName("┐")
    DL(new boolean[]{false, false, true, true}),
    @SerializedName("─")
    RL(new boolean[]{false, true, false, true}),
    @SerializedName("│")
    UD(new boolean[]{true, false, true, false});

    public final boolean[] shape;

    Connector(boolean[] shape) {
        this.shape = shape;
    }

    /**
     * Gets the corresponding Connector from the given boolean array that represents it.
     * @param shape a boolean array with the accessible directions accessible from the Connector represented
     * @return the Connector represented by the array
     */
    public static Connector fromShape(boolean[] shape){
        for (Connector c: Connector.values()) {
            if (Arrays.equals(shape, c.shape)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Given connector does not exist");
    }
}

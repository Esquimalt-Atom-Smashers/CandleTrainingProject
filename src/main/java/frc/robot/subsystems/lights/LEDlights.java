
package frc.robot.subsystems.lights;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDlights extends SubsystemBase {
    //This is already set to 26 for training ont he Tamatoa robot, but remember to change the CAN ID to match your robot configuration.
    private final CANdle candle = new CANdle(26); //TODO Set CAN ID // the "TODO" keyword in all caps makes this show up in the "problems" tab of the terminal

    //Constants
    private static final double DEFAULT_BRIGHTNESS = 0.2; //20% brightness (0.0 to 1.0) //putting this here makes it easy to find and tweak later if we don't like this value

    private CANdleConfiguration config;

    public LEDlights() {
        config = new CANdleConfiguration();
        config.brightnessScalar = DEFAULT_BRIGHTNESS; //using the constant defined above that way it is easy to tweak later
        config.disableWhenLOS = false;//hover over these methods to see the documentation for what they do (put your mouse pointer over "disableWhenLOS" and wait a second)
        config.statusLedOffWhenActive = true;
        config.stripType = CANdle.LEDStripType.RGB;
        config.vBatOutputMode = VBatOutputMode.Modulated;
        candle.configAllSettings(config,100);

        setColor(CommonColours.PURPLE.colour); //Default colour setting pulled from the Enum at the bottom of this class
    }

    /* public commands and methods are listed before the private methods they call purely for visibility and ease fo reading, this is not a strict rule
     * sometimes a class can be separated by function instead of visibility.
     */

    public Command rainbowCommand() {
        //written as a method reference instead of a lambda you can only do this with methods thaet take no parameters and return void
        return Commands.runOnce(this::rainbowAnimation, this); 
        //using "this" keyword means that this subsystem is required by the command, and any other command running this subsyetm will stop
    }

    public Command setColorCommand(Colour colour) {
        return Commands.runOnce(() -> setColor(colour), this); 
        //using "this" keyword means that this subsystem is required by the command, and any other command running this subsyetm will stop
    }

    public Command setStrobeCommand(Colour colour) {
        return Commands.runOnce(() -> setStrobe(colour), this);
        //using "this" keyword means that this subsystem is required by the command, and any other command running this subsyetm will stop
    }

    /** 
     * @param speed how fast the strobe turns on and off - double between 0 and 1 inclusive
     */
    public Command setStrobeCommand(Colour colour, double speed) {
        return Commands.runOnce(() -> setStrobe(colour, speed), this);
        //using "this" keyword means that this subsystem is required by the command, and any other command running this subsyetm will stop
    }

    public Command clearLEDAnimation() {
        return Commands.runOnce(() -> clearAnimation(), this);

    }

    public Command turnLEDsOff() {
        return setColorCommand(CommonColours.OFF.colour);
        //using "this" keyword is not required here since the command returned by setColorCommand already requires this subsystem
    }
    
    //this is a helper class to make it easier to pass colours as an object. it limits the range of values to those of an RBG value.
    public static class Colour {

        public int r;
        public int g;
        public int b;

        public Colour(int r, int g, int b) {
            this.r = clamp(r);
            this.g = clamp(g);
            this.b = clamp(b);
        }
        private int clamp(int value) {
            return Math.max(0, Math.min(255, value));
        }
    }

    //an enum containing a list of common colours for easy reference
    public enum CommonColours {
        RED(new Colour(255, 0, 0)),
        GREEN(new Colour(0, 255, 0)),
        BLUE(new Colour(0, 0, 255)),
        YELLOW(new Colour(255, 255, 0)),
        PURPLE(new Colour(128, 0, 128)),
        CYAN(new Colour(0, 255, 255)),
        WHITE(new Colour(255, 255, 255)),
        OFF(new Colour(0, 0, 0));

        public final Colour colour;

        CommonColours(Colour colour) {
            this.colour = colour;
        }
    }
    /* All the methods that control the led lights directly are made private to protect subsystem command structure integrity.
        Public commands are made to call these methods safely by using the command structure to ensure they require this subsystem.
        It doesn't make as much sense with LED lights since they don't have conflicting states like mechanical systems do, but it's good practice.
    */

    /** This method runs a rainbow animation from the phoenix led library with some default values */
    private void rainbowAnimation() {
        RainbowAnimation rainbowAnim = new RainbowAnimation(1, 0.5, 64);//currently set to some default values,
        // if you wanted to make these easily configurable, you could add them to a Constants section at the top of the class
        candle.animate(rainbowAnim);
    }

    private void setStrobe(Colour colour) {
        candle.animate(new StrobeAnimation(colour.r, colour.g, colour.b));
    }
    /** this strobes only some of the LEDs (roughly numbered 20 to 35), at a speed set by the caller of theis method */
    //currently unused but could be useful later mainly included to show the Object Oriented Programming property of polymorphism
    private void setStrobe(Colour colour, double speed) {
        speed = Math.max(0.01, Math.min(1, speed)); //clamp speed between 0.01 and 1
        candle.animate(new StrobeAnimation(colour.r, colour.g, colour.b, 0,speed,15,20));
    }

    private void setColor(Colour colour) {
        candle.setLEDs(colour.r, colour.g, colour.b);
    }

    private void clearAnimation() { 
        candle.clearAnimation(0);
    }
}
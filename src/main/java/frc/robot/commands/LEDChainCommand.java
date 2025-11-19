package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.lights.LEDlights;
import frc.robot.subsystems.lights.LEDlights.Colour;

public class LEDChainCommand extends Command {
    private final LEDlights ledSubsystem; 
    private final Timer timer = new Timer();
    private final Timer secondTimer = new Timer();
    private int indexAmount = 1;
    private final Colour OFF = new Colour (0,0,0);
    private final Colour CHARTREUSE = new Colour(223,255,0);
    private final Colour RED = new Colour(255,0,0);
    public LEDChainCommand (LEDlights ledSubsystem) {
        this.ledSubsystem = ledSubsystem;
        addRequirements(ledSubsystem);
        
    }
    
    @Override
    public void initialize() {
        timer.start();
        secondTimer.start();
        timer.reset();
        secondTimer.reset();
        indexAmount=-1;
    }
    public void execute() {
        int turnOffLights = indexAmount - 10;
        if (turnOffLights < 0) {
            turnOffLights = 0;
        }
        if (timer.get() > 0.125 ) {
            indexAmount++;
            timer.reset();
            ledSubsystem.setLEDS(CHARTREUSE,indexAmount,10);
        }
        else if (secondTimer.get() > 0.25 ) {
            ledSubsystem.setLEDS(RED, turnOffLights,10);
            secondTimer.reset();
        }


        if (indexAmount >= 212) {
            indexAmount = -1;
        }
    }
}
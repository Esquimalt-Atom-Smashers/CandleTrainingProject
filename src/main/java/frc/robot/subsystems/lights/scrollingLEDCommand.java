// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.lights;

import frc.robot.subsystems.lights.LEDlights;
import frc.robot.subsystems.lights.LEDlights.Colour;
import frc.robot.subsystems.lights.LEDlights.CommonColours;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/** An example command that uses an example subsystem. */
public class scrollingLEDCommand extends Command {
  private final LEDlights ledLightsSubsystem;
  private static Timer timer = new Timer();
  private static int lastCount =-1; //to keep track of last second we changed colour
  private double lightsPerSecond;
  private int numberOfScrollingLEDs, stripLength;
  private Colour startColour, scrollColour, finishColour;

  /**
   * Creates a new ExampleCommand that runs for 10 seconds
   *
   * @param subsystem The subsystem used by this command.
   */
  public scrollingLEDCommand(LEDlights subsystem, double lightsPerSecond) {
    ledLightsSubsystem = subsystem;
    this.lightsPerSecond = lightsPerSecond;
    numberOfScrollingLEDs = 10;
    stripLength = 212;
    startColour = CommonColours.BLUE.colour;
    scrollColour = CommonColours.GREEN.colour;
    finishColour = CommonColours.RED.colour;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
    ledLightsSubsystem.forceColour(startColour);// I imported CommonColours to make it shorter to use them in line
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //nothing needs to happen here in this simple code, but you could have it change to a different colour every second or something
    int totalCounter = (int) (timer.get() * lightsPerSecond);//get the index of the furthest forward scrolling LED from the timer.
    int counter = totalCounter % (stripLength + 1); //modulus operator allows looping the LEDs to the beginning
    //only change the colour if the number of elapsed seconds has changed
    //the "return" statement exits the "execute()" method early if there is no change, this can save a lot of CAN bus traffic at slow led speeds
    if (counter == lastCount) {
      return; //no change, so do nothing
    }
    //stores the number of LEDs that need to change behind the Count to set everything to the finish colour
    // without skipping any when the LED speed is high.
    int ledsToChangeFinish = Math.max(0, counter - lastCount);
    lastCount = counter; //update the last elapsed seconds
    //turn the new lights to the scroll colour
    ledLightsSubsystem.setColour(scrollColour, Math.max(counter - numberOfScrollingLEDs, 0), Math.min(numberOfScrollingLEDs, counter));
    
    //turn the old lights to the finish colour
    if (counter >= numberOfScrollingLEDs){//TODO check logic to see if just greater than
      ledLightsSubsystem.setColour(finishColour, (counter - numberOfScrollingLEDs - ledsToChangeFinish), ledsToChangeFinish);
    }

    //if the leds have reached the end of the longest string,turn off the appropriate number of end LEDs
    if ((totalCounter) > stripLength){
      if (counter < numberOfScrollingLEDs){//don't waste can messages addressing the end when it should already have turned everything to the finish colour
        ledLightsSubsystem.setColour(finishColour, (stripLength + counter - numberOfScrollingLEDs - ledsToChangeFinish), ledsToChangeFinish);
      }
    }

    
    System.out.println(counter + "lights have turned on");

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      //when the command is inturrupted, the lights go red
      //ledLightsSubsystem.forceColour(CommonColours.RED.colour); //with this commented out, they just stay wherever the command was inturrupted
    } else {
      //when the command ends normally, the lights go black/off
      ledLightsSubsystem.forceColour(new Colour(0, 0, 0));
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(10.0);
  }
}

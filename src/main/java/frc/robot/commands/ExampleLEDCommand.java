// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.lights.LEDlights;
import frc.robot.subsystems.lights.LEDlights.CommonColours;

/** An example command that uses an example subsystem. */
public class ExampleLEDCommand extends Command {
  private final LEDlights ledLightsSubsystem;
  private static Timer timer = new Timer();
  private static int lastElapsedSeconds =-1; //to keep track of last second we changed colour

  /**
   * Creates a new ExampleCommand that runs for 10 seconds
   *
   * @param subsystem The subsystem used by this command.
   */
  public ExampleLEDCommand(LEDlights subsystem) {
    ledLightsSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
    ledLightsSubsystem.forceColour(CommonColours.RED.colour);// I imported CommonColours to make it shorter to use them in line
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //nothing needs to happen here in this simple code, but you could have it change to a different colour every second or something
    int elapsedSeconds = (int) timer.get();//get the number of seconds elapsed as an integer rounded down
    //only change the colour if the number of elapsed seconds has changed
    //the "return" statement exits the "execute()" method early if there is no change, this can save a lot of CAN bus traffic
    if (elapsedSeconds == lastElapsedSeconds) {
      return; //no change in seconds, so do nothing
    }
    lastElapsedSeconds = elapsedSeconds; //update the last elapsed seconds
    //this switch statement changes the colour every second for the first 7 seconds
    switch (elapsedSeconds) {
      case 0:
        ledLightsSubsystem.forceColour(CommonColours.RED.colour);
        break;
      case 1:
        ledLightsSubsystem.forceColour(CommonColours.GREEN.colour);
        break;
      case 2:
        ledLightsSubsystem.forceColour(CommonColours.BLUE.colour);
        break;
      case 3:
        ledLightsSubsystem.forceColour(CommonColours.YELLOW.colour);
        break;
      case 4:
        ledLightsSubsystem.forceColour(CommonColours.PURPLE.colour);
        break;
      case 5:
        ledLightsSubsystem.forceColour(CommonColours.CYAN.colour);
        break;
      case 6:
        ledLightsSubsystem.forceColour(CommonColours.WHITE.colour);
        break;
      default:
        //do nothing after 7 seconds
        break;
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      //when the command is inturrupted, the lights go red
      ledLightsSubsystem.forceColour(CommonColours.RED.colour);
    } else {
      //when the command ends normally, the lights go black/off
      ledLightsSubsystem.forceColour(CommonColours.OFF.colour);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(10.0);
  }
}

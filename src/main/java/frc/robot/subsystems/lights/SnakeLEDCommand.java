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
public class SnakeLEDCommand extends Command {
  private final LEDlights ledLightsSubsystem;
  private final int LENGTH_OF_CHAIN = 212;
  private final int NUMBER_OF_LEDS = 30;
  private final double SECONDS_PER_MOVE = 0.0005;
  private final double EXECUTE_PERIOD = 0.002;
  private int counter;
  

  /**
   * Creates a new ExampleCommand that runs for 10 seconds
   *
   * @param subsystem The subsystem used by this command.
   */
  public SnakeLEDCommand(LEDlights subsystem) {
    ledLightsSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //timer.reset();
    //timer.start();
    ledLightsSubsystem.forceColour(CommonColours.RED.colour);// I imported CommonColours to make it shorter to use them in line
    ledLightsSubsystem.setSingleLED(0,1);
    counter = 1;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    counter ++;
    int index = (int)(counter/(SECONDS_PER_MOVE/EXECUTE_PERIOD));
    if ((index - NUMBER_OF_LEDS) >= 0){
        //turn off old leds
        ledLightsSubsystem.turnOffSingleLED((index - NUMBER_OF_LEDS),1);
    }
    //set index led to white
    ledLightsSubsystem.setSingleLED(index,1);
    if (index > (LENGTH_OF_CHAIN + NUMBER_OF_LEDS)){
      counter =1;
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
      ledLightsSubsystem.forceColour(new Colour(0, 0, 0));
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //return timer.hasElapsed(10.0);
    return false;
  }
}

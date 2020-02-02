package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.AutoCommand1;
import frc.robot.commands.CPSpinCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ExtenderSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShelbowSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SpinnerSubsystem;
import frc.robot.subsystems.WinchSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  
  /**
   * 
   * Subsystems
   * 
   */
  private final ExtenderSubsystem extenderSubsystem = new ExtenderSubsystem();
  private final IndexerSubsystem indexerSubsystem = new IndexerSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final ShelbowSubsystem shelbowSubsystem = new ShelbowSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem(true);
  private final SpinnerSubsystem spinnerSubsystem = new SpinnerSubsystem();
  private final WinchSubsystem winchSubsystem = new WinchSubsystem();

  // pass the shelbow talon into the driveSubsystem's contstuctor to give to the gyro
  private final DriveSubsystem driveSubsystem = new DriveSubsystem(shelbowSubsystem.getMasterReference());

  /**
   * 
   * Commands n Stuff
   * 
   */
  //private final ExampleCommand autoCommand = new ExampleCommand(subsystem);
  
  // setup auton
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  private final Command m_simpleAuto = new AutoCommand1();

  // sticks
  private final Joystick driverStick = new Joystick(0);
  private final Joystick operatorStick = new Joystick(1);


  /**
   * 
   * Constructor
   * 
   */
  public RobotContainer() {

    configureButtonBindings();

    driveSubsystem.setDefaultCommand(
      new RunCommand(() -> driveSubsystem.arcadeDrive(-driverStick.getY(), driverStick.getTwist()), driveSubsystem)
    );

    shelbowSubsystem.setDefaultCommand(
      new RunCommand(() -> shelbowSubsystem.shelbowFlex(-operatorStick.getY()), shelbowSubsystem)
    );

    // Add commands to the autonomous command chooser
    m_chooser.addOption("Test Auto", m_simpleAuto);

    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);
  }


  /**
   * 
   * Button Mapping
   * 
   */
  private void configureButtonBindings() {

    /**
     * Intake 
     */

    new JoystickButton(driverStick, 5).whenPressed(
      new InstantCommand(intakeSubsystem::raise, intakeSubsystem)
    );

    new JoystickButton(driverStick, 3).whenPressed(
      new InstantCommand(intakeSubsystem::lower, intakeSubsystem)
    );



    /**
     * Full Intake Routine 
     */
    
     // button pressed: raise arm, run intake, run indexer
     new JoystickButton(driverStick, 2).whenPressed(
      new ParallelCommandGroup(
        // todo: raise shelbow
        new InstantCommand(intakeSubsystem::acquire, intakeSubsystem),
        new InstantCommand(indexerSubsystem::forward, indexerSubsystem)
      )
    
    // button released: stop shooter, stop intake, back balls up a scootch in indexer, then stop it
    ).whenReleased(
      new ParallelCommandGroup(
        new InstantCommand(shooterSubsystem::stop, shooterSubsystem),
        new InstantCommand(intakeSubsystem::stop, intakeSubsystem),
        new StartEndCommand(
          () -> indexerSubsystem.backward(),
          () -> indexerSubsystem.stop(),
          indexerSubsystem
        ).withTimeout(0.05)
      )
    );


    
    /**
     * Full Intake Routine 
     */
    
    // button pressed: start shooter velocity control
    new JoystickButton(driverStick, 1).whenPressed(
      new InstantCommand(shooterSubsystem::setVelocity, shooterSubsystem)

    // while button pressed: if shooter is at velocity, run indexer and intake forward
    // if not, stop indexer and intake
    ).whileHeld(
      new ConditionalCommand(
        // if true
        new ParallelCommandGroup(
         new InstantCommand(indexerSubsystem::forward, indexerSubsystem),
         new InstantCommand(intakeSubsystem::acquire, intakeSubsystem)
        ),
        // if false
        new ParallelCommandGroup(
          new InstantCommand(intakeSubsystem::stop, intakeSubsystem),
          new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
        ),
        // the condition
        shooterSubsystem::atSetpoint
      )
    
    // button released: stop intake, back balls up a scootch in indexer, then stop it
    ).whenReleased(
      new ParallelCommandGroup(
        new InstantCommand(intakeSubsystem::stop, intakeSubsystem),
        new StartEndCommand(
          () -> indexerSubsystem.backward(),
          () -> indexerSubsystem.stop(),
          indexerSubsystem
        ).withTimeout(0.05)
      )
    );



    
    /**
     * Shelbow 
     */ 







    /**
     * Indexer 
     */
    // forward
    new JoystickButton(driverStick, 6).whenPressed(
      new InstantCommand(indexerSubsystem::forward, indexerSubsystem)
    ).whenReleased(
      new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
    );

    // reverse, reverse! 
    new JoystickButton(driverStick, 4).whenPressed(
      new InstantCommand(indexerSubsystem::backward, indexerSubsystem)
    ).whenReleased(
      new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
    );

    

    /**
     * Spinner 
     */
  
    // Spin the control panel 
    new JoystickButton(operatorStick, 11).whenPressed(
      new InstantCommand(spinnerSubsystem::spin, spinnerSubsystem)
    ).whenReleased(
      new InstantCommand(spinnerSubsystem::stop, spinnerSubsystem)
    );

    // setup a button and command for spinning the spinner a predetermined amount of encoder ticks
    new JoystickButton(operatorStick, 9).whenPressed(
      new CPSpinCommand(spinnerSubsystem)
    );
    
    new JoystickButton(operatorStick, 10).whenPressed(
      new InstantCommand(spinnerSubsystem::raise, spinnerSubsystem)
    ).whenReleased(
      new InstantCommand(spinnerSubsystem::stop, spinnerSubsystem)
    );

    new JoystickButton(operatorStick, 12).whenPressed(
      new InstantCommand(spinnerSubsystem::lower, spinnerSubsystem)
    ).whenReleased(
      new InstantCommand(spinnerSubsystem::stop, spinnerSubsystem)
    );

    /**
     * Extender 
     */

    // extend to climb
    new JoystickButton(driverStick, 9).whenPressed(
      new InstantCommand(extenderSubsystem::extend, extenderSubsystem)
    ).whenReleased(
      new InstantCommand(extenderSubsystem::stop, extenderSubsystem)
    );

    //retract
    new JoystickButton(driverStick, 10).whenPressed(
      new InstantCommand(extenderSubsystem::retract, extenderSubsystem)
    ).whenReleased(
      new InstantCommand(extenderSubsystem::stop, extenderSubsystem)
    );


    /**
     * Winch 
     */
  
    // climb
    new JoystickButton(operatorStick, 12).whenPressed(
      new InstantCommand(winchSubsystem::liftoff, winchSubsystem)
    ).whenReleased(
      new InstantCommand(winchSubsystem::hold, winchSubsystem)
    );

  }


  /**
   * Get Selected Auton Command
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}
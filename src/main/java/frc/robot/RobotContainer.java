package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.AutoCommand1;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ExtenderSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShelbowSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SpinnerSubsystem;
import frc.robot.subsystems.WinchSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
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
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final ExtenderSubsystem extenderSubsystem = new ExtenderSubsystem();
  private final IndexerSubsystem indexerSubsystem = new IndexerSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final ShelbowSubsystem shelbowSubsystem = new ShelbowSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final SpinnerSubsystem spinnerSubsystem = new SpinnerSubsystem();
  private final WinchSubsystem winchSubsystem = new WinchSubsystem();


  /**
   * 
   * Commands n Stuff
   * 
   */
  //private final ManualDriveCommand manualDriveCommand = new ManualDriveCommand();
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

    // mow the lawn
    new JoystickButton(operatorStick, 2).whenPressed(
      new InstantCommand(intakeSubsystem::aquire, intakeSubsystem)
    ).whenReleased(
      new InstantCommand(intakeSubsystem::stop, intakeSubsystem)
    );
     
    
    /**
     * Shelbow 
     */ 
    
    new JoystickButton(operatorStick, ).whenPressed(
      new InstantCommand(shelbowSubsystem::, shelbowSubsystem)
    ).whenReleased(
      new InstantCommand(shelbowSubsystem::, shelbowSubsystem)
    );


    /**
     * Indexer 
     */
    // Spin up the indexer when the #6 button is pressed
    new JoystickButton(operatorStick, 6).whenPressed(
      new InstantCommand(indexerSubsystem::forward, indexerSubsystem)
    ).whenReleased(
      new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
    );

    // reverse, reverse! 
    new JoystickButton(operatorStick, 4).whenPressed(
      new InstantCommand(indexerSubsystem::forward, indexerSubsystem)
    ).whenReleased(
      new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
    );

    
    /**
     * Shooter 
     */
    
     // yeet em
    new JoystickButton(driverStick, 1).whenPressed(
      new InstantCommand(shooterSubsystem::spin, shooterSubsystem)
    ).whenReleased(
      new InstantCommand(shooterSubsystem::stop, shooterSubsystem)
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
    
    
    /**
     * Extender 
     */

    // extend to climb
    new JoystickButton(driverStick, 8).whenPressed(
      new InstantCommand(extenderSubsystem::extend, extenderSubsystem)
    ).whenReleased(
      new InstantCommand(extenderSubsystem::stop, extenderSubsystem)
    );

    //retract
    new JoystickButton(driverStick, 7).whenPressed(
      new InstantCommand(extenderSubsystem::retract, extenderSubsystem)
    ).whenReleased(
      new InstantCommand(extenderSubsystem::stop, extenderSubsystem)
    );


    /**
     * Winch 
     */
  
    // climb
    new JoystickButton(operatorStick, 9).whenPressed(
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
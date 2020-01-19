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
  // The robot's subsystems and commands are defined here
  //Subsystems
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final ExtenderSubsystem extenderSubsystem = new ExtenderSubsystem();
  private final IndexerSubsystem indexerSubsystem = new IndexerSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final ShelbowSubsystem shelbowSubsystem = new ShelbowSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final SpinnerSubsystem spinnerSubsystem = new SpinnerSubsystem();
  private final WinchSubsystem winchSubsystem = new WinchSubsystem();

  //Commands
  //private final ManualDriveCommand manualDriveCommand = new ManualDriveCommand();
  



  //private final ExampleCommand autoCommand = new ExampleCommand(subsystem);
  

  private final Joystick driverStick = new Joystick(0);
  private final Joystick operatorStick = new Joystick(1);


   // A chooser for autonomous commands
   SendableChooser<Command> m_chooser = new SendableChooser<>();

   // A simple auto routine that drives forward a specified distance, and then stops.
  private final Command m_simpleAuto =
  new AutoCommand1();


  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
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
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

   // INDEXER Spin up the indexer when the #6 button is pressed
   new JoystickButton(operatorStick, 6).whenPressed(
     new InstantCommand(indexerSubsystem::forward, indexerSubsystem)
   ).whenReleased(
       new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
     );
    // INDEXER reverse, reverse! 
    new JoystickButton(operatorStick, 4).whenPressed(
     new InstantCommand(indexerSubsystem::forward, indexerSubsystem))
     .whenReleased(
       new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
    );
    //INTAKE mow the lawn
    new JoystickButton(operatorStick, 2).whenPressed(
     new InstantCommand(intakeSubsystem::aquire, intakeSubsystem))
     .whenReleased(
       new InstantCommand(intakeSubsystem::stop, intakeSubsystem)
    );
    //EXTENDER extend to climb
    new JoystickButton(driverStick, 8).whenPressed(
      new InstantCommand(extenderSubsystem::extend, extenderSubsystem))
      .whenReleased(
        new InstantCommand(extenderSubsystem::stop, extenderSubsystem)
    );
    //EXTENDER retract
      new JoystickButton(driverStick, 7).whenPressed(
      new InstantCommand(extenderSubsystem::retract, extenderSubsystem))
      .whenReleased(
        new InstantCommand(extenderSubsystem::stop, extenderSubsystem)
    );
    //SPINNER Spin the control panel 
    new JoystickButton(operatorStick, 11).whenPressed(
     new InstantCommand(spinnerSubsystem::spin, spinnerSubsystem))
     .whenReleased(
       new InstantCommand(spinnerSubsystem::stop, spinnerSubsystem)
    );
    //WINCH climb
    new JoystickButton(operatorStick, 9).whenPressed(
     new InstantCommand(winchSubsystem::liftoff, winchSubsystem))
     .whenReleased(
       new InstantCommand(winchSubsystem::hold, winchSubsystem)
    );
    //SHELBOW
    new JoystickButton(operatorStick, ).whenPressed(
     new InstantCommand(shelbowSubsystem::, shelbowSubsystem))
     .whenReleased(
       new InstantCommand(shelbowSubsystem::, shelbowSubsystem)
    );
    //SHOOTER yeet em
    new JoystickButton(driverStick, 1).whenPressed(
     new InstantCommand(shooterSubsystem::spin, shooterSubsystem))
     .whenReleased(
       new InstantCommand(shooterSubsystem::stop, shooterSubsystem)
    );
  }


  //private boolean whenReleased(InstantCommand instantCommand) {
    //return false;
 // }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_chooser.getSelected();
  }
}

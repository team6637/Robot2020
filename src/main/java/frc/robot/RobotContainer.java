package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.AutoCommand1;
import frc.robot.commands.CPSpinCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.AutoAim;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ExtenderSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.ShelbowSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SpinnerSubsystem;
import frc.robot.subsystems.WinchSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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
  private final LimelightSubsystem limelightSubsystem = new LimelightSubsystem(true);
  private final ShelbowSubsystem shelbowSubsystem = new ShelbowSubsystem(true);
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem(true); 
  private final SpinnerSubsystem spinnerSubsystem = new SpinnerSubsystem();
  private final WinchSubsystem winchSubsystem = new WinchSubsystem();

  // pass the shelbow talon into the driveSubsystem's contstuctor to give to the gyro
  private final DriveSubsystem driveSubsystem = new DriveSubsystem(true);
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
  public static Joystick driverStick = new Joystick(0);
  private final Joystick operatorStick = new Joystick(1);


  /**
   * 
   * Constructor
   * 
   */
  public RobotContainer() {

    configureButtonBindings();

    driveSubsystem.setDefaultCommand(
      new RunCommand(() -> driveSubsystem.arcadeDrive(getDriveToggle() * driverStick.getY(), driverStick.getTwist()), driveSubsystem)
    );

    
    // Add commands to the autonomous command chooser
    m_chooser.addOption("Test Auto", m_simpleAuto);

    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);

    limelightSubsystem.setupDriveMode();
  }

  public int getDriveToggle() {
    if(driverStick.getThrottle() > 0) {
      return -1;
    } else {
      return 1;
    } 
  }



  /**
   * 
   * Button Mapping
   * 
   */
  private void configureButtonBindings() {

    /**
     * Intake Routine 
     */
    
    // button pressed: raise arm, run intake, run indexer
    new JoystickButton(driverStick, 2).whenHeld(
      new IntakeCommand(intakeSubsystem, indexerSubsystem, shelbowSubsystem, shooterSubsystem)
    );


        /**
     * Indexer 
     */
    // forward
    new JoystickButton(driverStick, 6).whenPressed(
      new ParallelCommandGroup(
        new InstantCommand(indexerSubsystem::forward, indexerSubsystem),
        new InstantCommand(shooterSubsystem::backward, shooterSubsystem)
      )
    ).whenReleased(
      new ParallelCommandGroup(
        new InstantCommand(indexerSubsystem::stop, indexerSubsystem),
        new InstantCommand(shooterSubsystem::stop, shooterSubsystem)
      )
    );

    // reverse, reverse! 
    new JoystickButton(driverStick, 4).whenPressed(
      new ParallelCommandGroup(
        new InstantCommand(indexerSubsystem::backward, indexerSubsystem),
        new InstantCommand(intakeSubsystem::backward, intakeSubsystem)
      )
    ).whenReleased(
      new ParallelCommandGroup(
        new InstantCommand(indexerSubsystem::stop, indexerSubsystem),
        new InstantCommand(intakeSubsystem::stop, intakeSubsystem)
      )
    );

    // Low Goal Shoot Out Balls
    new JoystickButton(operatorStick, 2).whileHeld(
      new ParallelCommandGroup(
        new InstantCommand(indexerSubsystem::forward, indexerSubsystem),
        new InstantCommand(intakeSubsystem::acquire, intakeSubsystem),
        new InstantCommand(shooterSubsystem::shoot, shooterSubsystem)
      )
    ).whenReleased(
      new ParallelCommandGroup(
        new InstantCommand(indexerSubsystem::stop, indexerSubsystem),
        new InstantCommand(intakeSubsystem::stop, intakeSubsystem),
        new InstantCommand(shooterSubsystem::stop, shooterSubsystem)
      )
    );


    /**
     * Shooter Routine 
     */
    
    // button pressed: start shooter velocity control
    new JoystickButton(driverStick, 1).whenPressed(
      new InstantCommand(() -> shooterSubsystem.setVelocityFromAngle(shelbowSubsystem.getAngle()), shooterSubsystem)

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
        new InstantCommand(shooterSubsystem::stop, shooterSubsystem),
        new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
      )
    );


    /**
     * Auto Align 
     */
    new JoystickButton(driverStick, 3).whenHeld(
      new AutoAim(limelightSubsystem, driveSubsystem, shelbowSubsystem) 
    );


    
    
    /**
     * Shelbow 
     */

    // SETPOINTS
    new JoystickButton(driverStick, 7).whenPressed(
      new InstantCommand(shelbowSubsystem::goToUpPosition, shelbowSubsystem)
    );

    new JoystickButton(driverStick, 11).whenPressed(
      new InstantCommand(shelbowSubsystem::goToDownPosition, shelbowSubsystem)
    );

    new JoystickButton(driverStick, 9).whenPressed(
      new InstantCommand(shelbowSubsystem::goToCenterPosition, shelbowSubsystem)
    );

    // Manual Override
    // hold trigger and move stick to manually control shelbow
    new JoystickButton(operatorStick, 1).whenPressed(
      new SequentialCommandGroup(
        new InstantCommand(shelbowSubsystem::stopMotionMagic, shelbowSubsystem),
        new RunCommand(() -> shelbowSubsystem.shelbowFlex(operatorStick.getY()), shelbowSubsystem)
      )
    ).whenReleased(
      new InstantCommand(shelbowSubsystem::startMotionMagic, shelbowSubsystem) 
    );
    

    /**
     * Spinner 
     */
  
    // Spin the control panel 
    new JoystickButton(operatorStick, 8).whenPressed(
      new InstantCommand(spinnerSubsystem::spin, spinnerSubsystem)
    ).whenReleased(
      new InstantCommand(spinnerSubsystem::stop, spinnerSubsystem)
    );

    // setup a button and command for spinning the spinner a predetermined amount of encoder ticks
    new JoystickButton(operatorStick, 9).whenPressed(
      new CPSpinCommand(spinnerSubsystem)
    );
    

    /**
     * Extender 
     */

    // extend to climb
    new JoystickButton(operatorStick, 5).whenPressed(
      new InstantCommand(extenderSubsystem::extend, extenderSubsystem)
    ).whenReleased(
      new InstantCommand(extenderSubsystem::stop, extenderSubsystem)
    );

    //retract
    new JoystickButton(operatorStick, 3).whenPressed(
      new InstantCommand(extenderSubsystem::retract, extenderSubsystem)
    ).whenReleased(
      new InstantCommand(extenderSubsystem::stop, extenderSubsystem)
    );


    /**
     * Winch 
     */
  
    // climb
    new JoystickButton(operatorStick, 10).whenPressed(
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
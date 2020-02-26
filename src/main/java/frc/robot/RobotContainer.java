package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import frc.robot.commands.CPSpinCommand;
import frc.robot.commands.DriveToDistanceCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.Constants.ShelbowConstants;
//import frc.robot.commands.TurnToAngleCommand;
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
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

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
  private final IndexerSubsystem indexerSubsystem = new IndexerSubsystem(true);
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final LimelightSubsystem limelightSubsystem = new LimelightSubsystem(true);
  private final ShelbowSubsystem shelbowSubsystem = new ShelbowSubsystem(true);
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem(true); 
  private final SpinnerSubsystem spinnerSubsystem = new SpinnerSubsystem();
  private final WinchSubsystem winchSubsystem = new WinchSubsystem();

  // pass the shelbow talon into the driveSubsystem's contstuctor to give to the gyro
  private final DriveSubsystem driveSubsystem = new DriveSubsystem(true);

  // setup auton dropdown
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  // sticks
  public static Joystick driverStick = new Joystick(0);
  public static Joystick operatorStick = new Joystick(1);
  

  /**
   * 
   * Commands
   * 
   */
  // AIM COMMAND


  // SHOOT COMMAND


  // TEST COMMAND
  private final Command m_ttaTest = new SequentialCommandGroup(
    //new TurnToAngleCommand(driveSubsystem, 90),
    new DriveToDistanceCommand(driveSubsystem, 1)
  );



   /**
   * 
   * Command Routines
   * 
   */

  // SHOOT, DRIVE BACKWARD
  private final Command shootDriveBackCommand = new SequentialCommandGroup(
    new InstantCommand(() -> shelbowSubsystem.setTargetPosition(ShelbowConstants.autonPositionFrontCenter), shelbowSubsystem),
    new WaitCommand(4).withInterrupt(()-> shelbowSubsystem.atSetpoint()),
    new InstantCommand(() -> shooterSubsystem.setVelocityFromAngle(shelbowSubsystem.getAngleWithoutYOffset()), shooterSubsystem),
    new WaitCommand(4).withInterrupt(()-> shooterSubsystem.atSetpoint()),
    new InstantCommand(indexerSubsystem::forward, indexerSubsystem),
    new WaitCommand(3),
    new ParallelCommandGroup(
      new InstantCommand(shooterSubsystem::stop, shooterSubsystem),
      new InstantCommand(indexerSubsystem::stop, indexerSubsystem)
    ),
    new DriveToDistanceCommand(driveSubsystem, 1)
  );

  // SHOOT, DRIVE FORWARD, DRIVE BACKWARD
  private final Command shootDriveForwardBackCommand = new SequentialCommandGroup(
    new InstantCommand(() -> shooterSubsystem.setVelocityFromAngle(shelbowSubsystem.getAngleWithoutYOffset()), shooterSubsystem),
    new WaitCommand(4).withInterrupt(()-> shooterSubsystem.atSetpoint()),
    new InstantCommand(indexerSubsystem::forward, indexerSubsystem).withTimeout(3),
    new DriveToDistanceCommand(driveSubsystem, .5),
    new DriveToDistanceCommand(driveSubsystem, -1.5)
  );



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
    m_chooser.addOption("Test Auto", m_ttaTest);
    m_chooser.addOption("Shoot Drive Back", shootDriveBackCommand);
    m_chooser.addOption("Shoot Drive Forward Back", shootDriveForwardBackCommand);

    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);

    limelightSubsystem.setupDriveMode();
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

    // back off balls 
    new POVButton(driverStick, 180).whenPressed(
      new ParallelCommandGroup(
        new RunCommand(() -> indexerSubsystem.backwardSlow(), indexerSubsystem),
        new RunCommand(() -> shooterSubsystem.backward(), shooterSubsystem)
      ).withInterrupt(() -> !indexerSubsystem.getBallSensorTop()).withTimeout(1.0).andThen(
        new ParallelCommandGroup(
          new InstantCommand(indexerSubsystem::stop, indexerSubsystem),
          new InstantCommand(shooterSubsystem::stop, shooterSubsystem)
        )
      )
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

    
    /**
     * Shelbow 
     */

    // MOTION MAGIC SETPOINTS
    new JoystickButton(driverStick, 7).whenPressed(
      new InstantCommand(shelbowSubsystem::goToUpPosition, shelbowSubsystem)
    );

    new JoystickButton(driverStick, 11).whenPressed(
      new InstantCommand(shelbowSubsystem::goToDownPosition, shelbowSubsystem)
    );

    new JoystickButton(driverStick, 9).whenPressed(
      new InstantCommand(shelbowSubsystem::goToCenterPosition, shelbowSubsystem)
    );

    // MANUAL OVERIDE
    // hold trigger and move stick to manually control shelbow
    new JoystickButton(operatorStick, 1).whenPressed(
      new SequentialCommandGroup(
        new InstantCommand(shelbowSubsystem::stopMotionMagic, shelbowSubsystem),
        new RunCommand(() -> shelbowSubsystem.shelbowFlex(operatorStick.getY()), shelbowSubsystem)
      )
    ).whenReleased(
      new SequentialCommandGroup(
        new InstantCommand(shelbowSubsystem::stop, shelbowSubsystem),
        new WaitCommand(.3),
        new InstantCommand(() -> shelbowSubsystem.setTargetPosition(shelbowSubsystem.getPosition()), shelbowSubsystem),
        new InstantCommand(shelbowSubsystem::startMotionMagic, shelbowSubsystem)
      ) 
    );

    
    /**
     * Shooter Routine 
     */

    // button pressed: start shooter velocity control
    new JoystickButton(driverStick, 1).whenPressed(
     new InstantCommand(() -> shooterSubsystem.setVelocityFromAngle(shelbowSubsystem.getAngleWithoutYOffset()), shooterSubsystem)

    // new InstantCommand(() -> shooterSubsystem.setVelocity())

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


    // Manual Shoot Out Balls
    new JoystickButton(operatorStick, 2).whileHeld(
      new ParallelCommandGroup(
        new InstantCommand(indexerSubsystem::forward, indexerSubsystem),
        new InstantCommand(intakeSubsystem::acquire, intakeSubsystem),
        new InstantCommand(() -> shooterSubsystem.shootManual(operatorStick.getThrottle()), shooterSubsystem)
      )
    ).whenReleased(
      new ParallelCommandGroup(
        new InstantCommand(indexerSubsystem::stop, indexerSubsystem),
        new InstantCommand(intakeSubsystem::stop, intakeSubsystem),
        new InstantCommand(shooterSubsystem::stop, shooterSubsystem)
      )
    );


    /**
     * Auto Align 
     */
    new JoystickButton(driverStick, 3).whenHeld(
      new AutoAim(limelightSubsystem, driveSubsystem, shelbowSubsystem) 
    );
    

    /**
     * Spinner 
     */
  
    // Spin the control panel 
    new JoystickButton(operatorStick, 7).whenPressed(
      new InstantCommand(spinnerSubsystem::spin, spinnerSubsystem)
    ).whenReleased(
      new InstantCommand(spinnerSubsystem::stop, spinnerSubsystem)
    );

    // setup a button and command for spinning the spinner a predetermined amount of encoder ticks
    //new JoystickButton(operatorStick, 9).whenPressed(
      //new CPSpinCommand(spinnerSubsystem)
    //);

    // setup a button and command for spinning the spinner to a color
    //new JoystickButton(operatorStick, 11).whenPressed(
      //new ColorSpinCommand(spinnerSubsystem)
    //);


    /**
     * Extender 
     */

    // extend to climb
    new JoystickButton(operatorStick, 5).whenPressed(
      new ParallelCommandGroup(
        new InstantCommand(extenderSubsystem::extend, extenderSubsystem),
        new InstantCommand(shelbowSubsystem::goToCenterPosition, shelbowSubsystem)
      )
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
    new JoystickButton(operatorStick, 6).whenPressed(
      new InstantCommand(winchSubsystem::liftoff, winchSubsystem)
    ).whenReleased(
      new InstantCommand(winchSubsystem::stop, winchSubsystem)
    );
  }




  /**
   * 
   * Local Methods
   * 
   */
  
  // if the throttle on the drive joystick is up, return -1, else 1
  // used for negating the drive
  public int getDriveToggle() {
    if(driverStick.getThrottle() > 0) {
      return -1;
    } else {
      return 1;
    } 
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
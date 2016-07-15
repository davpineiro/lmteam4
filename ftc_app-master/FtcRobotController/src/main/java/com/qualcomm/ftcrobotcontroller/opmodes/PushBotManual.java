package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotManual
//
/**
 * Provide a basic manual operational mode that uses the left and right
 * drive motors, left arm motor, servo motors and gamepad input from two
 * gamepads for the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */
public class PushBotManual extends PushBotTelemetry

{
    //--------------------------------------------------------------------------
    //
    // PushBotManual
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public PushBotManual ()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotManual

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during
     * manual-operation.  The state machine uses gamepad input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        //----------------------------------------------------------------------
        //
        // DC Motors
        //
        // Obtain the current values of the joystick controllers.
        //
        // Note that x and y equal -1 when the joystick is pushed all of the way
        // forward (i.e. away from the human holder's body).
        //
        // The clip method guarantees the value never exceeds the range +-1.
        //
        // The DC motors are scaled to make it easier to control them at slower
        // speeds.
        //
        // The setPower methods write the motor power values to the DcMotor
        // class, but the power levels aren't applied until this method ends.
        //

        //
        // Manage the drive wheel motors.
        //

        float ratioDrive = (float) 0.5;

        // manipulate to round values in order to avoid oversensitivity with motor values
        float oversensitiveleftstick = (float) 10.0*gamepad1.left_stick_y;
        float Motor_left_stick_y = Math.round(oversensitiveleftstick/((float) 10.0));

        float oversensitiverightstick = (float) 10.0*gamepad1.right_stick_y;
        float Motor_right_stick_y = Math.round(oversensitiverightstick/((float) 10.0));

        // bounding values of motor to avoid burnout
        float cutoff = (float) 0.7;
        if (Motor_left_stick_y < -cutoff) {
            Motor_left_stick_y = -cutoff;
        }
        if (Motor_right_stick_y < -cutoff) {
            Motor_right_stick_y = -cutoff;
        }

        // bounding values of motor to avoid burnout
        if (Motor_left_stick_y > cutoff) {
            Motor_left_stick_y = cutoff;
        }
        if (Motor_right_stick_y > cutoff) {
            Motor_right_stick_y = cutoff;
        }

        float l_left_drive_power = scale_motor_power (-ratioDrive*Motor_left_stick_y);
        float l_right_drive_power = scale_motor_power (-ratioDrive*Motor_right_stick_y);


        set_drive_power (l_left_drive_power, l_right_drive_power);

        //
        // Manage the arm motor.
        //
        float ratioArm = (float) 0.9;

        float oversensitivearmstick = (float) 10.0*gamepad2.left_stick_y;
        float Arm_left_stick_y = Math.round(oversensitivearmstick/((float) 10.0));

        if (Arm_left_stick_y < -cutoff) {
            Arm_left_stick_y = -cutoff;
        }

        if (Arm_left_stick_y > cutoff) {
            Arm_left_stick_y = cutoff;
        }

        float l_left_arm_power = scale_motor_power (-ratioArm*Arm_left_stick_y);
        m_left_arm_power (l_left_arm_power);


        //----------------------------------------------------------------------
        //
        // Servo Motors
        //
        // Obtain the current values of the gamepad 'x' and 'b' buttons.
        //
        // Note that x and b buttons have boolean values of true and false.
        //
        // The clip method guarantees the value never exceeds the allowable range of
        // [0,1].
        //
        // The setPosition methods write the motor power values to the Servo
        // class, but the positions aren't applied until this method ends.
        //
        double addvalue = 0.01;

        if (gamepad2.x) {
            m_hand_position (a_hand_position () + addvalue);
        }
        else if (gamepad2.b) {
            m_hand_position (a_hand_position () - addvalue);
        }

        if (gamepad2.y){
            double val = fence_hand_position_A() + addvalue;
            if (val > 0.5){
                val = 0.5;
            }
            fence_hand_position(val);
        }
        else if (gamepad2.a){
            double val = fence_hand_position_A() - addvalue;
            if (val < 0.1) {
                val = 0.1;
            }
            fence_hand_position(val);
        }

        //
        // Send telemetry data to the driver station.
        //
        update_telemetry (); // Update common telemetry
        update_gamepad_telemetry ();

    } // loop

} // PushBotManual

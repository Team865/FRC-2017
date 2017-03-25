package ca.warp7.robot.controls;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

import static ca.warp7.robot.controls.Control.DOWN;
import static ca.warp7.robot.controls.Control.PRESSED;
import static ca.warp7.robot.controls.Control.RELEASED;
import static ca.warp7.robot.controls.Control.UP;

import edu.wpi.first.wpilibj.XboxController;

public class XboxControllerPlus {

	private XboxController controller;
	private boolean a;
	private boolean b;
	private boolean x;
	private boolean y;
	private boolean lb;
	private boolean rb;
	private boolean lt;
	private boolean rt;
	private boolean ls;
	private boolean rs;
	private boolean start;
	private boolean back;
	private int dpad;
	
	
	public XboxControllerPlus(int port) {
		controller = new XboxController(port);
		a = false;
		b = false;
		x = false;
		y = false;
		lb = false;
		rb = false;
		lt = false;
		rt = false;
		ls = false;
		rs = false;
		start = false;
		back = false;
		dpad = -1;
	}
	
	public Control getAButton(){
		boolean a = this.a;
		boolean b = controller.getAButton();
		this.a = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getBButton(){
		boolean a = b;
		boolean b = controller.getBButton();
		this.b = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getXButton(){
		boolean a = x;
		boolean b = controller.getXButton();
		x = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getYButton(){
		boolean a = y;
		boolean b = controller.getYButton();
		y = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getBumper(Hand h){
		boolean a;
		if(h == Hand.kLeft)
			a = lb;
		else
			a = rb;
		boolean b = controller.getBumper(h);
		if(h == Hand.kLeft)	
			lb = b;
		else
			rb = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getTrigger(Hand h){
		boolean a;
		if(h == Hand.kLeft)
			a = lt;
		else
			a = rt;
		boolean b = controller.getTriggerAxis(h) >= 0.5;
		if(h == Hand.kLeft)	
			lt = b;
		else
			rt = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getStickButton(Hand h){
		boolean a;
		if(h == Hand.kLeft)
			a = ls;
		else
			a = rs;
		boolean b = controller.getStickButton(h);
		if(h == Hand.kLeft)	
			ls = b;
		else
			rs = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getStartButton(){
		boolean a = start;
		boolean b = controller.getStartButton();
		start = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getBackButton(){
		boolean a = back;
		boolean b = controller.getBackButton();
		back = b;
		
		if(b != a)
			if(b)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b)
				return DOWN;
			else
				return UP;
	}
	
	public Control getDpad(int value){
		int a = dpad;
		int b = controller.getPOV(0);
		dpad = b;
		
		if(b != a)
			if(b >= 0)
				return PRESSED;
			else
				return RELEASED;
		else
			if(b >= 0)
				return DOWN;
			else
				return UP;
	}

	public void setRumble(RumbleType type, int value) {
		controller.setRumble(type, value);
	}

	public double getX(Hand hand) {
		return controller.getX(hand);
	}
	
	public double getY(Hand hand) {
		return controller.getY(hand);
	}
}

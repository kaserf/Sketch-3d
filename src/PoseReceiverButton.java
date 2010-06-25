

import java.util.Timer;
import java.util.TimerTask;

import ubitrack.SimplePose;

public class PoseReceiverButton extends PoseReceiver {

	private static final long TIME_OUT = 1000;
	
	private final ButtonObject buttonObject;
	private final MotionInterpolator motionInterpolator;
	
	private long timeStamp = System.currentTimeMillis();
	private boolean statePressed;
	private boolean initialized = false;
	public PoseReceiverButton(ButtonObject bObject, MotionInterpolator mInterpolator) {
		this.buttonObject = bObject;
		this.motionInterpolator = mInterpolator;
		
		Timer t = new Timer();
		TimerTask tt = new TimerTask(){
			@Override
			public void run() {
				if(!initialized)
					return;
				long currTime = System.currentTimeMillis();
				
				if (currTime - timeStamp > TIME_OUT) {
					if (!statePressed) {
						
						statePressed = true;
						buttonObject.buttonPressed(true);
						motionInterpolator.setActive(true);
						System.out.println("Button pressed");
					}
				} else {
					if (statePressed) {
						statePressed = false;			
						buttonObject.buttonPressed(false);
						System.out.println("Button released");
					}
				}
			}
		};
		t.scheduleAtFixedRate(tt, 0, TIME_OUT);
	}
	
	@Override
	public void receivePose(SimplePose pose) {
		super.receivePose(pose);
		initialized =true;
		timeStamp = System.currentTimeMillis();
	}

}

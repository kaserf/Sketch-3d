
import ubitrack.SimpleFacade;
import ubitrack.SimpleImageReceiver;
import ubitrack.SimplePoseReceiver;
import ubitrack.ubitrack;

public class UbitrackFacade {
	
	static {
		System.loadLibrary("ubitrack_java");
	}

	private SimpleFacade facade;

	public void initUbitrack() {
		ubitrack.initLogging();
		
		facade = new SimpleFacade(Exercise4.COMPONENT_DIRECTORY);
		
		if (facade.getLastError() != null) {
			return;
		}
		if (!facade.loadDataflow(Exercise4.DATAFLOW_PATH)) {
			return;
		}
	}
	
	public boolean setPoseCallback(String name, SimplePoseReceiver poseReceiver){
		return facade.setPoseCallback(name, poseReceiver);
	}
	
	public boolean setImageCallback(String name, SimpleImageReceiver imageReceiver){
		return facade.setImageCallback(name, imageReceiver);
	}

	public void startDataflow(){
		facade.startDataflow();
	}
	public void stopUbitrack() {
		System.out.println("stopUbitrack");
		facade.stopDataflow();
		 
		// Garbage collection for cleanup of native Ubitrack stuff
		System.gc();
		System.runFinalization();
	}
	
}

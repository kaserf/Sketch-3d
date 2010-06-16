import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;


public class MotionInterpolator  implements Notified  {
	
	ButtonObject button;
	private final TransformGroup startTG;
	private final TransformGroup destTG;
	private final TransformGroup BTG;
	private final Transform3D start3D = new Transform3D();
	private final Transform3D dest3D = new Transform3D();
	private final Transform3D B3D = new Transform3D();
	private boolean isActive= false;
	private boolean startIsstart = true;
	private boolean onbutton=false;

	public MotionInterpolator(TransformGroup targetTG, ButtonObject button)
	{
		startTG = new NotifyTransformGroup(this);
		destTG = new NotifyTransformGroup(this);
		this.button=button;
		this.BTG=button.getTransformGroup();
		this.targetTG=targetTG;
	}

	public TransformGroup getStartTG() {
		return startTG;
	}


	public TransformGroup getDestTG() {
		return destTG;
	}


	TransformGroup targetTG;


	public void notifyMe() {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_BOUNDS_WRITE);
		Transform3D b3d2 = new Transform3D();
		BTG.getTransform(b3d2);
		
		Transform3D start3d2 = new Transform3D();
		startTG.getTransform(start3d2);			

		Transform3D dest3d2 = new Transform3D();
		destTG.getTransform(dest3d2);			

		if (!isActive) {
			if(onbutton)
			{
				BTG.getTransform(B3D);
				targetTG.setTransform(B3D);
			}
			else if (startIsstart) {
	
				startTG.getTransform(start3D);
				targetTG.setTransform(start3D);
			} else {
				destTG.getTransform(dest3D);
				targetTG.setTransform(dest3D);

			}
		}
		if(isActive){

			if(!onbutton)
			{
				if(startIsstart)
				{
					Vector3f px = new Vector3f();
					b3d2.get(px);
					Vector3f py = new Vector3f();
					start3d2.get(py);
					px.sub(py);
	
					
					if (px.length()<0.08) 
					{
						button.buttonPressed(true);			
						targetTG.setTransform(b3d2);
						onbutton=true;
						startIsstart=!startIsstart;

					}
					else
					{
						button.buttonPressed(false);
						targetTG.setTransform(start3d2);
					}				


				}
				else
				{
					Vector3f px = new Vector3f();
					b3d2.get(px);
					Vector3f py = new Vector3f();
					dest3d2.get(py);
					px.sub(py);
					if (px.length()<0.08) 
					{
						button.buttonPressed(true);
						targetTG.setTransform(b3d2);
						onbutton=true;
						startIsstart=!startIsstart;
					}
					else
					{
						button.buttonPressed(false);
						targetTG.setTransform(dest3d2);

					}
				}


			}
			//Sheep on Button
			else
			{
				if(startIsstart)
				{
					Vector3f px = new Vector3f();
					b3d2.get(px);
					Vector3f py = new Vector3f();
					start3d2.get(py);
					px.sub(py);
					if (px.length()<0.08) 
					{
						button.buttonPressed(true);
						targetTG.setTransform(start3d2);
						onbutton=false;
					}
					else
					{
						button.buttonPressed(false);
						targetTG.setTransform(b3d2);
					}				
				}
				else
				{
					Vector3f px = new Vector3f();
					b3d2.get(px);
					Vector3f py = new Vector3f();
					dest3d2.get(py);

					px.sub(py);
					if (px.length()<0.08) 
					{
						button.buttonPressed(true);
						targetTG.setTransform(dest3d2);
						onbutton=false;

					}
					else
					{
						button.buttonPressed(false);
						targetTG.setTransform(b3d2);

					}
				}

			}
			setActive(false);

		}	

	}
	public void setActive(boolean flag)
	{
		isActive=flag;

	}
}

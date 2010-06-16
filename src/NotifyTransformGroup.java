package de.tum.in.far.threedui.ex4.solution.amal;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

public class NotifyTransformGroup extends TransformGroup {

	private Notified notified;
	
	public NotifyTransformGroup(Notified notified) {
		this.notified = notified;
	}
	
	@Override
	public void setTransform(Transform3D arg0) {
		super.setTransform(arg0);
		notified.notifyMe();
	}

	
	
}



import java.awt.image.BufferedImage;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ImageComponent2D;
import javax.vecmath.Point3d;

public class BackgroundObject extends BranchGroup {

	private final Background background;
	private BufferedImage bgImage;
	
	public BackgroundObject() {
		background = new Background();
		bgImage = new BufferedImage(ImageReceiver.X_PIXEL, ImageReceiver.Y_PIXEL, BufferedImage.TYPE_BYTE_GRAY);
		ImageComponent2D ic = new ImageComponent2D(ImageComponent2D.FORMAT_CHANNEL8, bgImage, true, false);
		background.setImage(ic);
		background.setCapability(Background.ALLOW_IMAGE_WRITE);
		background.setImageScaleMode(Background.SCALE_FIT_ALL);
		background.setApplicationBounds(new BoundingSphere(new Point3d(), 100.0));
		addChild(background);
	}

	public Background getBackground() {
		return background;
	}
	
	
}

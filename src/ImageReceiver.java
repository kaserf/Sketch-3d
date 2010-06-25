
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

import javax.media.j3d.Background;
import javax.media.j3d.ImageComponent2D;

import ubitrack.SWIGTYPE_p_signed_char;
import ubitrack.SimpleImage;
import ubitrack.SimpleImageReceiver;
import ubitrack.ubitrack;

/**
 * A {@link SimpleImageReceiver} implementation copying
 * received image data to a {@link Background} image.
 * @author Jan Schlüter
 */
public class ImageReceiver extends SimpleImageReceiver {

	public static final int X_PIXEL = 320;
	public static final int Y_PIXEL = 240;
	
	/**
	 * SKIP_FRAMES defines how many frames are skipped before
	 * another one is processed. Set this to 0 to display all
	 * frames, to 4 to display each 5th frame etc. Useful if
	 * your computer is not fast enough to process all frames
	 * in time.
	 */
	private static final int SKIP_FRAMES = 0;
	
	private final int _width, _height;
	private final byte[] _imgData;
	private final DataBufferByte _dataBuffer;
	private final WritableRaster _raster;
	private final BufferedImage _bgImage;
	
	private Background _background = null;
	
	/**
	 * Part of the better solution that does not work,
	 * but should be more performant: 
	private ImageComponent2D _ic = null;
	private final ImageComponent2D.Updater _updater;
	*/
	
	private int _count = 0;

	/**
	 * Create a new image receiver instance for images
	 * of given resolution.
	 */
	public ImageReceiver() {
		this(X_PIXEL, Y_PIXEL);
	}
	
	/**
	 * Create a new image receiver instance for images of
	 * a given dimension.
	 * @param width The width of the images to be processed
	 * @param height The height of the images to be processed
	 */
	public ImageReceiver(int width, int height) {
		_width = width;
		_height = height;
		int calcSize = _width * _height;
		_imgData = new byte[calcSize];		
		_dataBuffer = new DataBufferByte(_imgData, _imgData.length);
		int[] bitmask = {0xFF};
		_raster = Raster.createWritableRaster(new
				SinglePixelPackedSampleModel(
						DataBuffer.TYPE_BYTE, _width, _height, bitmask), _dataBuffer,
						new Point(0, 0));
		_bgImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		_bgImage.setData(_raster);
		/*
		 * Part of the better solution that does not work: 
		_ic = new ImageComponent2D(
				ImageComponent2D.FORMAT_CHANNEL8, _bgImage, true, true);
		_ic.setCapability(ImageComponent2D.ALLOW_IMAGE_WRITE);
		_updater = new ImageComponent2D.Updater() {
			public void updateData(ImageComponent2D imageComponent, int x, int y, int width, int height) {
				_bgImage.setData(_raster);
			}
		};
		*/
	}

	/**
	 * Setup a {@link Background} this image receiver should update
	 * on receiving new frames.
	 * @param background The background to update
	 */
	public void setBackground(Background background) {
		if (!background.isLive() && !background.isCompiled()) {
			background.setCapability(Background.ALLOW_IMAGE_WRITE);
		}
		_background = background;
		/*
		 * Part of the better solution that does not work: 
		_background.setImage(_ic);
		 */
	}

	public void receiveImage(SimpleImage image) {
		//BigInteger ts = image.getTimestamp();
//		System.out.println("Received image..." + _count);
		if ((_background != null) && (_count % (SKIP_FRAMES + 1) == SKIP_FRAMES)) {			
			SWIGTYPE_p_signed_char data_ptr = image.getImageData();
			int calcSize = _width * _height;
			for (int i = 0; i < calcSize; i++) {
				byte b = ubitrack.byteArray_getitem(data_ptr, i);
				_imgData[i] = b;
			}
			/*
			 * Part of the better solution that does not work: 
			 * Instead of the two commands below, just issue:
			_ic.updateData(_updater, 0, 0, _width, _height);
			*/
			_bgImage.setData(_raster);
			_background.setImage(new ImageComponent2D(
					ImageComponent2D.FORMAT_CHANNEL8, _bgImage, true, true));
//			System.out.println("Background set to image " + _count);
		}
		_count = _count + 1;
	}
	
	
}
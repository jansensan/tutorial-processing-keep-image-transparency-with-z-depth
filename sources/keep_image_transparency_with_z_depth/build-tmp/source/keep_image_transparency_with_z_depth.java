import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class keep_image_transparency_with_z_depth extends PApplet {

//===========/----------------------------------------------
//  [_TBL]  /  Table of Contents
//=========/------------------------------------------------
/*
[_CON]	Constants
[_VAR]	Variables
[_SET]	Setup
[_DRW]	Draw Loop
[_MTD]	Methods
*/


//===========/----------------------------------------------
//  [_CON]  /  Constants
//=========/------------------------------------------------

int FPS = 60;
int BG_COLOR = color(160);

int NUM_IMAGES = 32;
float RISING_SPEED = 0.5f;
float ROTATION_INCREMENT = 0.15f;


//===========/----------------------------------------------
//  [_VAR]  /  Variables
//=========/------------------------------------------------

ArrayList<ImageVO> imageVOs;
ArrayList<ImageAsset> images;


//===========/----------------------------------------------
//  [_SET]  /  Setup
//=========/------------------------------------------------

public void setup()
{
	size(1280, 720, P3D);
	background(BG_COLOR);
	frameRate(FPS);

	setupImageVOs();
	addImages();

	// println("before sort:");
	// printImagesZ(images);
	sortImagesBackToFront();
	// println("after sort:");
	// printImagesZ(images);
}


//===========/----------------------------------------------
//  [_DRW]  /  Draw loop
//=========/------------------------------------------------

public void draw()
{
	background(BG_COLOR);
	drawImages();
}


//===========/----------------------------------------------
//  [_MTD]  /  Methods
//=========/------------------------------------------------

/**
 * Instanciates the value objects ArrayList
 */
public void setupImageVOs()
{
	imageVOs = new ArrayList<ImageVO>();
	for(int i = 0; i < NUM_IMAGES; i++)
	{
		imageVOs.add(new ImageVO("pirate.png", 148, 256));
	}
}


/**
 * Sets the image position and rotation values.
 * Called only once, when image is repositioned after going out of screen.
 */
public void setImagePosition(ImageAsset img)
{
	img.x = random(width);
	img.y = img.maxY + random(height);
	img.z = 256 - random(512); 
	img.rotation = random(360);
}


/**
 * Instanciates the images asset ArrayList.
 */
public void addImages()
{
	images = new ArrayList<ImageAsset>();
	for(int i = 0; i < NUM_IMAGES; i++)
	{
		ImageVO vo = imageVOs.get(i);
		
		ImageAsset img = new ImageAsset(vo.imageURL, vo.imageWidth, vo.imageHeight);

		// set the maximum positions the image can reach out of screen
		img.minY = -img.imageHeight();
		img.maxY = height + img.imageHeight();

		setImagePosition(img);

		images.add(img);
	}
}


/**
 * Draws the image asset according to the image value object values.
 */
public void drawImage(ImageAsset img)
{
	pushMatrix();

	translate(img.x, img.y, img.z);		// move anchor point to object position
	rotate(radians(img.rotation));		// apply rotation
	translate(-img.x, -img.y, img.z);	// move back anchor to initial position

	img.draw();

	popMatrix();

	img.y -= RISING_SPEED;
	if(img.y <= img.minY)
	{
		setImagePosition(img);
		sortImagesBackToFront();
	}
	img.rotation += ROTATION_INCREMENT;
}


/**
 * Loops through the image asset ArrayList to draw them.
 */
public void drawImages()
{
	for(int i = 0; i < NUM_IMAGES; i++)
	{
		ImageAsset img = images.get(i);
		drawImage(img);
	}
}


/**
	Sorts images array according to their z depth.

	For transparency to work properly for images with a z depth,
	images have to be drawn from the back to the front.

	See how 3D works in Processing: http://processing.org/tutorials/p3d/
	See the thread on the Processing forum: https://forum.processing.org/topic/how-to-fix-transparency-issue-with-images-with-z-depths
*/
public void sortImagesBackToFront()
{

	// create an ArrayList that will contain the sorted items
	ArrayList<ImageAsset> sortedImages = new ArrayList<ImageAsset>();

	// loop through the unsorted items
	for(int i = 0; i < NUM_IMAGES; i++)
	{
		ImageAsset current = images.get(i);

		// add the first unsorted item to the sorted list
		if(i == 0)
		{
			sortedImages.add(current);
		}
		else
		{
			int numJLoops = sortedImages.size();
			
			// loop through the sorted items to compare with current item
			jLoop:
			for(int j = 0; j < numJLoops; j++)
			{
				ImageAsset sorted = sortedImages.get(j);
				
				// if value is higher
				if(current.compareZWith(sorted) > 0)
				{
					// if last sorted item
					if(j == numJLoops - 1)
					{
						sortedImages.add(current);
						break jLoop;
					}
				}
				// if value is lower or equal
				else
				{
					sortedImages.add(j, current);
					break jLoop;
				}
			}
		}
	}
	images = sortedImages;
}


/**
 * Prints the image assets z value to the console.
 */
public void printImagesZ(ArrayList<ImageAsset> l)
{
	int numLoops = l.size();
	for(int i = 0; i < numLoops; i++)
	{
		ImageAsset asset = l.get(i);
		println("image z: " + asset.z);
	}
}
class ImageAsset
{
	float x = 0;
	float y = 0;
	float z = 0;
	float maxY;
	float minY;
	float rotation = 0;

	private PImage _image;
	private int _imageWidth;
	private int _imageHeight;


	//===========/----------------------------------------------
	//  [_CON]  /  Constructor
	//=========/------------------------------------------------

	public ImageAsset	(
							String imageURL, 
							int imageWidth, 
							int imageHeight
						)
	{
		_image = loadImage(imageURL);
		_imageWidth = imageWidth;
		_imageHeight = imageHeight;
	}


	//===========/----------------------------------------------
	//  [_PUB]  /  Public methods
	//=========/------------------------------------------------

	/**
	 * Draw the image asset.
	 */
	public void draw()
	{
		imageMode(CENTER);
		image(_image, x, y, _imageWidth, _imageHeight);
	}


	/**
	 * Compares z value with another image asset.
	 */
	public int compareZWith(ImageAsset comparable)
	{
		// value is smaller
		if(z < comparable.z)
		{
			return -1;
		}
		
		// value is bigger
		if(z > comparable.z)
		{
			return 1;
		}
		
		// value is the same
		return 0;
	}


	//===========/----------------------------------------------
	//  [_GTR]  /  Getters
	//=========/------------------------------------------------

	/**
	 * Returns image width.
	 */
	public int imageWidth()
	{
		return _imageWidth;
	}


	/**
	 * Returns image height.
	 */
	public int imageHeight()
	{
		return _imageHeight;
	}
}
class ImageVO
{
	String imageURL;
	int imageWidth;
	int imageHeight;


	//===========/----------------------------------------------
	//  [_CON]  /  Constructor
	//=========/------------------------------------------------

	public ImageVO	(
						String url, 
						int w, 
						int h
					)
	{
		imageURL = url;
		imageWidth = w;
		imageHeight = h;
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "keep_image_transparency_with_z_depth" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

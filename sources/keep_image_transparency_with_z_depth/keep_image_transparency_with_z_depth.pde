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
color BG_COLOR = color(160);

int NUM_IMAGES = 32;
float RISING_SPEED = 0.5;
float ROTATION_INCREMENT = 0.15;


//===========/----------------------------------------------
//  [_VAR]  /  Variables
//=========/------------------------------------------------

ArrayList<ImageVO> imageVOs;
ArrayList<ImageAsset> images;


//===========/----------------------------------------------
//  [_SET]  /  Setup
//=========/------------------------------------------------

void setup()
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

void draw()
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
void setupImageVOs()
{
	imageVOs = new ArrayList<ImageVO>();
	for(int i = 0; i < NUM_IMAGES; i++)
	{
		// Pirate image is taken from Iain Lobb's "PirateMark" benchmark
		imageVOs.add(new ImageVO("pirate.png", 148, 256));
	}
}


/**
 * Sets the image position and rotation values.
 * Called only once, when image is repositioned after going out of screen.
 */
void setImagePosition(ImageAsset img)
{
	img.x = random(width);
	img.y = img.maxY + random(height);
	img.z = 256 - random(512); 
	img.rotation = random(360);
}


/**
 * Instanciates the images asset ArrayList.
 */
void addImages()
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
void drawImage(ImageAsset img)
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
void drawImages()
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
void sortImagesBackToFront()
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
void printImagesZ(ArrayList<ImageAsset> l)
{
	int numLoops = l.size();
	for(int i = 0; i < numLoops; i++)
	{
		ImageAsset asset = l.get(i);
		println("image z: " + asset.z);
	}
}
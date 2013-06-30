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
	void draw()
	{
		imageMode(CENTER);
		image(_image, x, y, _imageWidth, _imageHeight);
	}


	/**
	 * Compares z value with another image asset.
	 */
	int compareZWith(ImageAsset comparable)
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
	int imageWidth()
	{
		return _imageWidth;
	}


	/**
	 * Returns image height.
	 */
	int imageHeight()
	{
		return _imageHeight;
	}
}
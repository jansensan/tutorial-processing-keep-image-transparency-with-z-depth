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
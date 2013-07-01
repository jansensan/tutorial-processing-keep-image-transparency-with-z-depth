Keep image transparency with z depth
====================================

In order to display an image in [Processing](http://processing.org/), you need to create a [`PImage`](http://processing.org/reference/PImage.html) instance. Draw the image in the [`draw`](http://processing.org/reference/draw_.html) loop. Don't forget to also draw a [`background`](http://processing.org/reference/background_.html) so the image is not just drawn on top of itself infinitely.

There may be moments when you need to display images at different depths, using Processing's [3D/`P3D`](http://processing.org/tutorials/p3d/). This technique provides a natural parallax effect.

However, simply creating images and drawing them with a `z` depth will not apply the transparency properly over all the images, see the example below.

![image](https://raw.github.com/jansensan/tutorial-processing-keep-image-transparency-with-z-depth/master/assets/unsorted.jpg)

When I faced this situation, I asked the question on the Processing forums and on Twitter. It seems that the order in which the images with transparency are drawn is important. You can read a [detailed technical explanation](http://www.sjbaker.org/steve/omniv/alpha_sorting.html) about it.

Personaly I believe dealing with many images in Processing is a bit of a mess, so I decided to create a [class](https://github.com/jansensan/tutorial-processing-keep-image-transparency-with-z-depth/blob/master/sources/keep_image_transparency_with_z_depth/ImageAsset.pde) that contains the image and the data related to it.

From then, it became easier to create a function to sort those images according to their `z` value, so they could be drawn from back to front.

See the results below. 

![image](https://raw.github.com/jansensan/tutorial-processing-keep-image-transparency-with-z-depth/master/assets/sorted.jpg)

Don't hesitate to look at the [code](https://github.com/jansensan/tutorial-processing-keep-image-transparency-with-z-depth/tree/master/sources/keep_image_transparency_with_z_depth) and use it if you need anything like that.

**Credits**:<br>
Pirate illustration by [Amanda Lobb](http://amandalobb.com/)
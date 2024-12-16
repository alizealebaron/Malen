package malen.modele;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Rotation
{
	public static BufferedImage rotateImage(BufferedImage image, double angleDegrees)
	{
        int width = image.getWidth();
        int height = image.getHeight();
		double angleRadians = Math.toRadians(angleDegrees);

		// Calculer les nouvelles dimensions après la rotation
		int newWidth = (int) Math.abs(width * Math.cos(angleRadians)) + (int) Math.abs(height * Math.sin(angleRadians));
		int newHeight = (int) Math.abs(width * Math.sin(angleRadians)) + (int) Math.abs(height * Math.cos(angleRadians));

		BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, image.getType());
        
		// Calculer le centre de l'image source
		int centerX = width / 2;
		int centerY = height / 2;

		for (int i = 0; i < newWidth; i++)
		{
			for (int j = 0; j < newHeight; j++)
			{
				int xPrime = i - newWidth / 2;
				int yPrime = j - newHeight / 2;

				int x = (int) Math.round(xPrime * Math.cos(angleRadians) - yPrime * Math.sin(angleRadians));
				int y = (int) Math.round(xPrime * Math.sin(angleRadians) + yPrime * Math.cos(angleRadians));

				int srcX = x + centerX;
				int srcY = y + centerY;

				if (srcX >= 0 && srcX < width && srcY >= 0 && srcY < height)
				{
					rotatedImage.setRGB(i, j, image.getRGB(srcX, srcY));
				}
				else
				{
					rotatedImage.setRGB(i, j, 0xFF000000); // couleur noire pour le moment
				}
			}
		}
		try {
			ImageIO.write(rotatedImage, "png", new File("rotated_image_rsamp.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return rotatedImage;
    }
}

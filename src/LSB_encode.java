import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;


public class LSB_encode {
	static final String SECRET_MESSAGE_FILE = "C:\\Users\\user\\Pictures\\message.txt";
	static final String COVER_FILE = "C:\\Users\\user\\Pictures\\original.png";
	static final String STEGO_FILE = "C:\\Users\\user\\Pictures\\final.png";

	public static void main(String[] args) throws Exception {
		int[] bits = getBits(readText());
		
		BufferedImage theImage = readCoverFile(COVER_FILE);
		encode(bits, theImage);
	}

	public static String readText() throws FileNotFoundException {
		String contentOfMessageFile = "";
		try {
			File file = new File(SECRET_MESSAGE_FILE);
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				contentOfMessageFile += next;
				
				if (scanner.hasNextLine()) {
					contentOfMessageFile += "\n";
				}
				
				System.out.println("contentOfMessageFile ----- " + contentOfMessageFile);
			}
			
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
			contentOfMessageFile = null;
		}
		
		return contentOfMessageFile;
	}

	public static int[] getBits(String text) {
		int count = 0;
		int[] textBits = new int[text.length() * 8];
		
		for (int i = 0; i < text.length(); i++) {
			String str = Integer.toBinaryString(text.charAt(i));
			
			while (str.length() != 8) {
				str = '0' + str;
			}

			for (int j = 0; j < 8; j++) {
				textBits[count] = Integer.parseInt(String.valueOf(str.charAt(j)));
				count++;
			}
		}
		
		return textBits;
	}

	public static BufferedImage readCoverFile(String COVERIMAGEFILE) {
		BufferedImage theImage = null;
		File p = new File(COVERIMAGEFILE);
		try {
			theImage = ImageIO.read(p);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return theImage;
	}

	public static void encode(int[] bits, BufferedImage theImage) throws Exception {
		File file = new File(STEGO_FILE);
		int fileBits = bits.length / 8;
		int[] msgBits = new int[8];

		String str = Integer.toBinaryString(fileBits);
		
		while (str.length() != 8) {
			str = '0' + str;
		}
		
		for (int i = 0; i < 8; i++) {
			msgBits[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
		}

		int j = 0;
		int b = 0;
		int currentBitEntry = 8;

		for (int x = 0; x < theImage.getWidth(); x++) {
			for (int y = 0; y < theImage.getHeight(); y++) {
				if (x == 0 && y < 8) {
					int currentPixel = theImage.getRGB(x, y);
					
					int red = currentPixel >> 16;
					red = red & 255;
					
					int green = currentPixel >> 8;
					green = green & 255;
					
					int blue = currentPixel;
					blue = blue & 255;
					
					String blueStr = Integer.toBinaryString(blue);
					String sten_s = blueStr.substring(0, blueStr.length() - 1);
					sten_s = sten_s + Integer.toString(msgBits[b]);

					int s_pixel = Integer.parseInt(sten_s, 2);
					int a = 255;
					int rgb = (a << 24) | (red << 16) | (green << 8) | s_pixel;
					
					theImage.setRGB(x, y, rgb);
					ImageIO.write(theImage, "png", file);
					b++;
				} else if (currentBitEntry < bits.length + 8) {
					int currentPixel = theImage.getRGB(x, y);
					
					int red = currentPixel >> 16;
					red = red & 255;
					
					int green = currentPixel >> 8;
					green = green & 255;
					
					int blue = currentPixel;
					blue = blue & 255;
					
					String blueStr = Integer.toBinaryString(blue);
					String sten_s = blueStr.substring(0, blueStr.length() - 1);
					sten_s = sten_s + Integer.toString(bits[j]);
					j++;
					
					int s_pixel = Integer.parseInt(sten_s, 2);

					int a = 255;
					int rgb = (a << 24) | (red << 16) | (green << 8) | s_pixel;
					theImage.setRGB(x, y, rgb);
					
					ImageIO.write(theImage, "png", file);

					currentBitEntry++;
				}
			}
		}
	}
}
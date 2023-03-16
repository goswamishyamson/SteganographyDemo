import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class LSB_decode {
	static final String STEGO_FILE = "C:\\Users\\user\\Pictures\\final.png";
	static final String DECODED_SECRET_MESSAGE_FILE = "C:\\Users\\user\\Pictures\\decode-message.txt";

	public static String b_msg = "";
	public static int len = 0;

	public static void main(String[] args) throws Exception {

		BufferedImage yImage = readImageFile(STEGO_FILE);

		DecodeTheMessage(yImage);
		String msg = "";
		for (int i = 0; i < len * 8; i = i + 8) {
			String sub = b_msg.substring(i, i + 8);
			int m = Integer.parseInt(sub, 2);
			char ch = (char) m;

			msg += ch;
		}
		PrintWriter out = new PrintWriter(new FileWriter(DECODED_SECRET_MESSAGE_FILE, true), true);
		out.write(msg);
		out.close();
	}

	public static BufferedImage readImageFile(String COVERIMAGEFILE) {
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

	public static void DecodeTheMessage(BufferedImage yImage) throws Exception {
		int currentBitEntry = 0;
		String bx_msg = "";
		for (int x = 0; x < yImage.getWidth(); x++) {
			for (int y = 0; y < yImage.getHeight(); y++) {
				if (x == 0 && y < 8) {
					int currentPixel = yImage.getRGB(x, y);
					int red = currentPixel >> 16;
					red = red & 255;
					int green = currentPixel >> 8;
					green = green & 255;
					int blue = currentPixel;
					blue = blue & 255;
					String x_s = Integer.toBinaryString(blue);
					bx_msg += x_s.charAt(x_s.length() - 1);
					len = Integer.parseInt(bx_msg, 2);

				} else if (currentBitEntry < len * 8) {
					int currentPixel = yImage.getRGB(x, y);
					int red = currentPixel >> 16;
					red = red & 255;
					int green = currentPixel >> 8;
					green = green & 255;
					int blue = currentPixel;
					blue = blue & 255;
					String x_s = Integer.toBinaryString(blue);
					b_msg += x_s.charAt(x_s.length() - 1);

					currentBitEntry++;
					// System.out.println("curre "+currentBitEntry);
				}
			}
		}
		System.out.println("bin value of msg hided in img is " + b_msg);
	}
}

package mapDownloader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DonloadServlet
 */
@SuppressWarnings("serial")
@WebServlet("/MoveServlet")
public class MoveServlet extends HttpServlet {

	protected static final int IMAGE_SIZE = 256;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MoveServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int startX = Integer.parseInt(request.getParameter("startX"));
		int endX = Integer.parseInt(request.getParameter("endX"));
		int startY = Integer.parseInt(request.getParameter("startY"));
		int endY = Integer.parseInt(request.getParameter("endY"));
		String isVector = request.getParameter("isVector");
		String currentZ = request.getParameter("z");
		String format = request.getParameter("format");
		String fileName = request.getParameter("fileName");
		boolean needSkip = Integer.parseInt(request.getParameter("needSkip")) == 1;
		String Donloadbase = "";
		switch (isVector) {
		case "0":
			Donloadbase = DonloadServlet.DonloadbaseB;
			break;
		case "1":
			Donloadbase = DonloadServlet.DonloadbaseV;
			break;
		case "2":
			Donloadbase = DonloadServlet.DonloadbaseM;
			break;
		}
		File check = new File(Donloadbase + fileName + currentZ + "丨." + format);
		if (check.exists() && needSkip)
			return;
		String tmpsrcZ = Donloadbase + currentZ;
		for (int currentY = startY; currentY <= endY; currentY++) {
			String tmpsrcY = tmpsrcZ + File.separator + currentY;
			File check2 = new File(tmpsrcZ + File.separator + currentY + "丨");
			if (check2.exists() && needSkip)
				continue;
			BufferedImage DestImage = new BufferedImage((endX - startX + 1) * IMAGE_SIZE, IMAGE_SIZE,
					BufferedImage.TYPE_INT_RGB);
			for (int currentX = startX; currentX <= endX; currentX++) {
				String newPic = tmpsrcY + File.separator + currentX + "." + format;
				BufferedImage origin2 = ImageUtill.getBufferedImage(newPic);
				ImageUtill.mergeImage(DestImage, origin2, true, 0, (currentX - startX) * IMAGE_SIZE,
						isVector.equals("1"));
				origin2 = null;
			}
			ImageUtill.saveImage(DestImage, tmpsrcZ + File.separator, currentY + "丨", format);
			DestImage = null;
		}
		int n = (int) Math.floor(((long)(endX - startX + 1) * (long)(endY - startY + 1) ) / 15000.0);
		if (n == 0) {
			n = 1;
		}
		double factorY = ((endY - startY + 1) * 1.0 / n);
		int remainY = (endY - startY + 1) - (int) (Math.floor(factorY) * n);
		for (int i = 0; i < n; i++) {
			int tmpY = startY + (int) Math.floor(factorY) * i;
			endY = (int) (startY + Math.floor(factorY) * (i + 1));
			File check3 = new File(Donloadbase + File.separator + fileName + currentZ + "丨" + i + "." + format);
			if (check3.exists() && needSkip)
				continue;
			long width = (endX - startX + 1) * IMAGE_SIZE;
			if(width>Integer.MAX_VALUE) {
				throw new ServletException("合成图片width超限"+width);
			}
			long height = (long) (Math.floor(factorY) * IMAGE_SIZE);
			if(height>Integer.MAX_VALUE) {
				throw new ServletException("合成图片height超限"+ height);
			}
			BufferedImage DestImage = new BufferedImage((int)width,
					(int) height, BufferedImage.TYPE_INT_RGB);
			for (int currentY = tmpY; currentY < endY; currentY++) {
				String newPic = tmpsrcZ + File.separator + currentY + "丨";
				BufferedImage origin2 = ImageUtill.getBufferedImage(newPic);
				ImageUtill.mergeImage(DestImage, origin2, false, (currentY - tmpY) * IMAGE_SIZE, 0,
						isVector.equals("1"));
				origin2 = null;
			}
			ImageUtill.saveImage(DestImage, Donloadbase, fileName + currentZ + "丨" + i + "." + format, format);
			DestImage = null;
			Runtime.getRuntime().gc();
		}
		if (remainY == 0)
			return;
		int tmpY = endY;
		endY += remainY;
		BufferedImage DestImage = new BufferedImage((endX - startX + 1) * IMAGE_SIZE,
				((int) Math.floor(remainY)) * IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
		for (int currentY = tmpY; currentY < endY; currentY++) {
			String newPic = tmpsrcZ + File.separator + currentY + "丨";
			BufferedImage origin2 = ImageUtill.getBufferedImage(newPic);
			ImageUtill.mergeImage(DestImage, origin2, false, (currentY - tmpY) * IMAGE_SIZE, 0, isVector.equals("1"));
			origin2 = null;
		}
		ImageUtill.saveImage(DestImage, Donloadbase, fileName + currentZ + "丨" + n + "." + format, format);
		DestImage = null;

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
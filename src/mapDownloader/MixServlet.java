package mapDownloader;

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
@WebServlet("/MixServlet")
public class MixServlet extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MixServlet() {
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
		String currentZ = request.getParameter("z");
		String format = request.getParameter("format");
		boolean needSkip = Integer.parseInt(request.getParameter("needSkip")) == 1;
		String Donloadbase = DonloadServlet.DonloadbaseM;
		String tmpsrcZ = Donloadbase + currentZ;
		for (int currentY = startY; currentY <= endY; currentY++) {
			String tmpsrcY = tmpsrcZ + File.separator + currentY;
			File f = new File(tmpsrcY);
			if (!f.exists()) {
				f.mkdirs();
			}
			for (int currentX = startX; currentX <= endX; currentX++) {
				String oldPic = DonloadServlet.DonloadbaseB + currentZ + File.separator + currentY + File.separator
						+ currentX + "." + format;
				String newPic = tmpsrcY + File.separator + currentX + "." + format;
				File fs = new File(newPic);
				if (fs.exists()) {
					if (needSkip)
						continue;
				}
				FileUtil.moveFile(oldPic, newPic);
				String mixPic = DonloadServlet.DonloadbaseV + currentZ + File.separator + currentY + File.separator
						+ currentX + "." + format;
				ImageUtill.mixImageO(newPic, mixPic, format);
			}
		}
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
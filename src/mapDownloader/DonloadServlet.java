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
@WebServlet("/DonloadServlet")
public class DonloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final String DonloadbaseV = File.separator + "data" + File.separator + "DownLoad" + File.separator
			+ "MapV" + File.separator;
	protected static final String DonloadbaseB = File.separator + "data" + File.separator + "DownLoad" + File.separator
			+ "Map" + File.separator;
	protected static final String DonloadbaseM = File.separator + "data" + File.separator + "DownLoad" + File.separator
			+ "MapM" + File.separator;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DonloadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int startX = Integer.parseInt(request.getParameter("startX"));
		int endX = Integer.parseInt(request.getParameter("endX"));
		String urlZ = request.getParameter("urlZ");
		String urlY = request.getParameter("urlY");
		String urlX = request.getParameter("urlX");
		String currentY = request.getParameter("currentY");
		String currentZ = request.getParameter("z");
		String format = request.getParameter("format");
		String perfix = urlZ + currentZ + urlY + currentY;
		boolean isVector = Integer.parseInt(request.getParameter("isVector")) == 1;
		boolean needSkip = Integer.parseInt(request.getParameter("needSkip")) == 1;
		String Donloadbase = isVector ? DonloadbaseV : DonloadbaseB;
		for (int currentX = startX; currentX <= endX; currentX++) {
			String tmp = urlX.replaceFirst("丨X丨", currentX + "");
			String token = perfix + tmp;
			File b = new File(
					Donloadbase + currentZ + File.separator + currentY + File.separator + currentX + "." + format);
			if (b.exists() && b.length() != 0) {
				if (needSkip)
					continue;
			} else {
				File c = new File(Donloadbase + currentZ + File.separator + currentY);
				if (!c.exists())
					c.mkdirs();
				b.createNewFile();
			}
			Detect401 as = HttpRequestUtil.postDownTerrain(token, b);
			int count = 0;
			while (as != null && as.getCode() == 300) {
				as = HttpRequestUtil.postDownTerrain(token, b);
				count++;
				if (count > 20) {
					response.getWriter().write("401");
					response.getWriter().flush();
					response.getWriter().close();
					b.delete();
					return;
				}
			}
			count = 0;
			while (as != null && as.getCode() == 503) {
				as = HttpRequestUtil.postDownTerrain(token, b);
				count++;
				if (count > 20) {
					response.getWriter().write("401");
					response.getWriter().flush();
					response.getWriter().close();
					b.delete();
					return;
				}
			}
			if (as == null) {
				response.getWriter().write("500");
				response.getWriter().flush();
				response.getWriter().close();
				b.delete();
				return;
			}
			if (as.getCode() == 500) {
				System.out.println("z:" + currentZ + "x:" + currentX + "y:" + currentY + "远端服务器报错500已经跳过");
				b.delete();
				continue;
			}
			if (as.getCode() == 401) {
				response.getWriter().write("401");
				response.getWriter().flush();
				response.getWriter().close();
				b.delete();
				return;
			}
		}
		response.getWriter().write("200");
		response.getWriter().flush();
		response.getWriter().close();
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

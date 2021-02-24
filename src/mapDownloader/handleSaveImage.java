package mapDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Base64;

/**
 * Servlet implementation class handleSaveImage
 */
@WebServlet("/handleSaveImage")
public class handleSaveImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final String saveBase = "F:\\" + "DownLoad" + File.separator + "Map" + File.separator;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public handleSaveImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Base64.Decoder decoder = Base64.getDecoder();
		String src = request.getParameter("src");
		String image = request.getParameter("data");
		if(image == null) image =(String) request.getAttribute("data");
		if(image == null) image = request.getQueryString();
		image = image.substring(image.indexOf(",")+1);
		try {
			byte[] b = decoder.decode(image);
//			for(int i = 0;i<b.length;i++) {
//				if(b[i]<0) {
//					b[i] +=256;
//				}
//			}
			File img = new File(saveBase+src);
			if(!img.exists()) {	
				img.getParentFile().mkdirs();
				img.createNewFile();
			}
			FileOutputStream os = new FileOutputStream(img);
			os.write(b);
			os.flush();
			os.close();
			response.getWriter().write("200");
			response.getWriter().flush();
			response.getWriter().close();
		}catch(Exception e) {
			response.getWriter().write("500");
			response.getWriter().flush();
			response.getWriter().close();
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

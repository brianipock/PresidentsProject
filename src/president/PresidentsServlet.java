package president;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/presidents.do")
public class PresidentsServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();

		PresidentDAO dao = (PresidentDAO) session.getAttribute("dao");

		// if initial page load
		if (dao == null) {
			dao = new PresDAO(req.getServletContext());
			session.setAttribute("dao", dao);
		}

		String direction = req.getParameter("navigate"); // this is how a
															// servlet gets info
															// from a jsp
		// if button press
		if (direction != null) {
			switch (direction) {
			case "forward":
				dao.incrementIndex();
				break;
			case "back":
				dao.decrementIndex();
				break;
			case "getTerm":
				int i = 1;
				try {
					i = Integer.parseInt(req.getParameter("term"));
				} catch (NumberFormatException nfe) {
				}
				dao.setIndex(i - 1);
				break;
			case "filterText":
				String filter = req.getParameter("filter");
				if(dao.implementFilter(filter)){
					session.setAttribute("filter", filter);
				} else {
					session.setAttribute("filter", null);
				}
				break;
			}
		}
		
		String thumbnail = req.getParameter("thumbnail");
		if (thumbnail != null){
			dao.setIndex(Integer.parseInt(thumbnail));
		}

		RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/index.jsp");
		dispatcher.forward(req, resp);

	}
}
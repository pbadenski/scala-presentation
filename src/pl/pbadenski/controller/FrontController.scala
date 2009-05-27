package pl.pbadenski.controller

import javax.servlet.http._

class FrontController extends HttpServlet {
 	override def service(request : HttpServletRequest, response : HttpServletResponse) = {
		request
			.getRequestDispatcher(Mappings.process(request, response, request.getServletPath))
			.forward(request, response)
	}
}	

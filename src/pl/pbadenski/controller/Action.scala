package pl.pbadenski.controller

import javax.servlet.http._

trait Action {
	def request : HttpServletRequest
 
	def response : HttpServletResponse
}

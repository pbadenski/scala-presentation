package pl.pbadenski.controller

import collection._
import javax.servlet.http._

object Mappings {
	private val pathMappings = new mutable.ArrayBuffer[PartialFunction[String, ((HttpServletRequest, HttpServletResponse) => AnyRef, String)]]()

	def process(request : HttpServletRequest, response : HttpServletResponse, path : String) = {
	  def fillRequestAttributesWithResultProperties(result : AnyRef) = {
	    var properties = java.beans.Introspector.getBeanInfo(result.getClass).getPropertyDescriptors
	    properties.foreach { prop => prop match {
	      	case _ if prop.getName == "class" => ()
	      	case _ => request.setAttribute(prop.getName, prop.getReadMethod.invoke(result))
	    	}
	    }
	  }
	  
	  findAction(trimSlashesAndSuffix(path)) match {
	    case (action, jspFileForView) => 
	      val result = action(request, response)
	      fillRequestAttributesWithResultProperties(result);
	      jspFileForView
	  }
   	}

	def findAction(path : String) = pathMappings.reduceLeft { (f , s) => f orElse s }(path)
 
	def trimSlashesAndSuffix(path : String) = {
	  val Trimmer = """/?(.*)\.action""".r
	  path match {
	    case null => path
	    case _ => { val Trimmer(value) = path; value }
	  }
   }
}

class Mappings extends HttpServlet {
	{
	  Mappings.pathMappings += (path => path match {
	    case "customer/list" => 
	      Pair(
	        // _1
	        (req, res) => new CustomerController() {
	    	 val request = req
	    	 val response = res
	        }.list(),
	        // _2
           	fromWebInf(path + ".jsp"))
	  })  
	}
  
	private def fromWebInf(file : String) : String = "/WEB-INF/" + file;
  
	override def doPost(request : HttpServletRequest, response : HttpServletResponse) = ()
 
	override def doGet(request : HttpServletRequest, response : HttpServletResponse) = ()
}
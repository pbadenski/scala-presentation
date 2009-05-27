package pl.pbadenski.controller

import helpers.ScalaToJavaConversions._
import scala.reflect._

trait CustomerController extends Action {
	val repository = dataaccess.Repository
  
	def list() = {
	  new {
	    @BeanProperty val customers = repository.customers.toJavaList  
	  }
	}
}

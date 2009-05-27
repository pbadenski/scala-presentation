package pl.pbadenski.model.customer;
class Company(
  val name : String,
  val address : Address)	 {

  override def toString = List(name, address).mkString(", ")
}

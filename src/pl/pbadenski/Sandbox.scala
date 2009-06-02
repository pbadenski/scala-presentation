package pl.pbadenski

object Sandbox {
  def main(args : Array[String]) : Unit = {
//	def ++ = (i : Int) => i+1;
//	def -- = (i : Int) => i-1;
//	println(List(++, ++, ++, --).reduceLeft((f, g) => f andThen g)(0))
             
     case class I(b : Boolean) {
       def X = I(true)
     }
     case class F() extends I(false) 
     case class T() extends I(true)
     println("test")
     println(for (F() <- List(I(false), I(true), F())) yield true)
  }
}

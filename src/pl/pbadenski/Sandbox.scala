package pl.pbadenski

object Sandbox {
  def main(args : Array[String]) : Unit = {
	def ++ = (i : Int) => i+1;
	def -- = (i : Int) => i-1;
	println(List(++, ++, ++, --).reduceLeft((f, g) => f andThen g)(0)) 

	()
  }
}

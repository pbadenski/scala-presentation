package pl.pbadenski.helpers

object ScalaToJavaConversions {
	implicit def scala2JavaList[T](list : Seq[T]) = new {
	 def toJavaList = java.util.Arrays.asList(list.toArray: _*)
	}
}

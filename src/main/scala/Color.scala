class Color(var r: Float = 0, var g: Float = 0, var b: Float = 0, var a: Float = 1) {
	clamp()

	def clamp(): Unit = {
		if (r < 0) r = 0 else if (r > 1) r = 1 else r = r
		if (b < 0) b = 0 else if (b > 1) b = 1 else b = b
		if (g < 0) g = 0 else if (g > 1) g = 1 else g = g
		if (a < 0) a = 0 else if (a > 1) a = 1 else a = a
	}

	def +(that: Color): Color = 
		Color(this.r + that.r, this.g + that.g, this.b + that.b, this.a + that.a)

	def -(that: Color): Color = 
		Color(this.r - that.r, this.g - that.g, this.b - that.b, this.a - that.a)
	
	def *(that: Float): Color = 
		Color(this.r * that, this.g * that, this.b * that, this.a * that)

	def *(that: Double): Color = 
		Color((this.r * that).toFloat, (this.g * that).toFloat, (this.b * that).toFloat, (this.a*that).toFloat)
	
	def *(that: Color): Color = 
		Color(this.r * that.r, this.g * that.g, this.b * that.b, this.a * that.a)

	override def toString: String = s"Color($r, $g, $b, $a)"

	def toWritable: String = {
		var nr = (r * 255).toInt
		var ng = (g * 255).toInt
		var nb = (b * 255).toInt
		s"$nr $ng $nb "
	}

	def toWriteableRaw: String = {
		s"$r $g $b "
	}

	def +=(that: Color): Unit = {
		this + that
	}
}

object Color{
	def Red(): Color = Color(1, 0, 0, 1)
	def Green(): Color = Color(0, 1, 0, 1)
	def Blue(): Color = Color(0, 0, 1, 1)
	def White(): Color = Color(1, 1, 1, 1)
	def Black(): Color = Color(0, 0, 0, 1)
}

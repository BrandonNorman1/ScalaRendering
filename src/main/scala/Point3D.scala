case class Point3D(x: Double = 0, y: Double = 0, z: Double = 0, w: Double = 1) {	
	val values = Array(x, y, z, w)
	
	def apply(index: Int): Double = values(index)
	
	def negate: Point3D = {
		Point3D(-x, -y, -z)
	}
	
	def +(that: Vector3D): Point3D =
		Point3D(this.x + that.x, this.y + that.y, this.z + that.z)
	
	def -(that: Vector3D): Point3D =
		Point3D(this.x - that.x, this.y - that.y, this.z - that.z)
	}
	
	object Point3D{
		def distance(first: Point3D, second: Point3D): Double = {
			(math.sqrt(math.pow(first.x - second.x, 2) + math.pow(first.y - second.y, 2) + math.pow(first.z - second.z, 2))).toDouble
	}
}

case class Vector3D(x: Double = 0, y: Double = 0, z: Double = 0){
	val w = 0
	val values = Array(x, y, z, w)
	
	//Allows access via () operator
	def apply(index: Int): Double = values(index)
	
	def length: Double = math.sqrt(x*x + y*y + z*z)
	def lengthSquare: Double = x*x + y*y + z*z
	
	def normalize: Vector3D = {
		Vector3D(x/length, y/length, z/length)
	}
	
	def negate: Vector3D = 
		Vector3D(-x, -y, -z)
	
	def +(that: Vector3D): Vector3D =
		Vector3D(this.x + that.x, this.y + that.y, this.z + that.z)
	
	def -(that: Vector3D): Vector3D =
		Vector3D(this.x - that.x, this.y - that.y, this.z - that.z)
	
	def *(scalar: Double): Vector3D =
		Vector3D(x * scalar, y * scalar, z * scalar)
	
	def dot(that: Vector3D): Double = 
		this.x * that.x + this.y * that.y + this.z * that.z
	
	def project(a: Vector3D): Vector3D = {
		val d2 = a.lengthSquare
		val epsilon = 0.000001
		if(epsilon < d2){
			val d = this.dot(a) / d2
			var answer = Vector3D(0, 0, 0)
			if(epsilon < math.abs(d)){
			answer = a * d
			}
			answer
		}
		else{
			Vector3D(0, 0, 0)
		}
	}
	
	def reflect(n: Vector3D): Vector3D = {
		val normal = n.normalize
		val norm2 = normal * 2.0
		//println(norm2 * this.dot(normal))
		(norm2 * this.dot(normal) - this).negate
	}

	override def toString: String = s"Vector3D($x, $y, $z)"
}

object Vector3D{
	def apply(values: Array[Double]): Vector3D = Vector3D(values(0), values(1), values(2))
	def apply(conPoint: Point3D): Vector3D = Vector3D(conPoint.x, conPoint.y, conPoint.z)
	def apply(p1: Point3D, p2: Point3D): Vector3D = Vector3D(p2(0) - p1(0), p2(1) - p1(1), p2(2) - p1(2))
}
class Ray(var origin: Point3D, var direction: Vector3D){
    normalize()
    def normalize(): Unit = {
        val tmp = Vector3D(origin)
        val direct = direction - tmp
        direction = direct.normalize
    }
    override def toString: String = s"Ray($origin, $direction)"
}
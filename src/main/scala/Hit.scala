class Hit(var position: Point3D = new Point3D(), var normal: Vector3D = new Vector3D(), var material: Material = new Material()) {
    override def toString: String = s"Hit($position, $normal, $material)"
}

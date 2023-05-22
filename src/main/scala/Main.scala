object Main {
  def main(args: Array[String]): Unit = {
    var cam = Camera(Point3D(0, 0, -40), 50, 50, 1, RenderCallbackType.rayCast)

    var mtl_ambient = Color(0.01, 0.01, 0.01, 1.0)
    var mtl_diffuse_r = Color(1.00, 0.00, 0.00, 1.0)
    var mtl_diffuse_g = Color(0.00, 1.00, 0.00, 1.0)
    var mtl_diffuse_b = Color(0.00, 0.00, 1.00, 1.0)

    var materials: List[Material] = List(
      Material(ambient = mtl_ambient, diffuse = mtl_diffuse_r),
      Material(ambient = mtl_ambient, diffuse = mtl_diffuse_g),
      Material(ambient = mtl_ambient, diffuse = mtl_diffuse_b)
    )

    var objects: List[Shape] = List(
      Sphere(Point3D(10, 0, -10), 5, materials(0)),
      Box(Point3D(-5, -5, -5), Point3D(5, 5, 5), materials(1)),
      Sphere(Point3D(-10, 0, -10), 5, materials(2))
    )

    var lgt_amb = Color(0.01, 0.01, 0.01, 1.0);
    var lgt_diff = Color(0.75, 0.75, 0.75, 1.0);
    var lgt_spec = Color(0.50, 0.50, 0.50, 1.0);

    var lights: List[Light] = List(
      DirectionalLight(Vector3D(0, 0, -1), lgt_amb, lgt_diff, lgt_spec),
      SpotLight(Point3D(40, 40, 5), Vector3D(-1, 0.25, -1), 45, 1, lgt_amb, lgt_diff, lgt_spec)
    )

    var scene: List[List[SceneObject]] = List(
      objects,
      lights
    )

    cam.render(scene)
  }

  def msg = "I was compiled by Scala 3. :)"

}

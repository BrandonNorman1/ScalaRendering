class Material(var emission: Color = Color(), var ambient: Color = Color(), var diffuse: Color = Color(), var specular: Color = Color(), var shininess: Float = 1){
    def shade(ray: Ray, hit: Hit, lightDirection: Vector3D, lightDif: Color, lightSpec: Color): Color = {
        var n = hit.normal.normalize
        var l = lightDirection.normalize
        var r = l.reflect(n).normalize
        var v = ray.direction.negate
        var mat = max(hit.material.shininess, 0.0)

        var result = Color.Black()
        result = result + (hit.material.ambient * lightDif)
        var add =  lightDif * max(Math.abs(n.dot(l)), 0.0)
        result = result + (hit.material.diffuse * add)
        result = result + ((hit.material.specular * lightSpec) * Math.pow(max(r.dot(v), 0.0), mat))
        result
    }

    def max(left: Double, right: Double): Double = {
        if(left > right) left else right
    }
    override def toString: String = s"Material($emission, $ambient, $diffuse, $specular, $shininess)"
}
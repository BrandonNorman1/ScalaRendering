trait Shape() extends SceneObject{
    def intersect(ray: Ray, h: Hit): (Boolean, Hit)
    def volume: (Double)
}

class Box(min: Point3D, max: Point3D, material: Material) extends Shape{
    override def intersect(r: Ray, h: Hit): (Boolean, Hit) = {
        var ddx = 1.0 / r.direction(0)
        var ddy = 1.0 / r.direction(1)

        var tmin: Double = 0
        var tmax: Double = 0
        var tminy: Double = 0
        var tmaxy: Double = 0

        if (ddx >= 0) {
            tmin = (min(0) - r.origin(0)) * ddx //5
            tmax = (max(0) - r.origin(0)) * ddx //6
        } else {
            tmin = (max(0) - r.origin(0)) * ddx
            tmax = (min(0) - r.origin(0)) * ddx
        }
        if (ddy >= 0) {
            tminy = (min(1) - r.origin(1)) * ddy //0
            tmaxy = (max(1) - r.origin(1)) * ddy //0
        } else {
            tminy = (max(1) - r.origin(1)) * ddy
            tmaxy = (min(1) - r.origin(1)) * ddy
        }
        if ((tmin > tmaxy) || (tminy > tmax)) {
            return (false, h)
        }

        if (tminy > tmin) tmin = tminy
        if (tmaxy < tmax) tmax = tmaxy

        var ddz = 1.0 / r.direction(2)
        var tminz: Double = 0
        var tmaxz: Double = 0

        if (ddz >= 0) {
            tminz = (min(2) - r.origin(2)) * ddz
            tmaxz = (max(2) - r.origin(2)) * ddz
        } else {
            tminz = (max(2) - r.origin(2)) * ddz
            tmaxz = (min(2) - r.origin(2)) * ddz
        }
        if ((tmin > tmaxz) || (tminz > tmax)) {
            return (false, h)
        }
        if (tminz > tmin) tmin = tminz
        if (tmaxz < tmax) tmax = tmaxz

        var epsilon = 0.00000001
        if (tmax > epsilon) {
            h.position = r.origin + (r.direction * tmin)
            h.normal = getNormal(h.position)
            h.material = material
            return (true, h)
        }
        return (false, h)
    }

    def same(a: Double , b: Double, e: Double): Boolean = {
        return ((a <= (b + e)) && ( a >= (b - e)))
    }

    def getNormal(p: Point3D): Vector3D = {
        val eps = 0.000000001
        if      (same (p(0), min(0), eps)) return Vector3D(x = -1.0) 
        else if (same (p(0), max(0), eps)) return Vector3D(x = 1.0) 
        else if (same (p(1), min(1), eps)) return Vector3D(y = -1.0)
        else if (same (p(1), max(1), eps)) return Vector3D(y = 1.0)
        else if (same (p(2), min(2), eps)) return Vector3D(z = -1.0)
        else if (same (p(2), max(2), eps)) return Vector3D(z = 1.0)
        else Vector3D()
    }

    override def volume: Double = {
        (max(0) - min(0)) * (max(1) - min(1)) * (max(2) - min(2))
    }
}

class Sphere(position: Point3D, radius: Double, material: Material) extends Shape{
    override def intersect(r: Ray, h: Hit): (Boolean, Hit) = {
        var eps = 0.00001

        var p = Vector3D(position, r.origin)
        var b = 2 * p.dot(r.direction)
        var c = p.dot(p) - (radius * radius)
        var d = (b * b) - (4 * c)

        if (eps > d) 
        {
            return (false, h)
        }

        var rlen = 0.0
        var t0 = 0.5 * (-b - math.sqrt(d))
        var t1 = 0.5 * (-b + math.sqrt(d))

        if (eps < t0) {
            rlen = t0
        } else if (eps < t1) {
            rlen = t1
        } else {
            return (false, h)
        }

        h.position = r.origin + (r.direction * rlen)
        h.normal = Vector3D(position, h.position).normalize
        h.material = material
        return (true, h)
    }

    override def volume: Double = {
        (4/3) * Math.PI * Math.pow(radius, 3)
    }
}
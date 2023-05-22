class Attenuation(var c: Int = 1, var l: Int = 1, var q: Int = 1){
    override def toString: String = s"Attenuation($c, $l, $q)"
}

trait Light(val ambient: Color, val diffuse: Color, val specular: Color, val atten: Attenuation) extends SceneObject

class DirectionalLight(var direction: Vector3D, override val ambient: Color, override val diffuse: Color, override val specular: Color, override val atten: Attenuation = Attenuation()) extends Light(ambient, diffuse, specular, atten){
    override def toString: String = s"DirectionalLight($direction, $ambient, $diffuse, $specular, $atten)"
}

class PointLight(var position: Point3D, override val ambient: Color, override val diffuse: Color, override val specular: Color, override val atten: Attenuation = Attenuation()) extends Light(ambient, diffuse, specular, atten)

class SpotLight(var position: Point3D, var direction: Vector3D, var cutoff: Int = 180, var exponent: Int = 1, override val ambient: Color, override val diffuse: Color, override val specular: Color, override val atten: Attenuation = Attenuation()) extends Light(ambient, diffuse, specular, atten){
    if(cutoff != 180){ 
        if(cutoff < 0) cutoff = 0 else if(cutoff > 90) cutoff = 90
    }
    if(exponent < 0) exponent = 0 else if(exponent > 128) exponent = 128

    override def toString: String = s"SpotLight($position, $direction, $cutoff, $exponent, $ambient, $diffuse, $specular, $atten)"
}
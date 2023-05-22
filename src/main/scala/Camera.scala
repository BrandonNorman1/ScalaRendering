import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._

// Note: I have two render functions: Render and RenderWithScale. RenderWithScale does not work, but I am curious if you can pick out what might be wrong with my logic, because
// I sure can't (Other than the mismatch between location of ray and location of insertion into the image. That one, I figured out at some point but its been lost to some iterations).

enum RenderCallbackType:
    case rayCastDebug, rayCast

enum RenderOutputType:
    case ppm

trait SceneObject()

class Camera(var eye: Point3D, var sizeX: Int, var sizeY: Int, var resScale: Int = 1, var renderCallback: RenderCallbackType = RenderCallbackType.rayCastDebug, var renderOutput: RenderOutputType = RenderOutputType.ppm){
    def render(scene: List[List[SceneObject]] = List()): Unit = {
        var result = Color()
        var image = PPM("image.ppm", sizeX, sizeY)
        for
            i <- -sizeX/2 to sizeX/2 - 1
            j <- -sizeY/2 to sizeY/2 - 1
        do
            var ray = Ray(eye, Vector3D(j, i, 0))
            if(renderCallback == RenderCallbackType.rayCastDebug) result = rayCastDebug(ray)
            if(renderCallback == RenderCallbackType.rayCast) result = rayCast(ray, scene)
            //println(i + ", " + j + ", " + result)
            image.write(result, (i + sizeX/2, j + sizeY/2))
        image.flush()
    }

    def rayCastDebug(ray: Ray): Color = {
        var result = Color.Black()

        result.r = ray.direction.x.toFloat
        result.g = ray.direction.y.toFloat
        result.b = ray.direction.z.toFloat

        return result
    }

    def rayCast(ray: Ray, scene: List[List[SceneObject]]): Color = {
        var result = Color.Black()

        var hits: ListBuffer[Hit] = ListBuffer()
        for
            i <- scene(0)
        do
            var hit = Hit()
            var data = (false, hit)
            if(i.isInstanceOf[Box]){
                var box = i.asInstanceOf[Box]
                data = box.intersect(ray, hit)
            }
            if(i.isInstanceOf[Sphere]){
                var sphere = i.asInstanceOf[Sphere]
                data = sphere.intersect(ray, hit)
            }
            if(data(0) == true){
                hits.addOne(data(1))
            }
        if (!hits.isEmpty){
            for
                i <- scene(1)
            do
                result = result + i.asInstanceOf[Light].ambient
            
            // Grab all objects, put them in a list
            var closestList: ListBuffer[(Vector3D, Hit)] = ListBuffer()
            for
                i <- hits
            do
                closestList.addOne((Vector3D(eye, i.position), i))
            
            // Sort list by closest object
            val sortedList = closestList.sortWith((r, l)
                => r(0).length < l(0).length)
            if(sortedList.knownSize > 1){
                println(sortedList(0)(0).length + " or " + sortedList(1)(0).length + " with " + sortedList(0)(1).material.diffuse + " and " + sortedList(1)(1).material.diffuse)
            }
            
            
            var closestHit = sortedList(0)(1)
            result = closestHit.material.diffuse


            for
                i <- scene(1)
            do
                if(i.isInstanceOf[DirectionalLight]){
                    var light = i.asInstanceOf[DirectionalLight]
                    var shadowResult = castShadowRay(closestHit.position, light.direction, scene)
                    if(!shadowResult) result = result + closestHit.material.shade(ray, closestHit, light.direction, light.diffuse, light.specular)
                }
                else if(i.isInstanceOf[SpotLight]){
                    var light = i.asInstanceOf[SpotLight]
                    var shadowResult = castShadowRay(closestHit.position, light.direction, scene)
                    if(!shadowResult) result = result + closestHit.material.shade(ray, closestHit, light.direction, light.diffuse, light.specular)
                    
                }
                else{
                    println("Critical failure!")
                } 
            }
            result
        }

    def castShadowRay(point: Point3D, direction: Vector3D, scene: List[List[SceneObject]]): Boolean = {
        var data = (false, Hit())
        for 
            i <- scene(0)
        do
            var ray = Ray(point, direction)
            
            if(i.isInstanceOf[Box]){
                var box = i.asInstanceOf[Box]
                data = box.intersect(ray, Hit())
            }
            if(i.isInstanceOf[Sphere]){
                var sphere = i.asInstanceOf[Sphere]
                data = sphere.intersect(ray, Hit())
            }
        data(0)
        }


    // Does not work. Causes sideways rendering or blank
    def renderWithScale(scene: List[List[SceneObject]] = List()): Unit = {
        var result = Color()
        var scaleX = sizeX * resScale
        var scaleY = sizeY * resScale
        var image = PPM("image.ppm", scaleX, scaleY)
        var countX = 0
        var countY = scaleY - 1
        var step = BigDecimal(1.0/resScale)
        breakable{
            //  An issue is that the counts aren't reflecting where we are casting
            // Even when fixed, the sideways thing is still happening. I have no idea why nothing is displayed when actually going up in scale.
            for
                i <- BigDecimal(-scaleX/2) to BigDecimal(scaleX/2) by step
                j <- BigDecimal(-scaleY/2) to BigDecimal(scaleY/2) by step
            do
                if(countY == -1){
                    countY = scaleY - 1
                    countX += 1
                } 
                if(countX == scaleX) break
                var ray = Ray(eye, Vector3D(i.doubleValue, j.doubleValue, 0))
                if(renderCallback == RenderCallbackType.rayCastDebug) result = rayCastDebug(ray)
                if(renderCallback == RenderCallbackType.rayCast) result = rayCast(ray, scene)
                //println(i.doubleValue + ", " + j.doubleValue + ", " + result)
                print(s"Counts($countX, $countY), decimals(${i.doubleValue}, ${j.doubleValue}), ")
                image.write(result, (countX, countY))
                countY -= 1
        }
        image.flush()
    }
}

/*
def render(scene: List[List[SceneObject]] = List()): Unit = {
        var result = Color()
        var image = PPM("image.ppm", sizeX, sizeY)
        for
            i <- -sizeX/2 to sizeX/2 - 1
            j <- -sizeY/2 to sizeY/2 - 1
        do
            //var increment = k/resScale
            var ray = Ray(eye, Vector3D(i, j, 0))
            if(renderCallback == RenderCallbackType.rayCastDebug) result = rayCastDebug(ray)
            if(renderCallback == RenderCallbackType.rayCast) result = rayCast(ray, scene)
            //println(i + ", " + j + ", " + result)
            image.write(result, (i + sizeX/2, j + sizeY/2))
        image.flush()
    */

/*var result = Color()
        var image = PPM("image.ppm", sizeX * resScale, sizeY * resScale)
        var countX = 0
        var countY = 0
        var step = BigDecimal(1.0/resScale)
        //println(step)
        breakable{
            for
                i <- BigDecimal(-sizeX/2) to BigDecimal(sizeX/2) by step
                j <- BigDecimal(-sizeY/2) to BigDecimal(sizeY/2) by step
            do
                if(countY == (sizeY * resScale)){
                    countY = 0
                    countX += 1
                } 
                if(countX == sizeX * resScale) break
                
                var ray = Ray(eye, Vector3D(i.doubleValue, j.doubleValue, 0))
                if(renderCallback == RenderCallbackType.rayCastDebug) result = rayCastDebug(ray)
                if(renderCallback == RenderCallbackType.rayCast) result = rayCast(ray, scene)
                println(i.doubleValue + ", " + j.doubleValue + ", " + result)
                //println(countX + ", " + countY + ", " + result)
                //print(s"($countX, $countY), ")

                image.write(result, (countX, countY))
                countY += 1
        }
        image.flush()
        */




    /*    def brokeCast(ray: Ray, scene: List[List[SceneObject]]): Color = {
        var ambient = Color.Black()
        // Get ambient lighting
        for
            i <- scene(1)
        do
            ambient = ambient + i.asInstanceOf[Light].ambient
        
        var result = Color.Black()
        var hits: ListBuffer[Hit] = ListBuffer()
        for
            i <- scene(0)
        do
            var hit = Hit()
            var data = (false, hit)
            if(i.isInstanceOf[Box]){
                var box = i.asInstanceOf[Box]
                data = box.intersect(ray, hit)
                //if(data(0) == true) println(data)
            }
            if(i.isInstanceOf[Sphere]){
                var sphere = i.asInstanceOf[Sphere]
                data = sphere.intersect(ray, hit)
                //if(data(0) == true) println(data)
            }
            if(data(0) == true){
                hits.addOne(data(1))
            }
        if(!hits.isEmpty){
            var closestList: ListBuffer[(Vector3D, Hit)] = ListBuffer()
            for
                i <- hits
            do
                //println(i)
                closestList.addOne((Vector3D(eye, i.position), i))
            val sortedList = closestList.sortWith((r, l)
                => r(0).length < l(0).length)
            
            
            var closestHit = sortedList(0)(1)
            result = ambient + closestHit.material.diffuse

            for
                i <- scene(1)
            do
                if(i.isInstanceOf[DirectionalLight]){
                    var light = i.asInstanceOf[DirectionalLight]
                    var shadowResult = castShadowRay(closestHit.position, light.direction, scene)
                    //if(!shadowResult) result = result + closestHit.material.shade(ray, closestHit, light.direction, light.diffuse)
                }
                else if(i.isInstanceOf[SpotLight]){
                    var light = i.asInstanceOf[SpotLight]
                    var shadowResult = castShadowRay(closestHit.position, light.direction, scene)
                    //if(!shadowResult) result = result + closestHit.material.shade(ray, closestHit, light.direction, light.diffuse)
                    
                }
                else{
                    println("Critical failure!")
                } 
            result
        }
        else{
            result = ambient
            return result
        }
        //Color.Black()
    }*/

    /*breakable{
            //  The issue is that the counts aren't reflecting where we are casting
            for
                i <- BigDecimal(-sizeX/2) to BigDecimal(sizeX/2) by step
                j <- BigDecimal(-sizeY/2) to BigDecimal(sizeY/2) by step
            do
                if(countY == -1){
                    countY = sizeY - 1
                    countX += 1
                } 
                if(countX == sizeX) break
                var ray = Ray(eye, Vector3D(i.doubleValue, j.doubleValue, 0))
                if(renderCallback == RenderCallbackType.rayCastDebug) result = rayCastDebug(ray)
                if(renderCallback == RenderCallbackType.rayCast) result = rayCast(ray, scene)
                //println(i.doubleValue + ", " + j.doubleValue + ", " + result)
                print(s"Counts($countX, $countY), decimals(${i.doubleValue}, ${j.doubleValue}), ")
                image.write(result, (countX, countY))
                countY -= 1
        }*/
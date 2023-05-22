import java.io.PrintWriter
import java.io.File

class PPM(filename: String, var width: Int, var height: Int){
    var contents = Array.ofDim[Color](width, height)
    // Write to file
    def flush(): Boolean = {
        var writer = new PrintWriter(new File(filename))
        writer.write("P3 ")
        writer.write(width + " " + height + " ")
        writer.write("1\n")
        var l = 0
        var f = 0
        for 
            i <- contents
            j <- i
        do
            if(l == width - 1)
            {
                //print(s"flush($l, $f), ")
                writer.write(j.toWriteableRaw + "\n")
                l = 0
                f += 1
            }
            else
            {
                //print(s"flush($l, $f), ")
                writer.write(j.toWriteableRaw)
                l += 1
            }
        writer.flush()
        true
    }

    // Add to contents
    def write(color: Color, index: (Int, Int)): Unit = {
        contents(index(0))(index(1)) = color
    }

    def printA(): String = {
        contents.mkString
    }
} 

/*var f = 0
        for 
            i <- contents
            j <- i
        do
            if(l == width){
                l = 0
                f += 1
            }
            //println(l + ", " + f)
            writer.write(j.toWriteableRaw + "\n")
            l += 1
            */
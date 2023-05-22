class PPMTest extends munit.FunSuite {
  test("write should place value in the appropriate position") {
    var image = PPM("a", 10, 10)
    image.write(Color.White(), (0, 0))
    assert(image.contents(0)(0) == Color.White())
  }
  test("flush should finish") {
    var image = PPM("a", 10, 10)
    image.write(Color.White(), (0, 0))
    image.write(Color.White(), (0, 0))
    assert(image.flush())
  }
  
}

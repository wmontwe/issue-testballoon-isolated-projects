package example

import de.infix.testBalloon.framework.core.testSuite
import kotlin.test.assertEquals

val sampleTest by testSuite("sample") {
    test("adds numbers") {
        assertEquals(4, 2 + 2)
    }
}

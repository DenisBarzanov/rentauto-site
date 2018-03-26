package rentauto

import com.rentautosofia.rentacar.entity.BookedCar
import com.rentautosofia.rentacar.util.getIds
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class UtilityTests {

    @Test
    fun extractsIdsFromBookedCarsCorrectly() {
        val bookedCars = listOf<BookedCar>(BookedCar(1,0, Date(), Date()),
                BookedCar(2,0,Date(),Date()),
                BookedCar(4, 0, Date(), Date()))
        val ids = bookedCars.getIds()
        val expectedIds = listOf(1, 2, 3)
        assertTrue(ids.equals(expectedIds))
    }
}
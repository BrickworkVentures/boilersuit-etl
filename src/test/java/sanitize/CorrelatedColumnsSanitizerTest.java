package sanitize;

import static org.junit.Assert.assertEquals;

import ch.brickwork.bsetl.sanitize.CorrelatedColumnsSanitizer;
import ch.brickwork.bsetl.sanitize.constant.de.Landscapes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class CorrelatedColumnsSanitizerTest {

  @Test
  public void testUniqueness() {
    final CorrelatedColumnsSanitizer correlatedColumnsSanitizer = new CorrelatedColumnsSanitizer(
        new String[][]{
            Landscapes.FEMALE_LANDSCAPE_PREDICATES,
            Landscapes.FEMALE_LANDSCAPES
        }, null);

    final List<String> listOfResults = new ArrayList<>();
    final Set<String> setOfResults = new HashSet<>();
    for (int i = 0; i < 1000; i++) {
      final String result = String
          .valueOf(correlatedColumnsSanitizer.sanitize("Original", i + "", "test"));
      listOfResults.add(result);
      setOfResults.add(result);
      System.out.println(result);
    }

    assertEquals(listOfResults.size(), setOfResults.size());
  }

}

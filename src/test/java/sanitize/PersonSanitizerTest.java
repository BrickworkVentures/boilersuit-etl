package sanitize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ch.brickwork.bsetl.sanitize.PersonSanitizer;
import ch.brickwork.bsetl.sanitize.constant.InternetDomains;
import ch.brickwork.bsetl.sanitize.constant.de.HumanNames;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class PersonSanitizerTest {

  @Test
  public void testPersonSanitizer() {
    final PersonSanitizer personSanitizer = new PersonSanitizer(
        HumanNames.TOP_SWISS_FIRSTNAMES,
        HumanNames.TOP_GERMAN_LASTNAMES,
        InternetDomains.RANDOM_MAIL_DOMAINS,
        new String[] { "myNameCol1", "myNameCol2" },
        new String[] { "myEmailCol1", "myEmailCol2" },
        new String[] { "myFirstNameCol1", "myFirstNameCol2" },
        new String[] { "myLastNameCol1", "myLastNameCol2" },
        true
    );

    final Map<String, Object> record = new HashMap<>();
    record.put("id", 1);
    record.put("myNameCol1", "Original Name");
    record.put("myNameCol2", "Original Name");
    record.put("myFirstNameCol1", "Original FirstName");
    record.put("myFirstNameCol2", "Original FirstName");
    record.put("myLastNameCol1", "Original LastName");
    record.put("myLastNameCol2", "Original LastName");
    record.put("myEmailCol1", "Original Email");
    record.put("myEmailCol2", "Original Email");
    record.put("someUnknownProperty", "Original Value");

    final String sanitizedName1 = Objects.toString(personSanitizer.sanitize("Original Name", Objects.toString(1), "myNameCol1"));
    final String sanitizedName2 = Objects.toString(personSanitizer.sanitize("Original Name", Objects.toString(1), "myNameCol2"));
    final String sanitizedFirstName1 = Objects.toString(personSanitizer.sanitize("Original Firstname", Objects.toString(1), "myFirstNameCol1"));
    final String sanitizedFirstName2 = Objects.toString(personSanitizer.sanitize("Original Firstname", Objects.toString(1), "myFirstNameCol2"));
    final String sanitizedLastName1 = Objects.toString(personSanitizer.sanitize("Original Lastname", Objects.toString(1), "myLastNameCol1"));
    final String sanitizedLastName2 = Objects.toString(personSanitizer.sanitize("Original Lastname", Objects.toString(1), "myLastNameCol2"));
    final String sanitizedEmail1 = Objects.toString(personSanitizer.sanitize("Original Email", Objects.toString(1), "myEmailCol1"));
    final String sanitizedEmail2 = Objects.toString(personSanitizer.sanitize("Original Email", Objects.toString(1), "myEmailCol2"));
    final String sanitizedUnknown = Objects.toString(personSanitizer.sanitize("Original Unknown", Objects.toString(1), "someUnknownProperty"));

    // same kind same value
    assertEquals(sanitizedName1, sanitizedName2);
    assertEquals(sanitizedFirstName1, sanitizedFirstName2);
    assertEquals(sanitizedLastName1, sanitizedLastName2);
    assertEquals(sanitizedEmail1, sanitizedEmail2);

    // there is something in it
    assertTrue(StringUtils.isNotBlank(sanitizedName1));
    assertTrue(StringUtils.isNotBlank(sanitizedFirstName1));
    assertTrue(StringUtils.isNotBlank(sanitizedLastName1));
    assertTrue(StringUtils.isNotBlank(sanitizedEmail1));
    assertTrue(StringUtils.isNotBlank(sanitizedUnknown));

    // special features
    assertTrue(StringUtils.split(sanitizedName1, " ").length == 2);
    assertTrue(StringUtils.contains(sanitizedEmail1, "@"));

    // sanitized?
    assertFalse(sanitizedName1.contains("Original"));
    assertFalse(sanitizedFirstName1.contains("Original"));
    assertFalse(sanitizedLastName1.contains("Original"));
    assertFalse(sanitizedEmail1.contains("Original"));
    assertFalse(sanitizedUnknown.contains("Original"));
  }
  
}

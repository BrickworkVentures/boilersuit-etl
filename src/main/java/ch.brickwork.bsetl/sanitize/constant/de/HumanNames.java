/*
 * Copyright (c) 2019 Brickwork Ventures GmbH, CH-8400 Winterthur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ch.brickwork.bsetl.sanitize.constant.de;

public class HumanNames {

  public static final String[] TOP_GERMAN_LASTNAMES = {
      "Müller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Schulz", "Becker",
      "Hoffmann", "Schäfer", "Koch", "Richter", "Bauer", "Klein", "Wolf", "Schröder", "Neumann",
      "Schwarz", "Zimmermann", "Braun", "Hofmann", "Krüger", "Hartmann", "Lange", "Schmitt",
      "Werner", "Schmitz", "Krause", "Meier", "Lehmann", "Schmid", "Schulze", "Maier", "Köhler",
      "Herrmann", "Walter", "König", "Mayer", "Huber", "Kaiser", "Fuchs", "Peters", "Lang",
      "Scholz", "Möller", "Weiß", "Jung", "Hahn", "Schubert", "Vogel", "Friedrich", "Günther",
      "Keller", "Winkler", "Frank", "Berger", "Roth", "Beck", "Lorenz", "Baumann", "Franke",
      "Albrecht", "Schuster", "Simon", "Ludwig", "Böhm", "Winter", "Kraus", "Martin", "Schumacher",
      "Krämer", "Vogt", "Otto", "Jäger", "Stein", "Groß", "Sommer", "Seidel", "Heinrich", "Haas",
      "Brandt", "Schreiber", "Graf", "Dietrich", "Schulte", "Kühn", "Ziegler", "Kuhn", "Pohl",
      "Engel", "Horn", "Bergmann", "Voigt", "Busch", "Thomas", "Sauer", "Arnold", "Pfeiffer",
      "Wolff", "Beyer", "Seifert", "Ernst", "Lindner", "Hübner", "Kramer", "Jansen", "Franz",
      "Peter", "Hansen", "Wenzel", "Götz", "Paul", "Riedel", "Barth", "Kern", "Hermann", "Nagel",
      "Wilhelm", "Ott", "Bock", "Langer", "Grimm", "Ritter", "Haase", "Lenz", "Förster", "Mohr",
      "Kruse", "Schumann", "Jahn", "Thiel", "Kaufmann", "Zimmer", "Hoppe", "Petersen", "Fiedler",
      "Berg", "Arndt", "Marx", "Lutz", "Fritz", "Kraft", "Michel", "Walther", "Böttcher", "Schütz",
      "Eckert", "Sander", "Thiele", "Reuter", "Reinhardt", "Schindler", "Ebert", "Kunz",
      "Schilling", "Schramm", "Voß", "Nowak", "Hein", "Hesse", "Frey", "Rudolph", "Fröhlich",
      "Beckmann", "Kunze", "Herzog", "Bayer", "Behrens", "Stephan", "Büttner", "Gruber", "Adam",
      "Gärtner", "Witt", "Maurer", "Bender", "Bachmann", "Schultz", "Seitz", "Geiger", "Stahl",
      "Steiner", "Scherer", "Kirchner", "Dietz", "Ullrich", "Kurz", "Breuer", "Gerlach", "Ulrich",
      "Brinkmann", "Fink", "Heinz", "Löffler", "Reichert", "Naumann", "Böhme", "Schröter", "Blum",
      "Göbel", "Moser", "Schlüter", "Brunner", "Körner", "Schenk", "Wirth", "Wegner", "Brand",
      "Wendt", "Stark", "Schwab", "Krebs", "Heller", "Wolter", "Reimann", "Rieger", "Unger",
      "Binder", "Bruns", "Döring", "Menzel", "Buchholz", "Ackermann", "Rose", "Meißner", "Janssen",
      "Bartsch", "May", "Hirsch", "Jakob", "Schiller", "Kopp", "John", "Hinz", "Bach", "Pfeifer",
      "Bischoff", "Engelhardt", "Wilke", "Sturm", "Hildebrandt", "Siebert", "Urban", "Link",
      "Rohde", "Kohl", "Linke", "Wittmann", "Fricke", "Köster", "Gebhardt", "Weiss", "Vetter",
      "Freitag", "Nickel", "Hennig", "Rau", "Münch", "Witte", "Noack", "Renner", "Westphal",
      "Reich", "Will", "Baier", "Kolb", "Brückner", "Marquardt", "Kiefer", "Keil", "Henning",
      "Heinze", "Funk", "Lemke", "Ahrens", "Esser", "Pieper", "Baum", "Conrad", "Schlegel",
      "Fuhrmann", "Decker", "Jacob", "Held", "Röder", "Berndt", "Hanke", "Kirsch", "Neubauer",
      "Hammer", "Stoll", "Erdmann", "Mann", "Philipp", "Schön", "Wiese", "Kremer", "Bartels",
      "Klose", "Mertens", "Schreiner", "Dittrich", "Krieger", "Kröger", "Krug", "Harms", "Henke",
      "Großmann", "Martens", "Heß", "Schrader", "Strauß", "Adler", "Herbst", "Kühne", "Heine",
      "Konrad", "Kluge", "Henkel", "Wiedemann", "Albert", "Popp", "Wimmer", "Karl", "Wahl",
      "Stadler", "Hamann", "Kuhlmann", "Steffen", "Lindemann", "Fritsch", "Bernhardt", "Burkhardt",
      "Preuß", "Metzger", "Bader", "Nolte", "Hauser", "Blank", "Beier", "Klaus", "Probst", "Hess",
      "Zander", "Miller", "Niemann", "Funke", "Haupt", "Burger", "Bode", "Holz", "Jost", "Rauch",
      "Rothe", "Herold", "Jordan", "Anders", "Fleischer", "Wiegand", "Hartung", "Janßen", "Lohmann",
      "Krauß", "Vollmer", "Baur", "Heinemann", "Wild", "Brenner", "Reichel", "Wetzel", "Christ",
      "Rausch", "Hummel", "Reiter", "Mayr", "Knoll", "Kroll", "Wegener", "Beer", "Schade",
      "Neubert", "Merz", "Schüler", "Strobel", "Diehl", "Behrendt", "Glaser", "Feldmann", "Hagen",
      "Jacobs", "Rupp", "Geißler", "Straub", "Hohmann", "Römer", "Stock", "Haag", "Meister",
      "Freund", "Dörr", "Kessler", "Betz", "Seiler", "Altmann", "Weise", "Metz", "Eder", "Busse",
      "Mai", "Wunderlich", "Schütte", "Hentschel", "Voss", "Weis", "Heck", "Born", "Falk", "Raab",
      "Lauer", "Völker", "Bittner", "Merkel", "Sonntag", "Moritz", "Ehlers", "Bertram", "Hartwig",
      "Rapp", "Gerber", "Zeller", "Scharf", "Pietsch", "Kellner", "Bär", "Eichhorn", "Giese",
      "Wulf", "Block", "Opitz", "Gottschalk", "Jürgens", "Greiner", "Wieland", "Benz", "Keßler",
      "Steffens", "Heil", "Seeger", "Stumpf", "Gross", "Bühler", "Eberhardt", "Hiller", "Buck",
      "Weigel", "Schweizer", "Albers", "Heuer", "Pape", "Hempel", "Schott", "Schütze", "Scheffler",
      "Engelmann", "Wiesner", "Runge", "Geyer", "Neuhaus", "Forster", "Oswald", "Radtke", "Heim",
      "Geisler", "Appel", "Weidner", "Seidl", "Moll", "Dorn", "Klemm", "Barthel", "Gabriel",
      "Springer", "Timm", "Engels", "Kretschmer", "Reimer", "Steinbach", "Hensel", "Wichmann",
      "Eichler", "Hecht", "Winkelmann", "Heise", "Noll", "Fleischmann", "Neugebauer", "Hinrichs",
      "Schaller", "Lechner", "Brandl", "Mack", "Gebauer", "Siegel", "Zahn", "Singer", "Michels",
      "Schuler", "Scholl", "Uhlig", "Brüggemann", "Specht", "Bürger", "Eggert", "Baumgartner",
      "Weller", "Schnell", "Börner", "Brauer", "Kohler", "Pfaff", "Auer", "Drescher", "Otte",
      "Frenzel", "Petzold", "Rother", "Hagemann", "Sattler", "Wirtz", "Ruf", "Schirmer", "Sauter",
      "Schürmann", "Junker", "Walz", "Dreyer", "Sievers", "Haller", "Prinz", "Stolz", "Hausmann",
      "Dick", "Lux", "Schnabel", "Elsner", "Kühl", "Gerhardt", "Klotz", "Rabe", "Schick", "Faber",
      "Riedl", "Kranz", "Fries", "Reichelt", "Rösch", "Langner", "Maaß", "Wittig", "Geier", "Finke",
      "Kasper", "Maas", "Bremer", "Rath", "Knapp", "Dittmann", "Kahl", "Volk", "Faust", "Harder",
      "Biermann", "Pütz", "Kempf", "Mielke", "Michaelis", "Yilmaz", "Abel", "Thieme", "Schütt",
      "Hauck", "Cordes", "Eberle", "Schaefer", "Wehner", "Haug", "Fritzsche", "Kilian", "Eggers",
      "Große", "Matthes", "Reinhold", "Paulus", "Dürr", "Bohn", "Thoma", "Schober", "Koller",
      "Korn", "Höhne", "Hering", "Gerdes", "Ullmann", "Jensen", "Endres", "Bernhard", "Leonhardt",
      "Eckhardt", "Schaaf", "Höfer", "Junge", "Rademacher", "Pilz", "Hellwig", "Knorr", "Helbig",
      "Melzer", "Lippert", "Evers", "Bahr", "Klinger", "Heitmann", "Ehrhardt", "Heinrichs",
      "Horstmann", "Behr", "Stöhr", "Drews", "Rudolf", "Sieber", "Theis", "Groth", "Hecker",
      "Weiler", "Kemper", "Rost", "Lück", "Claus", "Hildebrand", "Steinmetz", "Götze", "Trautmann",
      "Blume", "Kurth", "Augustin", "Nitsche", "Janke", "Jahnke", "Klug", "Damm", "Heimann",
      "Strauch", "Schlosser", "Uhl", "Böhmer", "Ries", "Hellmann", "Höhn", "Hertel", "Dreher",
      "Borchert", "Huth", "Sperling", "Just", "Stenzel", "Kunkel", "Lau", "Sprenger", "Schönfeld",
      "Pohlmann", "Heilmann", "Wacker", "Lehner", "Teichmann", "Kaminski", "Vogl", "Gehrke",
      "Hartl", "Vogler", "Schroeder", "Thomsen", "Nitschke", "Engler", "Liedtke", "Wille", "Starke",
      "Friedrichs", "Kirchhoff", "Schwarze", "Balzer", "Reinhard", "Heinen", "Lotz", "Küster",
      "Kretzschmar", "Schöne", "Clemens", "Hornung", "Ulbrich", "Renz", "Hofer", "Ruppert", "Lohse",
      "Schuh", "Amann", "Westermann", "Stiller", "Burmeister", "Alt", "Hampel", "Brockmann",
      "Wessel", "Späth", "Hoyer", "Mader", "Bartel", "Rößler", "Krieg", "Grote", "Schwarzer",
      "Schweitzer", "Scheer", "Bosch", "Zink", "Roos", "Wagener", "Oppermann", "Henze", "Lehnert",
      "Seemann", "Trapp", "Reiß", "David", "Pfeffer", "Grau", "Horst", "Diekmann", "Korte", "Rehm",
      "Wilde", "Schleicher", "Lampe", "Grundmann", "Veit", "Daniel", "Eisele", "Hafner", "Steinert",
      "Sachs", "Pfister", "Kühnel", "Schüller", "Klatt", "Bischof", "Schwabe", "Wendel", "Tietz",
      "Frick", "Buschmann", "Steinke", "Menke", "Baumeister", "Kirschner", "Loos", "Ebner",
      "Kastner", "Wolters", "Orth", "Stange", "Becher", "Reinke", "Brendel", "Behnke", "Schweiger",
      "Kolbe", "Schmidtke", "Süß", "Rühl", "Gläser", "Heider", "Seibert", "Heckmann", "Reitz",
      "Baumgart", "Riemer", "Helm", "Knobloch", "Wörner", "Heyer", "Nguyen", "Baumgärtner", "Grund",
      "Brüning", "Ostermann", "Cremer", "Schauer", "Jacobi", "Ewald", "Fürst", "Widmann", "Otten",
      "Büchner", "Petri", "Fritsche", "Kock", "Ehlert", "Kleine", "Eckstein", "Hacker", "Brandes",
      "Buchner", "Hagedorn", "Keck", "Häusler", "Muth", "Apel", "Heuser", "Bastian", "Kersten",
      "Stamm", "Niemeyer", "Berthold", "Gehrmann", "Weinert", "Schatz", "Hager", "Volkmann",
      "Michael", "Wieczorek", "Wilms", "Burghardt", "Schultze", "Merten", "Schwartz", "Kling",
      "Rode", "Neu", "Mende", "Thies", "Böttger", "Schell", "Spindler", "Pabst", "Grün", "Weiland",
      "Mühlbauer", "Hanisch", "Doll", "Janzen", "Adams", "Hermes", "Haack", "Cramer", "Spies",
      "Stern", "Kugler", "Budde", "Jakobs", "Scheller", "Rösler", "Reiser", "Jonas", "Herr",
      "Ebeling", "Wulff", "Pauli", "Löhr", "Lukas", "Rahn", "Sachse", "Köhn", "Backhaus", "Mahler",
      "Hille", "Kowalski", "Heidrich", "Brück", "Gottwald", "Heidenreich", "Baumgarten", "Hamm",
      "Körber", "Kübler", "Frisch", "Hardt", "Enders", "Bräuer", "Seidler", "Küpper", "Lauterbach",
      "Zeidler", "Eckardt", "Kreuzer", "Schiffer", "Schaper", "Gehring", "Hannemann", "Ortmann",
      "Petry", "Thiemann", "Tiedemann", "Grünewald", "Johannsen", "Scheel", "Volz", "Kunert",
      "Dieckmann", "Bormann", "Obermeier", "Knauer", "Schaub", "Eilers", "Berner", "Pahl",
      "Reinecke", "Herz", "Henn", "Brehm", "Hoff", "Resch", "Ochs", "Krohn", "Lerch", "Raabe",
      "Ehrlich", "Hack", "Friedl", "Reis", "Rogge", "Meurer", "Thelen", "Drechsler", "Hölscher",
      "Morgenstern", "Sommerfeld", "Ebel", "Kellermann", "Rupprecht", "Post", "Hillebrand", "Hill",
      "Paulsen", "Grabowski", "Bolz", "Lorenzen", "Welsch", "Seibel", "Kleinert", "Schröer",
      "Jaeger", "Wächter", "Boldt", "Palm", "Kratz", "Reimers", "Pusch", "Exner", "Dietze", "Wüst",
      "Andres", "Heide", "Kaya", "Reichardt", "Kummer", "Metzner", "Grube", "Ewert", "Grunwald",
      "Habermann", "Zorn", "Fichtner", "Emmerich", "Mangold", "Reif", "Ahlers", "Kästner",
      "Küppers", "Petermann", "Stratmann", "Sailer", "Schuhmacher", "Hoch", "Struck", "Buchmann",
      "Rauscher", "Lüdtke", "Wendler", "Dreier", "Zöller", "Bucher", "Siegert", "Finger", "Hopf",
      "Rieck", "Friese", "Hopp", "Sahin", "Henrich", "Spengler"
  };

  public static final String[] TOP_SWISS_FIRSTNAMES = {
      "Maria", "Anna", "Ursula", "Sandra", "Ruth", "Elisabeth", "Monika", "Claudia", "Verena",
      "Nicole", "Barbara", "Silvia", "Andrea", "Marie", "Daniela", "Christine", "Karin", "Marianne",
      "Erika", "Brigitte", "Margrit", "Laura", "Susanne", "Rita", "Sarah", "Katharina", "Esther",
      "Rosmarie", "Heidi", "Anita", "Manuela", "Doris", "Beatrice", "Sonja", "Rosa", "Yvonne",
      "Sara", "Jacqueline", "Gertrud", "Ana", "Irene", "Franziska", "Julia", "Cornelia", "Fabienne",
      "Gabriela", "Martina", "Eva", "Patricia", "Isabelle", "Sabrina", "Nathalie", "Edith",
      "Alexandra", "Corinne", "Melanie", "Angela", "Nadine", "Alice", "Elena", "Jessica", "Vanessa",
      "Denise", "Simone", "Anne", "Sophie", "Regula", "Nina", "Caroline", "Emma", "Susanna",
      "Carmen", "Tanja", "Lara", "Catherine", "Sabine", "Lea", "Petra", "Céline", "Jasmin",
      "Stefanie", "Therese", "Nadia", "Tamara", "Johanna", "Chantal", "Marina", "Michelle",
      "Christina", "Martha", "Monique", "Adelheid", "Rahel", "Dora", "Lisa", "Janine", "Hedwig",
      "Pia", "Anja", "Diana", "Eveline", "Elsbeth", "Madeleine", "Judith", "Charlotte", "Maja",
      "Françoise", "Eliane", "Renate", "Chiara", "Christiane", "Jennifer", "Michèle", "Bettina",
      "Bernadette", "Aline", "Carla", "Jana", "Helena", "Mirjam", "Lena", "Brigitta", "Astrid",
      "Nadja", "Selina", "Theresia", "Olivia", "Linda", "Lucia", "Stephanie", "Priska", "Lina",
      "Regina", "Valérie", "Valentina", "Ramona", "Sonia", "Sofia", "Agnes", "Elisa", "Iris",
      "Monica", "Hanna", "Cristina", "Livia", "Klara", "Helene", "Dominique", "Elsa", "Beatrix",
      "Paula", "Annemarie", "Magdalena", "Alina", "Giulia", "Margrith", "Patrizia", "Leonie",
      "Marion", "Alessia", "Isabel", "Vera", "Stéphanie", "Liliane", "Nora", "Julie", "Jeannette",
      "Véronique", "Nelly", "Emilie", "Myriam", "Christa", "Ida", "Olga", "Sylvia", "Miriam", "Mia",
      "Marlise", "Sibylle", "Claudine", "Sylvie", "Katja", "Lorena", "Margaretha", "Lydia",
      "Rebecca", "Jolanda", "Luana", "Larissa", "Francesca", "Noemi", "Isabella", "Irma", "Deborah",
      "Martine", "Veronika", "Ingrid", "Michaela", "Florence", "Liselotte", "Suzanne", "Evelyne",
      "Milena", "Mara", "Corina", "Laurence", "Hildegard", "Danielle", "Carole", "Frieda", "Simona",
      "Sandrine", "Cécile", "Marta", "Mélanie", "Luzia", "Béatrice", "Gisela", "Antonia",
      "Gabriele", "Melissa", "Fiona", "Sophia", "Kathrin", "Katrin", "Camille", "Adriana", "Lucie",
      "Erna", "Natalie", "Clara", "Renata", "Teresa", "Luisa", "Emilia", "Josiane", "Lia",
      "Silvana", "Jeannine", "Léa", "Maya", "Sina", "Pascale", "Joëlle", "Gabriella", "Amélie",
      "Claire", "Chloé", "Ines", "Hélène", "Marisa", "Marianna", "Paola", "Alessandra", "Victoria",
      "Ivana", "Sabina", "Mathilde", "Bianca", "Samira", "Kim", "Cindy", "Elvira", "Angelina",
      "Anne-Marie", "Tina", "Valeria", "Annette", "Dorothea", "Alicia", "Tania", "Flavia",
      "Angelika", "Antoinette", "Natalia", "Zoé", "Rachel", "Elodie", "Melina", "Pauline",
      "Seraina", "Liliana", "Giovanna", "Mireille", "Jeanne", "Ariane", "Yvette", "Joana",
      "Natascha", "Muriel", "Thi", "Aurélie", "Tatiana", "Marguerite", "Helga", "Margaritha",
      "Francine", "Giuseppina", "Marija", "Carolina", "Irina", "Fanny", "Louise", "Salome", "Ella",
      "Margareta", "Noémie", "Debora", "Katia", "Viviane", "Amanda", "Sarina", "Marlene",
      "Kristina", "Jasmine", "Manon", "Jenny", "Lilian", "Laetitia", "Marlies", "Irène", "Fatima",
      "Jelena", "Gabrielle", "Ladina", "Carmela", "Anaïs", "Helen", "Emily", "Stefania", "Elin",
      "Virginie", "Audrey", "Bertha", "Antonella", "Elina", "Tatjana", "Rebekka", "Samantha",
      "Carina", "Annina", "Margot", "Evelyn", "Lynn", "Anouk", "Tiziana", "Svenja", "Kerstin",
      "Annamarie", "Berta", "Saskia", "Caterina", "Christelle", "Stella", "Leila", "Arlette",
      "Fatma", "Naomi", "Mila", "Lilly", "Susana", "Aurora", "Rose", "Romina", "Désirée", "Josette",
      "Estelle", "Colette", "Delphine", "Geneviève", "Gerda", "Mariana", "Juliette", "Beatriz",
      "Tabea", "Anina", "Ronja", "Aleksandra", "Delia", "Hannah", "Zoe", "Séverine", "Amina",
      "Veronica", "Jael", "Thérèse", "Gina", "Lotti", "Laure", "Eleonora", "Pamela", "Natacha",
      "Hilda", "Leandra", "Antonietta", "Lidia", "Anastasia", "Alma", "Gisèle", "Franca", "Yolanda",
      "Elise", "Rosemarie", "Birgit", "Selma", "Marie-Louise", "Ilona", "Yara", "Fernanda",
      "Marlis", "Justine", "Micheline", "Irmgard", "Danièle", "Magali", "Alexia", "Elfriede",
      "Loredana", "Cynthia", "Graziella", "Mira", "Géraldine", "Daria", "Emine", "Sylviane", "Mary",
      "Cäcilia", "Soraya", "Marine", "Alena", "Michela", "Jasmina", "Georgette", "Morgane",
      "Ursina", "Katarina", "Marlène", "Juliana", "Ulrike", "Ingeborg", "Heidy", "Svetlana",
      "Serena", "Carine", "Gloria", "Roberta", "Annika", "Agnès", "Bruna", "Marlyse", "Célia",
      "Celine", "Annelise", "Myrta", "Mirjana", "Karen", "Elizabeth", "Ilaria", "Andreia",
      "Pierrette", "Josefina", "Vesna", "Cinzia", "Luna", "Christel", "Jocelyne", "Susan", "Amelia",
      "Suzana", "Karine", "Cecilia", "Elif", "Joanna", "Marlen", "Mina", "Virginia", "Janina",
      "Andrée", "Dina", "Renée", "Aurelia", "Coralie", "Jeanine", "Daniel", "Peter", "Thomas",
      "Hans", "Christian", "Martin", "Andreas", "Michael", "Markus", "Marco", "David", "Patrick",
      "Stefan", "Walter", "Bruno", "Urs", "René", "Marcel", "Roland", "Werner", "Simon", "Pascal",
      "Beat", "Marc", "Paul", "Kurt", "Roger", "Manuel", "André", "Josef", "Rolf", "Antonio",
      "Heinz", "Luca", "Rudolf", "Michel", "Robert", "Nicolas", "Christoph", "Jean", "Samuel",
      "José", "Adrian", "Ernst", "Lukas", "Mario", "Reto", "Philippe", "Fabian", "Alfred",
      "Matthias", "Alexander", "Philipp", "Stephan", "Pierre", "Jürg", "Florian", "Benjamin", "Jan",
      "Anton", "Franz", "Giuseppe", "Johann", "Max", "Kevin", "Fabio", "Roman", "Oliver", "Sandro",
      "Ulrich", "Tobias", "Dominik", "Fritz", "Alain", "Karl", "Jonas", "Olivier", "Gabriel",
      "Alexandre", "Francesco", "Eric", "Bernhard", "Claudio", "Raphael", "Felix", "Dario", "Noah",
      "Claude", "Alessandro", "Albert", "Yves", "Richard", "Carlos", "Erich", "Sven", "Robin",
      "Ivan", "Roberto", "Remo", "Jakob", "Giovanni", "Julien", "Christophe", "Laurent", "Andrea",
      "Erwin", "Hugo", "Matteo", "Vincent", "Bernard", "Tim", "Luis", "François", "Stéphane",
      "Nico", "Cédric", "Joel", "Nicola", "Jonathan", "Louis", "Thierry", "Sebastian", "Diego",
      "Frédéric", "Julian", "Guido", "Jacques", "Sébastien", "Ali", "Mathias", "Jörg", "Heinrich",
      "Armin", "Denis", "Rafael", "Salvatore", "Gian", "Joël", "Lars", "Silvan", "Arthur", "Lucas",
      "Alex", "Elias", "Valentin", "Sascha", "Angelo", "Michele", "Jean-Pierre", "Yannick",
      "Charles", "Johannes", "Davide", "Dominique", "Dominic", "Alois", "Leon", "Fernando", "Georg",
      "Leo", "Stefano", "Patrik", "Pedro", "Manfred", "Herbert", "Leandro", "Loris", "Otto",
      "Frank", "Gerhard", "Luigi", "Paulo", "Eduard", "Jérôme", "Mauro", "Willi", "Raphaël",
      "Hermann", "Vincenzo", "Nils", "Carlo", "Emil", "Paolo", "Francisco", "Sergio", "Ricardo",
      "Renato", "Daniele", "Georges", "Willy", "Jorge", "Mike", "Mohamed", "Maxime", "Juan",
      "Lorenzo", "Lionel", "Alessio", "Franco", "Leonardo", "Serge", "Dieter", "Antoine", "Ramon",
      "Ivo", "John", "Friedrich", "Niklaus", "Miguel", "Silvio", "Timo", "Mattia", "Romain",
      "Jean-Claude", "Joseph", "Flavio", "Hanspeter", "Severin", "Nathan", "Domenico", "Elia",
      "Gilbert", "Liam", "Livio", "Loïc", "Tiago", "Pietro", "Jose", "Kilian", "Guillaume",
      "Didier", "Massimo", "Anthony", "Alberto", "Aaron", "Mathieu", "Yann", "Mark", "Gérard",
      "Andrin", "Dylan", "Fabrice", "Wolfgang", "João", "Maurice", "Moritz", "Levin", "Victor",
      "Pius", "Raymond", "Mehmet", "Damian", "Norbert", "Rui", "Enrico", "Wilhelm", "Marko",
      "William", "Damien", "Joao", "António", "Maurizio", "Ralph", "Colin", "Cyril", "Adrien",
      "Christopher", "Ruben", "Gilles", "Viktor", "Francis", "Maximilian", "Luc", "Theodor",
      "Klaus", "Philip", "Marius", "Joshua", "Nino", "Joaquim", "Jean-Marc", "Arnaud", "Konrad",
      "Eugen", "Lorenz", "Xavier", "Giorgio", "Riccardo", "Emanuel", "Nuno", "Henri", "Ralf", "Ben",
      "Hans-Peter", "Linus", "Jürgen", "Quentin", "Hubert", "Mustafa", "Steve", "Simone", "Janis",
      "Nikola", "Adam", "Arnold", "Cyrill", "Aleksandar", "Adriano", "Fabrizio", "Fabien", "Othmar",
      "Edgar", "Gabriele", "Boris", "Ibrahim", "Nick", "Steven", "Jeremy", "Alexis", "Sylvain",
      "Gianluca", "Milan", "Oscar", "Oskar", "Rainer", "Emmanuel", "Sacha", "Noé", "Edwin", "Enzo",
      "Igor", "Etienne", "James", "Yvan", "Luciano", "Gregor", "Adolf", "Hasan", "Axel", "Dragan",
      "Alan", "Mirko", "Lucien", "Mohammad", "Tom", "Guy", "Raffaele", "Ahmed", "Jens", "Joachim",
      "Dennis", "Jean-Luc", "Théo", "Jason", "Noel", "Patrice", "Léo", "Pablo", "Pasquale",
      "Laurin", "Rico", "Aldo", "Finn", "Danilo", "Christof", "Jean-François", "Ludovic", "Mirco",
      "Omar", "Armando", "Gérald", "Zoran", "Ryan", "Michaël", "Federico", "Silas", "Lenny",
      "Diogo", "Bryan", "Reinhard", "Vitor", "Amir", "Cedric", "Karim", "Rémy", "Ronny", "Helmut",
      "Timon", "Jérémy", "Filipe", "Elio", "Gregory", "Nevio", "Benoît", "Erik", "Dejan", "Gianni",
      "Rocco", "Marvin", "Björn", "Goran", "Justin", "Ahmet", "Luka"
  };
}
/*
 * Copyright 2106 Cetic ASBL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.cetic.tsimulus.test.generators.binary

import be.cetic.tsimulus.config.GeneratorFormat
import be.cetic.tsimulus.test.RTSTest
import org.scalatest.{FlatSpec, Inspectors, Matchers}
import spray.json._
import be.cetic.tsimulus.generators.binary.GreaterThanGenerator


class GreaterThanGeneratorTest extends FlatSpec with Matchers with Inspectors with RTSTest
{
   val source =
      """
        |{
        |   "name": "gt-generator",
        |   "type": "greater-than",
        |   "a": "daily-generator",
        |   "b": "monthly-generator",
        |   "strict": false
        |}
      """.stripMargin

   val source_implicit =
      """
        |{
        |   "name": "gt-generator",
        |   "type": "greater-than",
        |   "a": "daily-generator",
        |   "b": "monthly-generator"
        |}
      """.stripMargin

   "A GT generator" should "be correctly read from a json document" in {
      val generator = GreaterThanGenerator(source.parseJson)

      generator.name shouldBe Some("gt-generator")
      generator.a shouldBe Left("daily-generator")
      generator.b shouldBe Left("monthly-generator")
      generator.strict shouldBe Some(false)
   }

   it should "be correctly extracted from the global extractor" in {
      noException should be thrownBy GeneratorFormat.read(source.parseJson)
   }

   it should "be correctly exported to a json document" in {
      val generator = new GreaterThanGenerator(
         Some("gt-generator"),
         Left("daily-generator"),
         Left("monthly-generator"),
         Some(false)
      )

      generator shouldBe GreaterThanGenerator(generator.toJson)
   }

   "A GT generator with implicit strictness" should "be correctly read from a json document" in {
      val generator = GreaterThanGenerator(source_implicit.parseJson)

      generator.name shouldBe Some("gt-generator")
      generator.a shouldBe Left("daily-generator")
      generator.b shouldBe Left("monthly-generator")
      generator.strict shouldBe None
   }

   it should "be correctly exported to a json document" in {
      val generator = new GreaterThanGenerator(
         Some("gt-generator"),
         Left("daily-generator"),
         Left("monthly-generator"),
         None
      )

      generator shouldBe GreaterThanGenerator(generator.toJson)
   }

   it should "have a correct textual representation" in {
      val generator = new GreaterThanGenerator(
         Some("greater-than-generator"),
         Left("a-generator"),
         Left("b-generator"),
         None
      )

      generator.toString shouldBe """GreaterThan(Some(greater-than-generator), Left(a-generator), Left(b-generator), None)"""
   }
}
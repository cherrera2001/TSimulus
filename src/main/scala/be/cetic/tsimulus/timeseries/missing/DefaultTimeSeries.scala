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

package be.cetic.tsimulus.timeseries.missing

import be.cetic.tsimulus.timeseries.TimeSeries
import org.joda.time.LocalDateTime

/**
  * A time series that produces the first defined value, among the values of
  * underlying time series.
  *
  * If no defined value is available, an undefined value is produced.
  */
case class DefaultTimeSeries[T](generators: Seq[TimeSeries[T]]) extends TimeSeries[T]
{
   override def compute(times: Stream[LocalDateTime]) =
   {
      generators match {
         case Seq() => times.map(t => (t, None))
         case _ => {
            val others = generators.map(c => c.compute(times).map(Seq(_)))
               .reduce((s1, s2) => (s1 zip s2).map(e => e._1 ++ e._2))
               .map(seq => (seq.head._1, seq.map(_._2)))
               .map(entry => (entry._1, entry._2.flatten) )

            others map {
               case(t: LocalDateTime, s: Seq[T]) => (t, s.headOption)
            }
         }
      }
   }

   override def compute(time: LocalDateTime): Option[T] = generators.map(g => g.compute(time)).flatten.headOption
}

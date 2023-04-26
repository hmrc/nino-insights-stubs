/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.ninoinsightsstubs.controllers

import akka.actor.ActorSystem
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.ninoinsightsstubs.models.NinoDetails

class NinoRiskListsControllerSpec extends AnyWordSpec with Matchers {
  import NinoDetails.implicits._

  implicit val system: ActorSystem = ActorSystem("test")

  private val controller = new NinoRiskListsController(Helpers.stubControllerComponents())

  "POST /reject/nino" should {
    "return 200 with result false" in {
      val rejectNinoDetailsPresentInList: NinoDetails = NinoDetails("ZL806201C")
      val fakeRequestWithTrue = FakeRequest("POST", "/reject/nino")
        .withBody(Json.toJson(rejectNinoDetailsPresentInList))
        .withHeaders(CONTENT_TYPE -> "application/json")
      val responseTrue = """{"result": true}"""

      val result = controller.isNinoOnRejectList()(fakeRequestWithTrue)
      status(result) shouldBe Status.OK
      contentAsString(result) shouldBe responseTrue
    }

    "return 200 with result true" in {
      val rejectNinoDetails: NinoDetails = NinoDetails("AB123456A")
      val fakeRequestWithFalse = FakeRequest("POST", "/reject/nino")
        .withBody(Json.toJson(rejectNinoDetails))
        .withHeaders(CONTENT_TYPE -> "application/json")
      val responseFalse = """{"result": false}"""

      val result = controller.isNinoOnRejectList()(fakeRequestWithFalse)
      status(result) shouldBe Status.OK
      contentAsString(result) shouldBe responseFalse
    }
  }
}

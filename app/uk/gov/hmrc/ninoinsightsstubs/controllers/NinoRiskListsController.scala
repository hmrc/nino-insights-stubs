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

import play.api.libs.json._
import play.api.mvc.{Action, BaseController, ControllerComponents}
import uk.gov.hmrc.ninoinsightsstubs.models.NinoDetails

import javax.inject.Inject
import scala.concurrent.Future

import scala.io.Source

class NinoRiskListsController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

  import NinoDetails.implicits._

  lazy val ninoRejectList: List[String] = {
    val bufferedSource = Source.fromURL(getClass.getResource("/data/nino_reject_list.csv"))
    val lines = bufferedSource.getLines().drop(1)
    lines.flatMap(line => line.split(",").map(_.trim).headOption).toList
  }

  def isNinoOnRejectList: Action[JsValue] = Action.async(parse.json) { req =>
    val ninoOnRejectListRequest = Json.fromJson[NinoDetails](req.body)
    ninoOnRejectListRequest.fold(
      _ => {
        Future.successful {
          BadRequest("""{"message": "malformed request payload}""")
        }
      },
      valid =>
        Future.successful {
          ninoRejectList.contains(valid.nino) match {
            case true  => Ok("""{"result": true}""")
            case false => Ok("""{"result": false}""")
          }
        }
    )
  }

}

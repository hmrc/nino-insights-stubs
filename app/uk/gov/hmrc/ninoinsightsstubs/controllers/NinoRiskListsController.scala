/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.ninoinsightsstubs.controllers

import play.api.libs.json._
import play.api.mvc.{Action, BaseController, ControllerComponents}
import uk.gov.hmrc.ninoinsightsstubs.models.NinoDetails

import javax.inject.Inject
import scala.concurrent.Future
import NinoDetails.implicits._

import scala.io.Source
class NinoRiskListsController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {
  val ninoRejectList:List[String] = {
    val bufferedSource = Source.fromURL(getClass.getResource("/data/nino_reject_list.csv"))
    val lines = bufferedSource.getLines().drop(1)
    lines.map(line => line.split(",").map(_.trim).headOption).toList.flatten
  }

  def isNinoOnRejectList: Action[JsValue] = Action.async(parse.json) { req =>

    val ninoOnRejectListRequest = Json.fromJson[NinoDetails](req.body)
    ninoOnRejectListRequest.fold(
      _ => Future.successful(BadRequest("""{"message": "malformed request payload}""")),
      valid => Future.successful(
        ninoRejectList.find(nino => valid.nino.nino.contains(nino))
        match {
          case Some(_)  => Ok("""{"result": true}""")
          case None => Ok("""{"result": false}""")
        })
    )
  }


}

/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.ninoinsightsstubs.models

import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.auth.core.Nino

case class NinoDetails(nino: Nino)

object NinoDetails {
  object implicits {
    implicit val ninoFormat: Format[NinoDetails] = Json.format[NinoDetails]
  }
}

package errorHandlers

import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.{RequestHeader, Result}
import play.api.mvc.Results._

import scala.concurrent.Future
import play.api.mvc.Results.Status



class OwnErrorHandler extends DefaultHttpErrorHandler{
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
  val errorMessage = s"Client Error: $statusCode $message"

  val result: Result = statusCode match {
    case 400 => BadRequest(errorMessage)
    case 401 => Unauthorized(errorMessage)
    case 403 => Forbidden(errorMessage)
    case 404 => NotFound(errorMessage)
    case 405 => MethodNotAllowed(errorMessage)
    case _   => Status(statusCode)(errorMessage)
  }

  Future.successful(result)
}

override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    val errorMessage = s"Server Error: ${exception.getClass.getSimpleName} - ${exception.getMessage}"

    val result: Result = exception match {
      //case _: RequestTimeoutException => GatewayTimeout(errorMessage)
      case _: java.lang.RuntimeException => InternalServerError(errorMessage)
      case _ => Status(500)(errorMessage)
    }

    Future.successful(result)
  }
}

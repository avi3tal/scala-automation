package com.gu.example.page

import com.gu.automation.support.page._
import com.gu.example.page.TestIdLocator._
import org.openqa.selenium.By._
import org.openqa.selenium.support.ui.ExpectedConditions._
import org.openqa.selenium.{WebDriver, WebElement}

import scala.collection.JavaConversions._

/**
 *
 * This is an example of a Page object.  It's a relatively complex example with many alternatives.  Please use the
 * simplest subset required for your project!
 */
case class ExamplePage(implicit driver: WebDriver) {

  /**
   * Having a root is a recommended if your Page/Module/Item represents a section of the actual page
   */
  private def root = driver.findElement(id("root"))

  /**
   * This example uses the relative implicit locator
   */
  private def buttonUsingImplicitRelative = root findElement "first"

  /**
   * This example uses the absolute implicit locator
   *
   * Note that if you are caching by using lazy val, or validating the field when the page is created using val, they
   * will only work if you state the type of the val as WebElement - otherwise it will just fetch when it's needed.
   */
  private def buttonUsingImplicitAbsolute: WebElement = "sub"


  // this is validated as soon as the page loads
  private val second = root findElement id("second")

  // this is cached after the first time it's used
  private lazy val textbox = root findElement xpath("./*[@name='textbox']")

  private def missing = root findElement "asdasdasd"

  private def rows = root findElements "row"

  def waitForExample = {
    // this syntax is a bit clunky and could be improved in future - let me know if you use it and want better
    ExplicitWait().until(elementToBeClickable(buttonUsingImplicitAbsolute))

    WaitGet(buttonUsingImplicitRelative, 30).click()
  }

  def sendText(text: String) = {
    textbox.sendKeys(text)
  }

  def safeGetExample = {
    // click only if it's there
    ElementOption(missing).map(_.click())
    ElementOption(second).map(_.click())
    ElementOption(missing) match {
      case None => println("missing: not there")
      case Some(x) => println(s"missing: found $x")
    }
  }

  def printWhetherPresentAndDisplayed = {
    if (ElementOption(missing).exists(_.isDisplayed)) {
      println("here - won't happen")
    } else {
      println("not here - as expected")
    }
  }

  def multiRowList = {
    println("rows: " + rows.count(_.isDisplayed))
  }

  def getModules = {
    val textModules = rows.map(TextModule(_))
    println("modules: " + textModules)

    // return the resulting page object
    textModules
  }

  def getFromExample =
    textbox.getAttribute("value")

}

/**
 * companion object just to let people go directly to the page.  This is optional, only needed if people need to do that.
 */
object ExamplePage extends PageCompanion[ExamplePage] {

  override val relativeUrl = "/example.html"

  override def makePage(implicit driver: WebDriver) = ExamplePage()

}

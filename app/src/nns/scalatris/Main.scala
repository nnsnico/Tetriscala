package nns.scalatris

import indigo.*
import indigo.scenes.*
import nns.scalatris.scenes.game.GameScene
import cats.implicits._

import scala.scalajs.js.annotation.JSExportTopLevel
import indigo.shared.datatypes.Rectangle

@JSExportTopLevel("IndigoGame")
object Main extends IndigoGame[ViewConfig, StartUpData, Unit, ViewModel]:

  // 1. Initialize View config
  override def boot(flags: Map[String, String]): Outcome[BootResult[ViewConfig]] = Outcome {
    val viewConfig = ViewConfig.default
    val config     = GameConfig(
      viewport = viewConfig.viewport,
      clearColor = RGBA.Black,
      magnification = viewConfig.magnificationLevel,
    )

    val path: String =
      flags.getOrElse("baseUrl", "")

    BootResult(config, viewConfig)
      .withAssets(assets.allAssets(path))
      .withFonts(assets.Font.info)
  }

  // 2. Setup global config (ViewConfig, Assets)
  override def setup(
      viewConfig: ViewConfig,
      assetCollection: AssetCollection,
      dice: Dice,
  ): Outcome[Startup[StartUpData]] = StartUpData.initialize(viewConfig)

  // All Scenes
  override def scenes(viewConfig: ViewConfig): NonEmptyList[Scene[StartUpData, Unit, ViewModel]] =
    NonEmptyList(
      GameScene,
    )

  // The first Scene
  override def initialScene(viewConfig: ViewConfig): Option[SceneName] = Option(GameScene.name)

  // initialize global game model
  override def initialModel(startupData: StartUpData): Outcome[Unit] = Outcome(())

  // on update global game model
  override def updateModel(
      context: FrameContext[StartUpData],
      model: Unit,
  ): GlobalEvent => Outcome[Unit] =
    _ => Outcome(())

  // initialize view state
  override def initialViewModel(startupData: StartUpData, model: Unit): Outcome[ViewModel] =
    Outcome(
      ViewModel.init(startupData),
    )

  // on update view state
  override def updateViewModel(
      context: FrameContext[StartUpData],
      model: Unit,
      viewModel: ViewModel,
  ): GlobalEvent => Outcome[ViewModel] = _ => Outcome(viewModel)

  override def present(
      context: FrameContext[StartUpData],
      model: Unit,
      viewModel: ViewModel,
  ): Outcome[SceneUpdateFragment] = Outcome(
    SceneUpdateFragment
      .empty
      .addLayer(Layer(BindingKey("ui"))),
  )

  override def eventFilters: EventFilters = EventFilters.Restricted

case object GameReset extends GlobalEvent

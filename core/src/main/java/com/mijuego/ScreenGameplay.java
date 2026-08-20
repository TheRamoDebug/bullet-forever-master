package com.mijuego;


import com.ScreensClasses.PausaClass;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import io.github.com.mygdx.game.Main;
import com.utilClasses.*;


public class ScreenGameplay implements Screen {

    private final Main game;
    private static final float WORLD_WIDTH = 16f;
    private static final float WORLD_HEIGHT = 9f;


    private OrthographicCamera cameraFirst;
    private OrthographicCamera cameraSecond;
    private OrthographicCamera cameraThird;

    private Viewport viewportFirst;
    private Viewport viewportSecond;
    private Viewport viewportThird;


    private Sprite plane;
    private Sprite enemySprite;

    private Texture bullet;
    private Texture background;
    private Texture backTexture;
    private Texture enemyTexture;

    private TextureRegion newPlane;


    private ControllerBullets bulletsPlayer;
    private ControllerBullets bulletsEnemy;
    private ControllerEnemies controllerMoreEnemies;


    private Circle colisionPlayer;


    private LevelsController controllerLevelsNew;
    private GameOverOverlay gameOverOverlay;
    private StatsClass statsClass;
    private Controls newController;

    private PausaClass pausaClass;
    private PlayerAnimation playerAnimation;


    private float superCont = 1;
    private float deltaFinal;

    private int level = 0;

    private Vector2 movementPlayer;

    private boolean stop = false;
    private boolean state = true;


    public ScreenGameplay(Main game){
        this.game = game;
    }


    public ScreenGameplay(Main game, int level){
        this.game = game;
        this.level = level;
    }


    @Override
    public void show(){

        if(level == 0){
            level = Player.getNumberLevel();
        }

        MasterClass.stopMusicMenu();
        MasterClass.backgroundMusicGameplay();

        controllerLevelsNew = new LevelsController();


        //this is the screenViews
        cameraFirst = new OrthographicCamera();
        cameraSecond = new OrthographicCamera();
        cameraThird = new OrthographicCamera();

        viewportFirst = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cameraFirst);
        viewportSecond = new StretchViewport(1280, 720, cameraSecond);
        viewportThird = new StretchViewport(1280, 720, cameraThird);

        gameOverOverlay = new GameOverOverlay(viewportThird, game, level);


        // PLAYER START POSITION
        movementPlayer = new Vector2(WORLD_WIDTH / 2f - 0.2f, 0.5f);

        playerAnimation = new PlayerAnimation();

        colisionPlayer = new Circle();

        newController = new Controls();


        enemyTexture = new Texture("Sprites/Enemy1.png");
        bullet = new Texture("Sprites/disparoNew.png");
        background = new Texture("BackgroundsEtc/backgroundGameplay.jfif");
        backTexture = new Texture("BackgroundsEtc/backgroundStats.jpg");

        plane = new Sprite(new Texture("Sprites/playerPlane.png"));

        enemySprite = new Sprite(enemyTexture);
        enemySprite.flip(false, true);


        pausaClass = new PausaClass(viewportThird, game, level);


        bulletsPlayer = new ControllerBullets();
        bulletsEnemy = new ControllerBullets();

        controllerMoreEnemies = new ControllerEnemies();


        statsClass = new StatsClass(viewportSecond, game.batch, level);

        Gdx.input.setInputProcessor(null);
    }


    @Override
    public void render(float delta) {

        ScreenUtils.clear(0f, 0f, 0f, 1f);


        if (Player.isAlive()){
            deltaFinal = delta;
        } else {
            deltaFinal = 0;
        }


        if(stop){
            deltaFinal = 0;
        }


        game.batch.setColor(Color.WHITE);


        //put the viewport, configure the matrix, and another thing xd
        viewportFirst.apply();
        game.batch.setProjectionMatrix(cameraFirst.combined);
        game.batch.enableBlending();


        game.batch.begin();


        statsClass.actu();


        //controller for the levels and more(like draw enemys, shots, draw the background and move the enemys
        state = controllerLevelsNew.selectLevel(
            game.batch,
            controllerMoreEnemies,
            bulletsEnemy,
            background,
            enemySprite,
            deltaFinal,
            level,
            movementPlayer
        );


        //functions for draw bullets and collides
        bulletsPlayer.drawBulletsAndCollide(
            game.batch,
            bullet,
            controllerMoreEnemies,
            statsClass
        );

        bulletsEnemy.drawBulletsEnemies(
            game.batch,
            bullet,
            colisionPlayer
        );


        //functions for player
        newPlane = playerAnimation.update(deltaFinal);

        game.batch.draw(newPlane, movementPlayer.x, movementPlayer.y, 0.6f, 0.6f);

        colisionPlayer.set(movementPlayer.x + 0.3f, movementPlayer.y + 0.3f, 0.06f);


        if (Player.isAlive()) {

            newController.controlsKeysShots(
                bulletsPlayer,
                movementPlayer,
                MasterClass.getShotPlayer(),
                deltaFinal
            );

            movementPlayer = newController.controlsKeys(
                movementPlayer,
                deltaFinal
            );
        }


        bulletsEnemy.updateScreen(
            deltaFinal,
            WORLD_WIDTH,
            WORLD_HEIGHT
        );

        bulletsPlayer.updateScreen(
            deltaFinal,
            WORLD_WIDTH,
            WORLD_HEIGHT
        );


        game.batch.end();


        if (!Player.isAlive()) {

            Gdx.input.setInputProcessor(
                gameOverOverlay.getStage()
            );

            gameOverOverlay.render(delta);
            gameOverOverlay.shapeRenderer(
                game.shapeRenderer,
                delta
            );
        }


        viewportSecond.apply();
        game.batch.setProjectionMatrix(cameraSecond.combined);


        game.batch.begin();

        game.batch.draw(
            backTexture,
            0,
            0,
            viewportSecond.getWorldWidth(),
            viewportSecond.getWorldHeight()
        );

        game.batch.end();


        statsClass.render(delta);


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        );


        viewportFirst.apply();
        game.shapeRenderer.setProjectionMatrix(
            cameraFirst.combined
        );


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            stop = !stop;
        }


        if (stop) {
            pausaClass.render(delta);
            pausaClass.shapeRenderer(game.shapeRenderer);
        }


        if(state) {

            if (superCont > 0) {
                superCont -= delta * 0.5f;
            } else {
                superCont = 0;
            }

            game.shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
            );

            game.shapeRenderer.setColor(
                new Color(1f, 1f, 1f, superCont)
            );

            game.shapeRenderer.rect(
                0,
                0,
                WORLD_WIDTH,
                WORLD_HEIGHT
            );

            game.shapeRenderer.end();

        }


        if(!state){

            if (superCont <= 1) {
                superCont += delta * 0.5f;
            } else {

                superCont = 1;

                Screen screen = game.getScreen();
                MasterClass.stopMusicGameplay();
                game.setScreen(
                    new ScreenLevels(game)
                );
                MasterClass.backgroundMusicMenu();
                screen.dispose();
            }


            game.shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
            );

            game.shapeRenderer.setColor(
                new Color(1f, 1f, 1f, superCont)
            );

            game.shapeRenderer.rect(
                0,
                0,
                WORLD_WIDTH,
                WORLD_HEIGHT
            );

            game.shapeRenderer.end();
        }
    }


    @Override
    public void resize(int width, int height) {

        int widthFirst = (int) (width * 0.75f);
        int widthSecond = width - widthFirst;


        viewportFirst.update(
            widthFirst,
            height,
            true
        );

        viewportFirst.setScreenBounds(
            0,
            0,
            widthFirst,
            height
        );


        viewportSecond.update(
            widthSecond,
            height,
            true
        );

        viewportSecond.setScreenBounds(
            widthFirst,
            0,
            widthSecond,
            height
        );


        viewportThird.update(
            widthFirst,
            height,
            true
        );

        viewportThird.setScreenBounds(
            0,
            0,
            widthFirst,
            height
        );
    }


    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}


    @Override
    public void dispose() {

        if (plane != null && plane.getTexture() != null) {
            plane.getTexture().dispose();
        }

        if (bullet != null) {
            bullet.dispose();
        }

        if (background != null) {
            background.dispose();
        }

        if (backTexture != null) {
            backTexture.dispose();
        }

        if (enemyTexture != null) {
            enemyTexture.dispose();
        }

        if (gameOverOverlay != null) {
            gameOverOverlay.dispose();
        }

        if (statsClass != null) {
            statsClass.dispose();
        }

        if (playerAnimation != null) {
            playerAnimation.dispose();
        }
    }
}

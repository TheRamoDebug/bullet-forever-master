package com.mijuego;

import com.ScreensClasses.MenuScreenClass;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.utilClasses.MasterClass;
import io.github.com.mygdx.game.Main;

public class ScreenMenu implements Screen{
    private final Main game;
    private Viewport viewportTitle;
    private Texture Title;
    private Texture thunder;
    private Texture backSpace;
    private Camera camera;
    private MenuScreenClass menuClass;


    private static final float WORLD_WIDTH = 16f;
    private static final float WORLD_HEIGHT = 9f;


    private float cont;
    public float superCont = 1;



    private Sprite gear;
    private Sprite gear2;

    private BitmapFont font;
    private Stage stage;
    private Skin skin;


    public ScreenMenu(Main game) {
        this.game = game;
    }


    @Override
    public void show() {
        MasterClass.backgroundMusicMenu();
        menuClass = new MenuScreenClass();
        camera = new OrthographicCamera();
        viewportTitle = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        gear = new Sprite(new Texture("Sprites/gear.png"));
        gear2 = new Sprite(new Texture("Sprites/gear.png"));
        thunder = new Texture("things/thunderV2.png");
        backSpace = new Texture("BackgroundsEtc/backgroundSpaceTitle.jpg");
        Title = new Texture("BackgroundsEtc/backgroundTitle.png");





        //method for c buttons
        menuClass.MenuEtc(gear, gear2);

        Viewport uiViewport = new FitViewport(1280, 720);
        stage = new Stage(uiViewport, game.batch);

        skin = new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));

        Table table = new Table();
        table.bottom().padLeft(457);

        font = skin.getFont("title");
        font.getData().setScale(0.35f, 0.3f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        TextButton buttonPlay = new TextButton("JUGAR", skin);
        TextButton buttonLevels = new TextButton("NIVELES", skin);
        TextButton buttonExit = new TextButton("SALIR", skin);
        TextButton buttonOptions = new TextButton("MODO INFINITO", skin);


        buttonPlay.setTransform(true);
        buttonLevels.setTransform(true);
        buttonExit.setTransform(true);
        buttonOptions.setTransform(true);


        menuClass.initializeButtons(buttonPlay, buttonExit, buttonLevels, buttonOptions, table, game);




        stage.addActor(table);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cont += delta;

        game.batch.setProjectionMatrix(camera.combined);
        viewportTitle.apply();
        game.batch.begin();

        menuClass.Background(game.batch, thunder, backSpace, delta);

        menuClass.moveButtons(stage);

        menuClass.MenuTitle(game.batch, gear, gear2, Title, cont, delta);

        game.batch.end();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        game.shapeRenderer.setProjectionMatrix(camera.combined);
        superCont = menuClass.shapeRenderer(superCont,delta,game);


        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height){
        viewportTitle.update(width,height, true);
        stage.getViewport().update(width, height,true);
    }


    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide(){}

    @Override
    public void dispose(){
        Title.dispose();
        thunder.dispose();
        backSpace.dispose();
        gear.getTexture().dispose();
        gear2.getTexture().dispose();


        font.dispose();
        stage.dispose();
        skin.dispose();
    }
}

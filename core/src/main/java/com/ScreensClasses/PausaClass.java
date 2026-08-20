package com.ScreensClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mijuego.ScreenGameplay;
import com.mijuego.ScreenMenu;
import com.utilClasses.MasterClass;
import io.github.com.mygdx.game.Main;


public class PausaClass {
    private final Main main;

    private Stage stage;
    private Table table;
    private Skin skin;
    private BitmapFont bitmapFont;


    private TextButton exit;
    private TextButton retry;


    public PausaClass(Viewport viewport, Main main, int level){
        this.main = main;
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));
        table = new Table();
        table.setFillParent(true);
        table.align(Align.top);




        bitmapFont = skin.getFont("title");
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        retry = new TextButton("REINTENTAR", skin);
        table.add(retry).padTop(120).center().row();

        retry.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Screen screen = main.getScreen();
                main.setScreen(new ScreenGameplay(main, level));
                screen.dispose();
            }
        });


        exit = new TextButton("SALIR", skin);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Screen screen = main.getScreen();
                main.setScreen(new ScreenMenu(main));
                MasterClass.stopMusicGameplay();
                screen.dispose();
            }
        });

        table.add(exit).padTop(280).center().row();



        stage.addActor(table);
    }



    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        stage.act(delta);
        stage.draw();
    }


    public void shapeRenderer(ShapeRenderer sr){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0f, 0f, 0f, 0.5f));
        sr.rect(0, 0, 1280,720);
        sr.end();
    }


}

package com.mijuego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.utilClasses.MasterClass;
import com.utilClasses.Player;
import io.github.com.mygdx.game.Main;

public class GameOverOverlay implements Disposable {
    private Stage stage;
    private Skin skin;
    private TextButton retryButton;
    private TextButton menuButton;

    private Main game;

    private float fadeAlpha = 0;

    public GameOverOverlay(Viewport viewport, Main game, int level) {
        this.game = game;
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(150);

        BitmapFont titleFont = skin.getFont("title");
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);



        retryButton = new TextButton("REINTENTAR", skin);
        menuButton = new TextButton("MENU PRINCIPAL", skin);


        table.add(retryButton).padBottom(200).row();
        table.add(menuButton).row();


        menuButton.setTransform(true);
        retryButton.setTransform(true);

        menuButton.setOrigin(menuButton.getWidth() / 2f, menuButton.getHeight() / 2f);
        retryButton.setOrigin(retryButton.getWidth() / 2f, retryButton.getHeight() / 2f);


        addAnimmation(menuButton);
        addAnimmation(retryButton);

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Screen screen = game.getScreen();
                Player.setHealth(6);
                game.setScreen(new ScreenGameplay(game, level));
                screen.dispose();
            }});

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Screen screen = game.getScreen();
                game.setScreen(new ScreenMenu(game));
                MasterClass.stopMusicGameplay();
                screen.dispose();
            }
        });

        stage.addActor(table);
    }


    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }


    public Stage getStage() {
        return stage;
    }


    public void addAnimmation(Button button){
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    button.clearActions();
                    button.addAction(
                        Actions.scaleTo(1.2f, 1.2f, 0.1f, Interpolation.smooth));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    button.clearActions();
                    button.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.smooth));
                }
            }
        });
    }


    public void shapeRenderer(ShapeRenderer shapeRenderer, float delta){
        if(fadeAlpha < 0.6f) {
            fadeAlpha += delta * 0.5f;
        }
        Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0f, 0f, 0f, fadeAlpha));
        shapeRenderer.rect(0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        shapeRenderer.end();
    }


    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}

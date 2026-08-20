package com.ScreensClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mijuego.ScreenGameplay;
import com.mijuego.ScreenMenu;
import com.utilClasses.Player;
import io.github.com.mygdx.game.Main;


public class LevelsScreenClass {

    private static final float WORLD_WIDTH = 16f;
    private static final float WORLD_HEIGHT = 9f;


    private float destinyY = WORLD_HEIGHT -WORLD_HEIGHT / 4;
    private float posY;



    private float movementBackground = 0;
    private float acum = 0;
    private float delta;


    private Stage stage;
    private Screen screenSelect;
    private Main main;


    private boolean state = true;
    private boolean oneShot = true;
    private boolean twoShot = false;

    private Texture iconlevel1;
    private Texture blackGround;

    private Image level1;
    private Image level2;

    public void Background(Sprite background,Sprite Thunder, float delta, SpriteBatch c){
        this.delta = delta;

        movementBackground += delta * 60f;

        c.setColor(Color.WHITE);


        c.draw(background, 0, -movementBackground / 2, WORLD_WIDTH, WORLD_HEIGHT * 1.2f);
        c.draw(background, 0, 9f - movementBackground / 2, WORLD_WIDTH, WORLD_HEIGHT * 1.2f);

        c.draw(Thunder, -WORLD_WIDTH / 4, -movementBackground, WORLD_WIDTH * 0.5F, WORLD_HEIGHT * 2);
        c.draw(Thunder, -WORLD_WIDTH / 4, WORLD_HEIGHT * 2 - movementBackground, WORLD_WIDTH * 0.5F, WORLD_HEIGHT * 2);

        c.draw(Thunder, -WORLD_WIDTH / 4 + WORLD_WIDTH, -movementBackground, WORLD_WIDTH * 0.5F, WORLD_HEIGHT * 2);
        c.draw(Thunder, -WORLD_WIDTH / 4 + WORLD_WIDTH, WORLD_HEIGHT * 2 - movementBackground, WORLD_WIDTH * 0.5F, WORLD_HEIGHT * 2);


        if (movementBackground >= 16f) {
            movementBackground = 0;
        }
    }


    public void menuTitle(SpriteBatch c, Sprite title,float cont){
        float alpha = MathUtils.clamp(cont / 4f, 0f, 1f);
        posY = Interpolation.elastic.apply(20, destinyY, alpha);

        if(alpha < 1f) {
            c.draw(title, WORLD_WIDTH / 4f, posY, WORLD_WIDTH / 2f, WORLD_WIDTH / 8f);
        } else {
            if (oneShot) {
                oneShot = false;
                title.setPosition(WORLD_WIDTH / 4f, destinyY);
                title.setSize(WORLD_WIDTH / 2f, WORLD_WIDTH / 8f);
                title.setOrigin(title.getWidth() / 2f, title.getHeight() / 2f);
            }
            title.rotate(MathUtils.cos(acum += delta) * 0.05f);
            title.draw(c);
        }
    }

    public void assingTextureAndClick(){
        iconlevel1 = new Texture("things/iconolevel1.jpeg");
        blackGround = new Texture("BackgroundsEtc/backgroundSpaceTitle.jpg");

        switch (Player.getNumberLevel()){
            case 1 -> {
                level1 = new Image(iconlevel1);
                clickLevel1();
                level2 = new Image(blackGround);
            }
            default -> {
                level1 = new Image(iconlevel1);
                level2 = new Image(iconlevel1);
            }
        }

    }

    private void clickLevel1(){
        level1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                expandImage(level1);
                state = false;
                screenSelect = new ScreenGameplay(main, 1);
            }
        });
    }

    private void clickLevel2(){
        level2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                expandImage(level2);
                state = false;
                screenSelect = new ScreenGameplay(main, 2);
            }
        });
    }


    public void organizedImages(Stage stage, Main main) {
        this.main = main;
        this.stage = stage;

        assingTextureAndClick();

        addAnimmation(level1);
        addAnimmation(level2);


        fadeIn(level1);
        fadeIn(level2);

        level1.setPosition(280, 200);
        level2.setPosition(710, 200);

        level1.setSize(300,180);
        level2.setSize(300,180);


        level1.setOrigin(level1.getWidth() / 2f, level1.getHeight() / 2f);
        level2.setOrigin(level2.getWidth() / 2f, level2.getHeight() / 2f);












        stage.addActor(level1);
        stage.addActor(level2);
    }





    public void addAnimmation(Image level){
        level.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    level.clearActions();
                    level.addAction(
                        Actions.scaleTo(1.2f, 1.2f, 0.1f, Interpolation.smooth));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    level.clearActions();
                    level.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.smooth));
                }
            }
        });
    }



    public void expandImage(Image image){
        Gdx.input.setInputProcessor(null);

        level1.addAction(Actions.fadeOut(1f, Interpolation.bounce));
        level2.addAction(Actions.fadeOut(1f, Interpolation.bounce));


        image.clearActions();
        image.addAction(Actions.sequence(
            Actions.delay(0.3f),
            Actions.parallel(
                Actions.sizeTo(1280f, 720f, 2f, Interpolation.smooth),
                Actions.moveTo(0,0,2f,Interpolation.smooth),
                Actions.fadeOut(1f, Interpolation.smooth)
            )
        ));

    }

    public void fadeIn(Image image){
        image.getColor().a = 0f;
        image.addAction(Actions.sequence(
            Actions.fadeIn(2.5f, Interpolation.bounce),
            Actions.run(() -> Gdx.input.setInputProcessor(stage))
        ));

    }





    public float shapeRenderer(float superCont, Main game){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            state = false;
            screenSelect = new ScreenMenu(game);
            level1.addAction(Actions.fadeOut(1f, Interpolation.bounce));
            level2.addAction(Actions.fadeOut(1f, Interpolation.bounce));
        }

        if(state) {
            if (superCont > 0) {
                superCont -= delta * 0.5f;
            } else {
                superCont = 0;
            }

            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(new Color(1f, 1f, 1f, superCont));
            game.shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            game.shapeRenderer.end();
        }
        if(!state){
            if (superCont <= 1) {
                superCont += delta * 0.5f;
            } else {
                superCont = 1;
                Screen screen = game.getScreen();
                game.setScreen(screenSelect);
                screen.dispose();
            }

            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(new Color(1f, 1f, 1f, superCont));
            game.shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            game.shapeRenderer.end();
        }
        return superCont;
    }


    public void dispose(){
        iconlevel1.dispose();
        blackGround.dispose();
    }

}

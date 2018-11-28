import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Shoot_em_up extends PApplet {

PImage backgroundImg, menuImg, mouseImg;

int y;
int x;
int numCol= 10; //used for alien spacing
int Score; 
int bestScore = 0;
int level = 1; //Game level
float speed = 1.5f; //Alien Speed
int playerLives = 3;
int timer = 0;

//For loop controls for array size
int numAliens = 3; //columns
int rowsAliens = 1; //rows
int Remain = numAliens*2;

// Game modes
final int NORMAL = 0; //game running
final int END = 1;
final int PAUSE = 2;
final int WON = 3;
final int MENU = 4;

int gameMode = MENU; //Default game mode on launch  

//class declarations
Defender player1;
Alien alien;
Alien alien2;
Bomb bomb;

//ArrayLists
ArrayList bullets = new ArrayList();
ArrayList bombs = new ArrayList();
//2D Arrays
Alien [][] Line1 = new Alien [5][9];
Alien [][] Line2 = new Alien [5][9];

public void setup() 
{
  size(640, 640);
  loadImages();
  runMenu();
  player1 = new Defender (width/2, 550, 3, 0, 700); //initial spawn location, live, timelastShot, bullet coolDown
  alien = new Alien (-100, 0, 2);
  bomb = new Bomb (-100, 0, 5);
  bullets = new ArrayList();
  bombs = new ArrayList();
  spawnEnemies();
} //ENds void setup

public void draw() 
{
  if ( gameMode == NORMAL) // game running
  {
    drawBackground();
    handleEnemies(); //properties for alien 2D array, class updates and bomb class updates
    spawnBullets(); //drawn bullets when fired
    player1.update();
    alien.dropBomb();
    Overlay(); //overlayed text and graphics
    gameWon(); //properties for pacing a level plus reset for new level
    gameOver(); //conditions for failing game
  } 

  if ( gameMode == END)
  {
    if ( Score > bestScore) // Changes best score if record is beaten
    {
      bestScore = Score;
    }
    background(0);
    textAlign(CENTER);
    text("GAME OVER", width/2, height/5);
    text("Your final score is: "+ Score, width/2, height/5 + 30);
    text("Best score is: " + bestScore, width/2, height/5 + 50);
    text("Press R to restart", width/2, height/2);
    noFill();
    noStroke();

    //reset game parameters for new try
    numAliens = 3;
    Remain = numAliens*2;
    playerLives = 3;
    level = 1;
    speed = 1.5f;
  }
} //Ends void draw 

public void keyPressed() //Keyboard input
{
  if (gameMode == MENU)
  {
    if (key == 'f') 
    {
      gameMode = NORMAL;
    }
  }

  if (gameMode == END)
  {
    if (key == 'r') 
    {
      Score = 0;
      gameMode = NORMAL;
      setup();
    }
  }
} // Ends void keyPressed

public void mousePressed() { //Mouse input
  if (gameMode == NORMAL)
  {
    if (mouseButton == LEFT) {
      player1.shoot(); //trigger for player bullet spawning
    }
  }
} //ENds void mousePressed

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void loadImages()
{
  backgroundImg = loadImage("Tiledbackground.jpg");
  mouseImg = loadImage("Mouse.png");
  mouseImg.resize(29, 42);
  menuImg = loadImage("MenuBackgroundPlain.jpg");
  menuImg.resize(width, height);
} //Ends void loadImages

public void runMenu()
{
  if ( gameMode == MENU) // Menu before game starts
  {
    image (menuImg, 0, 0);
    textAlign(CENTER);
    textSize(25);
    text("Press F to start", width/2, height/2);
    noFill();
    noStroke();

    textSize(20);
    text("Controls", width/2, height/2 + 40);
    textSize(18);
    text("*Move the ship with the mouse", width/2, height/2 + 60);
    text("*Fire with left mouse button", width/2, height/2 + 75);
    textSize(12);
  }
} //Ends void runMenu

public void drawBackground () 
{
  image(backgroundImg, 0, y); //display background over canvas
  image(backgroundImg, 0, y-backgroundImg.height); //draw background twice adjacent
  y = y + 1; //background movement speed

  if (y == backgroundImg.height)
    y=0; //wrap background
} //Ends void drawBackground


public void Overlay() 
{
  text("Score: ", width - width/6, height- 10);
  text("Level: ", 20, height- 10);
  text("Remaining: ", width/6, height- 10);
  text("Lives: ", width - width/4, height -10);
  text(Score, width - width/6 +40, height- 10);
  text(Remain, width/6 + 70, height- 10);
  text(level, 40, height- 10);
  text(playerLives, width - width/5 - 10, height -10);
  stroke(255);
  noFill();
  rect(0, height/1.5f, width -1, height/2 -1);
  noStroke();
  image (mouseImg, width/1.07f, height/1.5f + 10);
} //Ends void Overlay


public void spawnBullets() //Player Bullet spawning
{
  for (int i = 0; i < bullets.size (); i++)
  {
    Bullet b = (Bullet) bullets.get(i);
    b.move();
    b.draw();
    //b.bHitCheck();
  }
} //Ends void spawnBullets

public void spawnEnemies()
{
  for ( int j = 0; j< rowsAliens; j++) // This loop creates rows of aliens
  {
    for (int i = 0; i< numAliens; i++) // This loops creats how many aliens spawn in a line
    { 
      Line1[j][i] = new Alien(35 + i%numCol*60, 50 + PApplet.parseInt(j/numCol)*60, speed); //Alien movement left --> right (x + Xspacing, y + Yspacing, speed)
      Line2[j][i] = new Alien(width-50 - i%numCol*60, 5 + PApplet.parseInt(j/numCol)*60, speed); //Alien movement right --> left (x + Xspacing, y + Yspacing, speed)
    }
  }
} //Ends void spawnEnemies

public void handleEnemies()
{
  for ( int j = 0; j< rowsAliens; j++) // This loop updates the aliens in a column
  {
    for (int i = 0; i< numAliens; i++) // This loop updates the aliens in a row
    { 
      Line1[j][i].update();
      Line1[j][i].move();
      Line2[j][i].update();
      Line2[j][i].moveInverse();
    }
  }

  for (int i = 0; i < bombs.size (); i++)
  {
    Bomb bomb = (Bomb) bombs.get(i);
    bomb.update();
  }
} //Ends void handleEnemies

public void gameOver()
{
  if (playerLives == 0)
  {
    gameMode = END;
  }
} //Ends void gameOver

public void gameWon()
{
  if (Remain == 0)
  {
    gameMode = WON;
    level++; //add one to level
    Score += 20; //add 20 to score for advancing level
    if (numAliens <= 9) // prevents too many aliens spawning
    {
      numAliens = numAliens +1;
    }
    speed = speed + 0.5f; //increase alien speed
    gameMode = NORMAL; //sets game mode for new level
    Remain = numAliens*2; //updates the new level's numbers of aliens left
    setup(); //runs setup again to start the game
  }
} //Ends void gameWon

class Bomb extends Bullet {

  Bomb(float x, float y, float speedY)
  {
    super(x, y, speedY);
  }

  public void draw() //bomb graphics
  {
    stroke(0);
    fill(255);
    rect(x, y, 5, 10);
  }

  public void move() 
  {
    y = y + speedY;
    if (this.y > height || this.y < 0) // if bullet outside of screen remove and stop
    { 
      bombs.remove(this);
      this.speedY = 0;
    }
  }

  public void update() // When called in the main game class this method performs the other methods within this class
  {
    move();
    draw();
  }
}

class Alien 
{
  float x, y;
  float speedX;
  int count;
  PImage alienImg, alienImg2;
  
  //bomb drop delay
  float coolDown = random(10);
  float lastShot = random(10);

  Alien ( float x, float y, float speedX)
  {
    this.x = x;
    this.y = y;
    this.speedX = speedX;

    //**Load images
    //alienImg = loadImage("alien.png"); //temp alien
    //alienImg.resize(50, 50);
    alienImg = loadImage("invader3.png");
    alienImg.resize(50, 40);
    alienImg2 = loadImage("invader4.png");
    alienImg2.resize(50, 40);
  }

  public void render()
  {
     // Animate the alien by switching between two images depending on the count number.

    if (count <= 20) {
      image ( alienImg, this.x - 25, this.y);
    } else if (count <= 40) {
      image (alienImg2, this.x - 25, this.y);
    } else {
      image (alienImg, this.x - 25, this.y);
      count = 0;
    }
    count = count + 1;
    //image(alienImg, this.x - 25, this.y);
  }

  public void move()
  {
    this.x = this.x + this.speedX;
    if (this.x > 590) { //right wall collision & reverse movement
      this.x = 590;
      this.speedX = -this.speedX;
      this.y+=90; //move down after collision
    }

    if (this.x < 35) { //left wall collision & reverse movement
      this.speedX = -this.speedX;
      this.x = 35;
      this.y+=90; //move down after collision
    }
  }

  public void moveInverse()
  {
    this.x = this.x - this.speedX;
    if (this.x > 590) { //right wall collision & reverse movement
      this.x = 590;
      this.speedX = -this.speedX;
      this.y+=90; //move down after collision
    }

    if (this.x < 35) { //left wall collision & reverse movement
      this.speedX = -this.speedX;
      this.x = 35;
      this.y+=90; //mfffove down after collision
    }

    if (this.y > height) //if aliens goes off the bottom of the screen
    {
      gameMode = END; 
    }
  }

  public void hitCheck() 
  { //Bullet collision with alien detection
    for (int i = 0; i < bullets.size (); i++) 
    {
      Bullet b = (Bullet) bullets.get(i);
      float distBetween = dist(b.x, b.y, this.x, this.y); //distance between bullet and alien
      if (distBetween < 35) // dist betweem for hit box
      { 
        this.y = -500; //remove hit aliens
        this.x = -500;
        this.speedX = 0;
        Remain--; //remove 1 from remaining aliens
        Score = Score + 10;
        b.y = -100;
      }
    }
  }

  public void dropBomb() 
  { 
    if (second() - lastShot > coolDown) //bullet fire cooldown
    { 
      Bomb bomb = new Bomb(this.x, this.y, 5); //constructor, bullet spawnlocation = ship x,y & -speedY
      bombs.add(bomb); //add bomb to array
      lastShot = second(); 
    }
  }

  public void update() // When called in the main game class this method performs the other methods within this class
  {
    render();
    hitCheck();
    dropBomb();
  }
}

class Bullet
{
  float x, y;
  float speedY;

  Bullet(float x, float y, float speedY)
  {
    this.x = x;
    this.y = y;
    this.speedY = speedY;
  }

  public void draw() 
  {
    fill(255, 0, 0);
    rect(this.x, this.y - 20, 4, 8);
  }

  public void move() 
  {
    this.y = this.y + this.speedY;
    if (this.y > height || this.y < 0) // if bullet outside of screen remove
    { 
      bullets.remove(this);
    }
  }

  //  void bHitCheck() {
  //    for (int i = 0; i < enemies.size (); i++) {
  //      Alien a = (Alien) enemies.get(i);      
  //      float distBetween = dist(a.x, a.y, this.x, this.y); //distance between bullet and alien
  //      if (distBetween < 25 && speedY < 0) { // dist betweem for hit box
  //        bullets.remove(this);
  //Score++;
  //      }
  //    }
  //  }
}
class Defender {
  float x=mouseX;
  float y=mouseY;
  int lives;
  int timeLastShot;
  int coolDown;
  boolean canShoot;

  PImage defenderImg, defenderLeftImg, defenderRightImg;

  Defender (float x, float y, int lives, int timeLastShot, int coolDown) 
  {
    this.x = x;
    this.y = y;
    this.lives = lives;
    this.timeLastShot = timeLastShot;
    this.coolDown = coolDown;

    //load images
    defenderImg = loadImage("ship-center.png");
    defenderImg.resize(100, 75);
    defenderLeftImg = loadImage("ship-left.png");
    defenderLeftImg.resize(100, 75);
    defenderRightImg = loadImage("ship-right.png");
    defenderRightImg.resize(100, 75);
  }

  public void moveShip()
  {
    if (mouseY > height/1.5f) //limit ship movement to portion of screen
    {
      x = mouseX;
      y = mouseY;
    }
  }

  public void render()
  {
    image(defenderImg, x-50, y-37);
  }

  public void shoot() 
  { 
    if (millis() - timeLastShot > coolDown) //bullet fire cooldown
    { 
      Bullet bullet = new Bullet(this.x, this.y, -5); //constructor, bullet spawnlocation = ship x,y & -speedY
      bullets.add(bullet); //add bullet to array
      timeLastShot = millis();
    }
  }

  public void hitCheck() 
  { //Bullet collision with alien detection
    for (int i = 0; i < bombs.size (); i++) 
    {
      Bomb bomb = (Bomb) bombs.get(i);
      float distBetween = dist(bomb.x, bomb.y, this.x, this.y); //distance between bullet and alien
      if (distBetween < 38) // dist betweem for hit box
      { 
        bomb.y = height + 100;
        playerLives--; //remove 1 from life
      }
    }
  }

  public void update() // When called in the main game class this method performs the other methods within this class
  {
    render();
    hitCheck();
    moveShip();
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Shoot_em_up" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

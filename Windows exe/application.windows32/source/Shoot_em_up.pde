PImage backgroundImg, menuImg, mouseImg;

int y;
int x;
int numCol= 10; //used for alien spacing
int Score; 
int bestScore = 0;
int level = 1; //Game level
float speed = 1.5; //Alien Speed
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

void setup() 
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

void draw() 
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
    speed = 1.5;
  }
} //Ends void draw 

void keyPressed() //Keyboard input
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

void mousePressed() { //Mouse input
  if (gameMode == NORMAL)
  {
    if (mouseButton == LEFT) {
      player1.shoot(); //trigger for player bullet spawning
    }
  }
} //ENds void mousePressed

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void loadImages()
{
  backgroundImg = loadImage("Tiledbackground.jpg");
  mouseImg = loadImage("Mouse.png");
  mouseImg.resize(29, 42);
  menuImg = loadImage("MenuBackgroundPlain.jpg");
  menuImg.resize(width, height);
} //Ends void loadImages

void runMenu()
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

void drawBackground () 
{
  image(backgroundImg, 0, y); //display background over canvas
  image(backgroundImg, 0, y-backgroundImg.height); //draw background twice adjacent
  y = y + 1; //background movement speed

  if (y == backgroundImg.height)
    y=0; //wrap background
} //Ends void drawBackground


void Overlay() 
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
  rect(0, height/1.5, width -1, height/2 -1);
  noStroke();
  image (mouseImg, width/1.07, height/1.5 + 10);
} //Ends void Overlay


void spawnBullets() //Player Bullet spawning
{
  for (int i = 0; i < bullets.size (); i++)
  {
    Bullet b = (Bullet) bullets.get(i);
    b.move();
    b.draw();
    //b.bHitCheck();
  }
} //Ends void spawnBullets

void spawnEnemies()
{
  for ( int j = 0; j< rowsAliens; j++) // This loop creates rows of aliens
  {
    for (int i = 0; i< numAliens; i++) // This loops creats how many aliens spawn in a line
    { 
      Line1[j][i] = new Alien(35 + i%numCol*60, 50 + int(j/numCol)*60, speed); //Alien movement left --> right (x + Xspacing, y + Yspacing, speed)
      Line2[j][i] = new Alien(width-50 - i%numCol*60, 5 + int(j/numCol)*60, speed); //Alien movement right --> left (x + Xspacing, y + Yspacing, speed)
    }
  }
} //Ends void spawnEnemies

void handleEnemies()
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

void gameOver()
{
  if (playerLives == 0)
  {
    gameMode = END;
  }
} //Ends void gameOver

void gameWon()
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
    speed = speed + 0.5; //increase alien speed
    gameMode = NORMAL; //sets game mode for new level
    Remain = numAliens*2; //updates the new level's numbers of aliens left
    setup(); //runs setup again to start the game
  }
} //Ends void gameWon


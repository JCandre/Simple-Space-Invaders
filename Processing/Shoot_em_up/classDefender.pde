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

  void moveShip()
  {
    if (mouseY > height/1.5) //limit ship movement to portion of screen
    {
      x = mouseX;
      y = mouseY;
    }
  }

  void render()
  {
    image(defenderImg, x-50, y-37);
  }

  void shoot() 
  { 
    if (millis() - timeLastShot > coolDown) //bullet fire cooldown
    { 
      Bullet bullet = new Bullet(this.x, this.y, -5); //constructor, bullet spawnlocation = ship x,y & -speedY
      bullets.add(bullet); //add bullet to array
      timeLastShot = millis();
    }
  }

  void hitCheck() 
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

  void update() // When called in the main game class this method performs the other methods within this class
  {
    render();
    hitCheck();
    moveShip();
  }
}


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

  void render()
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

  void move()
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

  void moveInverse()
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

  void hitCheck() 
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

  void dropBomb() 
  { 
    if (second() - lastShot > coolDown) //bullet fire cooldown
    { 
      Bomb bomb = new Bomb(this.x, this.y, 5); //constructor, bullet spawnlocation = ship x,y & -speedY
      bombs.add(bomb); //add bomb to array
      lastShot = second(); 
    }
  }

  void update() // When called in the main game class this method performs the other methods within this class
  {
    render();
    hitCheck();
    dropBomb();
  }
}


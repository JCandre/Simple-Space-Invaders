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

  void draw() 
  {
    fill(255, 0, 0);
    rect(this.x, this.y - 20, 4, 8);
  }

  void move() 
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

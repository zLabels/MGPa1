package com.sidm.mobilegamea1;

/**
 * Created by Malcolm on 8/12/2015.
 */
public class Vector2 {

    float x,y;

    Vector2(float a, float b)
    {
        x = a;
        y = b;
    }

    Vector2(Vector2 rhs)
    {
        x = rhs.x;
        y = rhs.y;
    }

    void Set(float a, float b)
    {
        x = a;
        y = b;
    }

    void Set(Vector2 rhs)
    {
        x = rhs.x;
        y = rhs.y;
    }

    void SetZero()
    {
        x = 0;
        y = 0;
    }

    boolean IsZero()
    {
        if(x == 0 && y == 0)
        {
            return true;
        }
        return false;
    }

    Vector2 operatorPlus(Vector2 rhs)
    {
        return new Vector2(x + rhs.x, y + rhs.y);
    }

    void operatorPlusEqual(Vector2 rhs)
    {
        x += rhs.x;
        y += rhs.y;
    }

    Vector2 operatorMinus(Vector2 rhs)
    {
        return new Vector2(x - rhs.x, y - rhs.y);
    }

    void operatorMinusEqual(Vector2 rhs)
    {
        x -= rhs.x;
        y -= rhs.y;
    }

    float Length()
    {
        return (float)Math.sqrt(x*x + y*y);
    }

    Vector2 Normailzed()
    {
        float d = Length();
        return new Vector2(x/d, y/d);
    }
}

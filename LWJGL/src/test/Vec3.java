/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 * Precomputed Atmospheric Scattering
 * Copyright (c) 2008 INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * A 3D vector.
 */
public class Vec3
{

    /**
     * The null vector (0,0,0).
     */
    public static final Vec3 ZERO = new Vec3( 0.0, 0.0, 0.0 );
    /**
     * The unit x vector (1,0,0).
     */
    public static final Vec3 UNIT_X = new Vec3( 1.0, 0.0, 0.0 );
    /**
     * The unit y vector (0,1,0).
     */
    public static final Vec3 UNIT_Y = new Vec3( 0.0, 1.0, 0.0 );
    /**
     * The unit z vector (0,0,1).
     */
    public static final Vec3 UNIT_Z = new Vec3( 0.0, 0.0, 1.0 );
    public double x,  y,  z;

    /**
     * Creates a new, uninitialized vector.
     */
    public Vec3()
    {

    }

    /**
     * Creates a new vector with the given coordinates.
     */
    public Vec3( double xi, double yi, double zi )
    {
        assign( xi, yi, zi );
    }

    /**
     * Creates a new vector with the given coordinates.
     */
    public Vec3( double[] v )
    {
        if( v.length >= 3 )
        {
            assign( v[0], v[1], v[2] );
        }
    }

    /**
     * Creates a new vector as a copy of the given vector.
     */
    public Vec3( Vec3 v )
    {
        assign( v.x, v.y, v.z );
    }

    @Override
    public String toString()
    {
        return "Vec3( " + x + ", " + y + ", " + z + " )";
    }

    /**
     * Returns the coordinate of this vector whose index is given.
     */
    public double get( int index )
    {
        if( index == 0 )
        {
            return x;
        }
        else if( index == 1 )
        {
            return y;
        }
        else if( index == 2 )
        {
            return z;
        }
        return Double.MAX_VALUE;
    }

    /**
     * Assigns the given vector to this vector.
     */
    public void assign( Vec3 v )
    {
        assign( v.x, v.y, v.z );
    }

    public void assign( double x, double y, double z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns true if this vector is equal to the given vector.
     */
    @Override
    public boolean equals( Object obj )
    {
        if( obj instanceof Vec3 )
        {
            Vec3 v = (Vec3) obj;
            return ( x == v.x && y == v.y && z == v.z );
        }
        return false;
    }

    /**
     * Returns true if this vector is different from the given vector.
     */
    public boolean notEqual( final Vec3 v )
    {
        return ( x != v.x || y != v.y || z != v.z );
    }

    /**
     * Returns the sum of this vector and of the given vector.
     */
    public Vec3 sum( final Vec3 v )
    {
        return new Vec3( x + v.x, y + v.y, z + v.z );
    }

    /**
     * Returns the difference of this vector and of the given vector.
     */
    public Vec3 difference( final Vec3 v )
    {
        return new Vec3( x - v.x, y - v.y, z - v.z );
    }

    /**
     * Returns the product of this vector and of the given vector. The
     * product is done component by component.
     */
    public Vec3 product( final Vec3 v )
    {
        return new Vec3( x * v.x, y * v.y, z * v.z );

    }

    /**
     * Returns the product of this vector and of the given scalar.
     */
    public Vec3 product( final double scalar )
    {
        return new Vec3( x * scalar, y * scalar, z * scalar );

    }

    /**
     * Returns the division of this vector and of the given vector. The
     * division is done component by component.
     */
    public Vec3 divide( final Vec3 v )
    {
        return new Vec3( x / v.x, y / v.y, z / v.z );

    }

    /**
     * Returns the division of this vector and of the given scalar.
     */
    public Vec3 divide( double scalar ) throws Exception
    {
        if( scalar != 0 )
        {
            double inv = 1 / scalar;
            return new Vec3( x * inv, y * inv, z * inv );
        }
        throw new Exception( "Division by zero" );

    }

    /**
     * Returns the opposite of this vector.
     */
    public Vec3 negate()
    {
        return new Vec3( -x, -y, -z );

    }

    /**
     * Adds the given vector to this vector.
     */
    public Vec3 addAssign( final Vec3 v )
    {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    /**
     * Substracts the given vector from this vector.
     */
    public Vec3 subtractAssign( final Vec3 v )
    {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;

    }

    /**
     * Multiplies this vector by the given scalar.
     */
    public Vec3 multiplyAssign( final double scalar )
    {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;

    }

    /**
     * Divides this vector by the given scalar.
     */
    public Vec3 divideAssign( final double scalar ) throws Exception
    {
        if( scalar != 0 )
        {
            double inv = 1 / scalar;
            x *= inv;
            y *= inv;
            z *= inv;
            return this;
        }
        throw new Exception( "Division by zero" );
    }

    /**
     * Returns the length of this vector.
     */
    public double length()
    {
        return Math.sqrt( x * x + y * y + z * z );

    }

    /**
     * Returns the squared length of this vector.
     */
    public double squaredLength()
    {
        return ( x * x + y * y + z * z );

    }

    /**
     * Returns the dot product of this vector and of the given vector.
     */
    public double dotProduct( final Vec3 v )
    {
        return ( x * v.x + y * v.y + z * v.z );

    }

    /**
     * Normalizes this vector and returns its initial length.
     */
    public double normalise()
    {
        double length = Math.sqrt( x * x + y * y + z * z );
        double invLength = 1.0 / length;
        x *= invLength;
        y *= invLength;
        z *= invLength;
        return length;

    }

    /**
     * Normalizes this vector to the given length and returns its initial length.
     */
    public double normalise( double len )
    {
        double length = Math.sqrt( x * x + y * y + z * z );
        double invLength = len / length;
        x *= invLength;
        y *= invLength;
        z *= invLength;
        return length;

    }

    /**
     * Returns he cross product of this vector and of the given vector.
     */
    public Vec3 crossProduct( final Vec3 v )
    {
        return new Vec3( y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x );

    }
}
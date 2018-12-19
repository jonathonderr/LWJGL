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
 * A 4x4 matrix.
 */
public class Mat4
{

    public double[][] m = new double[ 4 ][ 4 ];
    public double[] _m = new double[ 16 ];

    /**
     * Creates a new, uninitialized matrix.
     */
    public Mat4()
    {

    }

    /**
     * Creates a new matrix with the given components. The first index is the
     * row index, the second one is the column index.
     */
    public Mat4( double m00, double m01, double m02, double m03,
            double m10, double m11, double m12, double m13,
            double m20, double m21, double m22, double m23,
            double m30, double m31, double m32, double m33 )
    {
        m[0][0] = _m[0] = m00;
        m[0][1] = _m[1] = m01;
        m[0][2] = _m[2] = m02;
        m[0][3] = _m[3] = m03;
        m[1][0] = _m[4] = m10;
        m[1][1] = _m[5] = m11;
        m[1][2] = _m[6] = m12;
        m[1][3] = _m[7] = m13;
        m[2][0] = _m[8] = m20;
        m[2][1] = _m[9] = m21;
        m[2][2] = _m[10] = m22;
        m[2][3] = _m[11] = m23;
        m[3][0] = _m[12] = m30;
        m[3][1] = _m[13] = m31;
        m[3][2] = _m[14] = m32;
        m[3][3] = _m[15] = m33;
    }

    @Override
    public String toString()
    {
        String ret = "\n";
        for( int i = 0; i < _m.length; i++ )
        {
            if( i > 0 && i % 4 == 0 )
            {
                ret += "\n";
            }
            ret += "\t" + _m[i];
        }
        return ret;
    }

    /**
     * Returns the coefficients of this matrix.
     */
    public double[] coefficients()
    {
        double[] ret = new double[ 16 ];
        System.arraycopy( _m, 0, ret, 0, _m.length );
        return ret;
    }

    public float[] coefficientsF()
    {
        float[] ret = new float[ 16 ];
        for( int i = 0; i < _m.length; i++ )
        {
            ret[i] = (float) _m[i];
        }
        return ret;
    }

    /**
     * Returns the row of this matrix whose index is given.
     */
    public double[] getRow( int row )
    {
        return m[row];
    }

    /**
     * Returns true is this matrix is different from the given matrix.
     */
    public boolean notEqual( Mat4 m2 )
    {
        if( m[0][0] != m2.m[0][0] || m[0][1] != m2.m[0][1] || m[0][2] != m2.m[0][2] || m[0][3] != m2.m[0][3] ||
                m[1][0] != m2.m[1][0] || m[1][1] != m2.m[1][1] || m[1][2] != m2.m[1][2] || m[1][3] != m2.m[1][3] ||
                m[2][0] != m2.m[2][0] || m[2][1] != m2.m[2][1] || m[2][2] != m2.m[2][2] || m[2][3] != m2.m[2][3] ||
                m[3][0] != m2.m[3][0] || m[3][1] != m2.m[3][1] || m[3][2] != m2.m[3][2] || m[3][3] != m2.m[3][3] )
        {
            return true;
        }

        return false;
    }

    /**
     * Returns the sum of this matrix and of the given matrix.
     */
    public Mat4 sum( Mat4 m2 )
    {
        Mat4 r = new Mat4();

        r._m[0] = r.m[0][0] = m[0][0] + m2.m[0][0];
        r._m[1] = r.m[0][1] = m[0][1] + m2.m[0][1];
        r._m[2] = r.m[0][2] = m[0][2] + m2.m[0][2];
        r._m[3] = r.m[0][3] = m[0][3] + m2.m[0][3];

        r._m[4] = r.m[1][0] = m[1][0] + m2.m[1][0];
        r._m[5] = r.m[1][1] = m[1][1] + m2.m[1][1];
        r._m[6] = r.m[1][2] = m[1][2] + m2.m[1][2];
        r._m[7] = r.m[1][3] = m[1][3] + m2.m[1][3];

        r._m[8] = r.m[2][0] = m[2][0] + m2.m[2][0];
        r._m[9] = r.m[2][1] = m[2][1] + m2.m[2][1];
        r._m[10] = r.m[2][2] = m[2][2] + m2.m[2][2];
        r._m[11] = r.m[2][3] = m[2][3] + m2.m[2][3];

        r._m[12] = r.m[3][0] = m[3][0] + m2.m[3][0];
        r._m[13] = r.m[3][1] = m[3][1] + m2.m[3][1];
        r._m[14] = r.m[3][2] = m[3][2] + m2.m[3][2];
        r._m[15] = r.m[3][3] = m[3][3] + m2.m[3][3];

        return r;
    }

    /**
     * Returns the difference of this matrix and of the given matrix.
     */
    public Mat4 difference( Mat4 m2 )
    {
        Mat4 r = new Mat4();
        r._m[0] = r.m[0][0] = m[0][0] - m2.m[0][0];
        r._m[1] = r.m[0][1] = m[0][1] - m2.m[0][1];
        r._m[2] = r.m[0][2] = m[0][2] - m2.m[0][2];
        r._m[3] = r.m[0][3] = m[0][3] - m2.m[0][3];

        r._m[4] = r.m[1][0] = m[1][0] - m2.m[1][0];
        r._m[5] = r.m[1][1] = m[1][1] - m2.m[1][1];
        r._m[6] = r.m[1][2] = m[1][2] - m2.m[1][2];
        r._m[7] = r.m[1][3] = m[1][3] - m2.m[1][3];

        r._m[8] = r.m[2][0] = m[2][0] - m2.m[2][0];
        r._m[9] = r.m[2][1] = m[2][1] - m2.m[2][1];
        r._m[10] = r.m[2][2] = m[2][2] - m2.m[2][2];
        r._m[11] = r.m[2][3] = m[2][3] - m2.m[2][3];

        r._m[12] = r.m[3][0] = m[3][0] - m2.m[3][0];
        r._m[13] = r.m[3][1] = m[3][1] - m2.m[3][1];
        r._m[14] = r.m[3][2] = m[3][2] - m2.m[3][2];
        r._m[15] = r.m[3][3] = m[3][3] - m2.m[3][3];

        return r;
    }

    /**
     * Returns the product of this matrix and of the given matrix.
     */
    public Mat4 product( Mat4 m2 )
    {
        Mat4 r = new Mat4();
        r._m[0] = r.m[0][0] = m[0][0] * m2.m[0][0] + m[0][1] * m2.m[1][0] + m[0][2] * m2.m[2][0] + m[0][3] * m2.m[3][0];
        r._m[1] = r.m[0][1] = m[0][0] * m2.m[0][1] + m[0][1] * m2.m[1][1] + m[0][2] * m2.m[2][1] + m[0][3] * m2.m[3][1];
        r._m[2] = r.m[0][2] = m[0][0] * m2.m[0][2] + m[0][1] * m2.m[1][2] + m[0][2] * m2.m[2][2] + m[0][3] * m2.m[3][2];
        r._m[3] = r.m[0][3] = m[0][0] * m2.m[0][3] + m[0][1] * m2.m[1][3] + m[0][2] * m2.m[2][3] + m[0][3] * m2.m[3][3];

        r._m[4] = r.m[1][0] = m[1][0] * m2.m[0][0] + m[1][1] * m2.m[1][0] + m[1][2] * m2.m[2][0] + m[1][3] * m2.m[3][0];
        r._m[5] = r.m[1][1] = m[1][0] * m2.m[0][1] + m[1][1] * m2.m[1][1] + m[1][2] * m2.m[2][1] + m[1][3] * m2.m[3][1];
        r._m[6] = r.m[1][2] = m[1][0] * m2.m[0][2] + m[1][1] * m2.m[1][2] + m[1][2] * m2.m[2][2] + m[1][3] * m2.m[3][2];
        r._m[7] = r.m[1][3] = m[1][0] * m2.m[0][3] + m[1][1] * m2.m[1][3] + m[1][2] * m2.m[2][3] + m[1][3] * m2.m[3][3];

        r._m[8] = r.m[2][0] = m[2][0] * m2.m[0][0] + m[2][1] * m2.m[1][0] + m[2][2] * m2.m[2][0] + m[2][3] * m2.m[3][0];
        r._m[9] = r.m[2][1] = m[2][0] * m2.m[0][1] + m[2][1] * m2.m[1][1] + m[2][2] * m2.m[2][1] + m[2][3] * m2.m[3][1];
        r._m[10] = r.m[2][2] = m[2][0] * m2.m[0][2] + m[2][1] * m2.m[1][2] + m[2][2] * m2.m[2][2] + m[2][3] * m2.m[3][2];
        r._m[11] = r.m[2][3] = m[2][0] * m2.m[0][3] + m[2][1] * m2.m[1][3] + m[2][2] * m2.m[2][3] + m[2][3] * m2.m[3][3];

        r._m[12] = r.m[3][0] = m[3][0] * m2.m[0][0] + m[3][1] * m2.m[1][0] + m[3][2] * m2.m[2][0] + m[3][3] * m2.m[3][0];
        r._m[13] = r.m[3][1] = m[3][0] * m2.m[0][1] + m[3][1] * m2.m[1][1] + m[3][2] * m2.m[2][1] + m[3][3] * m2.m[3][1];
        r._m[14] = r.m[3][2] = m[3][0] * m2.m[0][2] + m[3][1] * m2.m[1][2] + m[3][2] * m2.m[2][2] + m[3][3] * m2.m[3][2];
        r._m[15] = r.m[3][3] = m[3][0] * m2.m[0][3] + m[3][1] * m2.m[1][3] + m[3][2] * m2.m[2][3] + m[3][3] * m2.m[3][3];

        return r;
    }

    /**
     * Returns the product of this matrix and of the given vector. The given
     * vector w coordinate is set to 1, and the 4 vector result is converted
     * to a 3 vector by dividing its xyz components by its w component.
     */
    public Vec3 product( Vec3 v )
    {
        Vec3 r = new Vec3();

        double fInvW = 1.0 / ( m[3][0] * v.x + m[3][1] * v.y + m[3][2] * v.z + m[3][3] );

        r.x = ( m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z + m[0][3] ) * fInvW;
        r.y = ( m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z + m[1][3] ) * fInvW;
        r.z = ( m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z + m[2][3] ) * fInvW;

        return r;

    }

    /**
     * Returns the product of this matrix and of the given scalar.
     */
    public Mat4 product( double f )
    {
        Mat4 r = new Mat4();

        r._m[0] = r.m[0][0] = m[0][0] * f;
        r._m[1] = r.m[0][1] = m[0][1] * f;
        r._m[2] = r.m[0][2] = m[0][2] * f;
        r._m[3] = r.m[0][3] = m[0][3] * f;

        r._m[4] = r.m[1][0] = m[1][0] * f;
        r._m[5] = r.m[1][1] = m[1][1] * f;
        r._m[6] = r.m[1][2] = m[1][2] * f;
        r._m[7] = r.m[1][3] = m[1][3] * f;

        r._m[8] = r.m[2][0] = m[2][0] * f;
        r._m[9] = r.m[2][1] = m[2][1] * f;
        r._m[10] = r.m[2][2] = m[2][2] * f;
        r._m[11] = r.m[2][3] = m[2][3] * f;

        r._m[12] = r.m[3][0] = m[3][0] * f;
        r._m[13] = r.m[3][1] = m[3][1] * f;
        r._m[14] = r.m[3][2] = m[3][2] * f;
        r._m[15] = r.m[3][3] = m[3][3] * f;

        return r;
    }

    /**
     * Returns the transpose of this matrix.
     */
    public Mat4 transpose()
    {
        return new Mat4( m[0][0], m[1][0], m[2][0], m[3][0],
                m[0][1], m[1][1], m[2][1], m[3][1],
                m[0][2], m[1][2], m[2][2], m[3][2],
                m[0][3], m[1][3], m[2][3], m[3][3] );

    }

    private double MINOR( final Mat4 mat, int r0, int r1, int r2, int c0, int c1, int c2 )
    {
        return mat.m[r0][c0] * ( mat.m[r1][c1] * mat.m[r2][c2] - mat.m[r2][c1] * mat.m[r1][c2] ) -
                mat.m[r0][c1] * ( mat.m[r1][c0] * mat.m[r2][c2] - mat.m[r2][c0] * mat.m[r1][c2] ) +
                mat.m[r0][c2] * ( mat.m[r1][c0] * mat.m[r2][c1] - mat.m[r2][c0] * mat.m[r1][c1] );
    }

    /**
     * Returns the adjoint of this matrix.
     */
    public Mat4 adjoint()
    {
        return new Mat4( MINOR( this, 1, 2, 3, 1, 2, 3 ),
                -MINOR( this, 0, 2, 3, 1, 2, 3 ),
                MINOR( this, 0, 1, 3, 1, 2, 3 ),
                -MINOR( this, 0, 1, 2, 1, 2, 3 ),
                -MINOR( this, 1, 2, 3, 0, 2, 3 ),
                MINOR( this, 0, 2, 3, 0, 2, 3 ),
                -MINOR( this, 0, 1, 3, 0, 2, 3 ),
                MINOR( this, 0, 1, 2, 0, 2, 3 ),
                MINOR( this, 1, 2, 3, 0, 1, 3 ),
                -MINOR( this, 0, 2, 3, 0, 1, 3 ),
                MINOR( this, 0, 1, 3, 0, 1, 3 ),
                -MINOR( this, 0, 1, 2, 0, 1, 3 ),
                -MINOR( this, 1, 2, 3, 0, 1, 2 ),
                MINOR( this, 0, 2, 3, 0, 1, 2 ),
                -MINOR( this, 0, 1, 3, 0, 1, 2 ),
                MINOR( this, 0, 1, 2, 0, 1, 2 ) );

    }

    /**
     * Returns the inverse of this matrix.
     */
    public Mat4 inverse()
    {
        return adjoint().product( 1.0f / determinant() );

    }

    /**
     * Returns the determinant of this matrix.
     */
    public double determinant()
    {
        return m[0][0] * MINOR( this, 1, 2, 3, 1, 2, 3 ) -
                m[0][1] * MINOR( this, 1, 2, 3, 0, 2, 3 ) +
                m[0][2] * MINOR( this, 1, 2, 3, 0, 1, 3 ) -
                m[0][3] * MINOR( this, 1, 2, 3, 0, 1, 2 );

    }

    /**
     * Returns the translation matrix corresponding to the given translation
     * vector.
     */
    public static Mat4 translate( final Vec3 v )
    {
        return new Mat4( 1, 0, 0, v.x,
                0, 1, 0, v.y,
                0, 0, 1, v.z,
                0, 0, 0, 1 );

    }

    /**
     * Returns the perspective projection matrix corresponding to the given
     * projection parameters.
     *
     * @param fovy vertical field of view in degrees.
     * @param aspect aspect ratio of the projection window.
     * @param zNear near clipping plane.
     * @param zFar far clipping plane.
     */
    public static Mat4 perspectiveProjection( double fovy, double aspect, double zNear, double zFar )
    {
        double f = 1.0 / Math.tan( fovy / 2 );

        return new Mat4( f / aspect, 0, 0, 0,
                0, f, 0, 0,
                0, 0, ( zFar + zNear ) / ( zNear - zFar ), ( 2 * zFar * zNear ) / ( zNear - zFar ),
                0, 0, -1, 0 );

    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj instanceof Mat4 )
        {
            Mat4 m2 = (Mat4) obj;
            if( m[0][0] != m2.m[0][0] || m[0][1] != m2.m[0][1] || m[0][2] != m2.m[0][2] || m[0][3] != m2.m[0][3] ||
                    m[1][0] != m2.m[1][0] || m[1][1] != m2.m[1][1] || m[1][2] != m2.m[1][2] || m[1][3] != m2.m[1][3] ||
                    m[2][0] != m2.m[2][0] || m[2][1] != m2.m[2][1] || m[2][2] != m2.m[2][2] || m[2][3] != m2.m[2][3] ||
                    m[3][0] != m2.m[3][0] || m[3][1] != m2.m[3][1] || m[3][2] != m2.m[3][2] || m[3][3] != m2.m[3][3] )
            {
                return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 73 * hash + ( this.m != null ? this.m.hashCode() : 0 );
        hash = 73 * hash + ( this._m != null ? this._m.hashCode() : 0 );
        return hash;
    }
}
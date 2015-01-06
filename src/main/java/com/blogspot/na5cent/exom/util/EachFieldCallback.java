/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blogspot.na5cent.exom.util;

import java.lang.reflect.Field;

/**
 * @author redcrow
 */
public interface EachFieldCallback {

        void each(Field field, String name) throws Throwable;
    }

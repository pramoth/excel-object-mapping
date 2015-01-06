/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.na5cent.exom.converter;

/**
 * @author redcrow
 */
public interface TypeConverter<T> {

    T convert(Object value, String... pattern);
}

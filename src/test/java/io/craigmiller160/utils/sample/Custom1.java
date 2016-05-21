/*
 * Copyright 2016 Craig Miller
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.craigmiller160.utils.sample;

/**
 * Created by craig on 5/5/16.
 */
public class Custom1 {

    private String string;
    private boolean bool;
    private int one;
    private int two;

    public Custom1(){}

    public Custom1(String string){
        this.string = string;
    }

    public Custom1(boolean bool, int... nums){
        this.bool = bool;
        if(nums.length > 0){
            one = nums[0];
        }

        if(nums.length > 1){
            two = nums[1];
        }
    }

    public void setString(String string){
        this.string = string;
    }

    public String getString(){
        return string;
    }

    public int getOne(){
        return one;
    }

    public int getTwo(){
        return two;
    }

    public boolean getBool(){
        return bool;
    }

}

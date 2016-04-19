/*
 * Copyright {yyyy} Craig Miller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.craigmiller160.utils.reflect;

/**
 * An extension of RuntimeException to wrap and handle any
 * exceptions directly from the reflective operations of
 * this framework.
 *
 * Created by craig on 3/12/16.
 */
public class ReflectiveException extends RuntimeException{

    public ReflectiveException() {
    }

    public ReflectiveException(String message) {
        super(message);
    }

    public ReflectiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectiveException(Throwable cause) {
        super(cause);
    }
}

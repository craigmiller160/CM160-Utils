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

package io.craigmiller160.utils.repo;

import java.util.List;

/**
 * An interface defining the basic API for
 * a Repository class that follows the
 * Repository Pattern.
 *
 * Created by craigmiller on 4/19/16.
 */
public interface Repo<T> {

    void insert(T entity);

    void update(T entity);

    void delete(T entity);

    List<Object> query(Query query);

}

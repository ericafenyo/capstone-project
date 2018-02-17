/*
 * Copyright (C) 2018 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.eric.quickheadline;

import android.support.annotation.NonNull;

import com.example.eric.quickheadline.model.Bookmark;

/**
 * Created by eric on 16/02/2018.
 */

public class RepositoryContract {

    public interface IBookmark {

        void performInsert(@NonNull final BookmarkDb db, Bookmark bookmark);

        void performQuery(@NonNull BookmarkDb db);

        void loadEmptyState();
    }
}

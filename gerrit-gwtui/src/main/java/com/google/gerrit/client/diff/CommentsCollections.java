begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|Gerrit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
operator|.
name|CommentApi
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
operator|.
name|CommentInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|CallbackGroup
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|NativeMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|Natives
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JsArray
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/** Collection of published and draft comments loaded from the server. */
end_comment

begin_class
DECL|class|CommentsCollections
class|class
name|CommentsCollections
block|{
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
DECL|field|publishedBase
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
name|publishedBase
decl_stmt|;
DECL|field|publishedRevision
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
name|publishedRevision
decl_stmt|;
DECL|field|draftsBase
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
name|draftsBase
decl_stmt|;
DECL|field|draftsRevision
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
name|draftsRevision
decl_stmt|;
DECL|method|load (PatchSet.Id base, PatchSet.Id revision, String path, CallbackGroup group)
name|void
name|load
parameter_list|(
name|PatchSet
operator|.
name|Id
name|base
parameter_list|,
name|PatchSet
operator|.
name|Id
name|revision
parameter_list|,
name|String
name|path
parameter_list|,
name|CallbackGroup
name|group
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|CommentApi
operator|.
name|comments
argument_list|(
name|base
argument_list|,
name|group
operator|.
name|add
argument_list|(
name|publishedBase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|CommentApi
operator|.
name|comments
argument_list|(
name|revision
argument_list|,
name|group
operator|.
name|add
argument_list|(
name|publishedRevision
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|CommentApi
operator|.
name|drafts
argument_list|(
name|base
argument_list|,
name|group
operator|.
name|add
argument_list|(
name|draftsBase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|CommentApi
operator|.
name|drafts
argument_list|(
name|revision
argument_list|,
name|group
operator|.
name|add
argument_list|(
name|draftsRevision
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|publishedBase ()
specifier|private
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
name|publishedBase
parameter_list|()
block|{
return|return
operator|new
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|result
parameter_list|)
block|{
name|publishedBase
operator|=
name|sort
argument_list|(
name|result
operator|.
name|get
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{       }
block|}
return|;
block|}
DECL|method|publishedRevision ()
specifier|private
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
name|publishedRevision
parameter_list|()
block|{
return|return
operator|new
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|result
parameter_list|)
block|{
name|publishedRevision
operator|=
name|sort
argument_list|(
name|result
operator|.
name|get
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{       }
block|}
return|;
block|}
DECL|method|draftsBase ()
specifier|private
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
name|draftsBase
parameter_list|()
block|{
return|return
operator|new
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|result
parameter_list|)
block|{
name|draftsBase
operator|=
name|sort
argument_list|(
name|result
operator|.
name|get
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{       }
block|}
return|;
block|}
DECL|method|draftsRevision ()
specifier|private
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
name|draftsRevision
parameter_list|()
block|{
return|return
operator|new
name|AsyncCallback
argument_list|<
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|NativeMap
argument_list|<
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|result
parameter_list|)
block|{
name|draftsRevision
operator|=
name|sort
argument_list|(
name|result
operator|.
name|get
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{       }
block|}
return|;
block|}
DECL|method|sort (JsArray<CommentInfo> in)
specifier|private
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
name|sort
parameter_list|(
name|JsArray
argument_list|<
name|CommentInfo
argument_list|>
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|CommentInfo
name|c
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|in
argument_list|)
control|)
block|{
name|c
operator|.
name|path
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|in
argument_list|)
argument_list|,
operator|new
name|Comparator
argument_list|<
name|CommentInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|CommentInfo
name|a
parameter_list|,
name|CommentInfo
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|updated
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|updated
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
block|}
end_class

end_unit

